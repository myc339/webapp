package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.timgroup.statsd.StatsDClient;
import neu.edu.csye6225.assignment2.dao.ImageDao;
import neu.edu.csye6225.assignment2.dao.RecipeDao;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.ImageRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.service.AmazonS3ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
class Images{
    private List<ImageRepository> image;
    public Images(List<ImageRepository> image){
        this.image=image;
    }
    public List<ImageRepository> getImage() {
        return image;
    }

    public void setImage(List<ImageRepository> image) {
        this.image = image;
    }
}

@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {
    private String awsS3Bucket;
    private AmazonS3 amazonS3;
//    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private RecipeServiceImpl recipeService;
    private static final Logger log = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private UserDao userDao;
    public static StatsDClient statsd;
    @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AmazonS3 amazonS3,String awsS3Bucket,StatsDClient statsDClient)
    {
        this.amazonS3=amazonS3;
        this.awsS3Bucket=awsS3Bucket;
        this.statsd=statsDClient;
        System.out.println("bucketName:"+this.awsS3Bucket);
        System.out.println("region:"+awsRegion.getName());
    }

//    @Async
    public JSONObject uploadFileToS3Bucket(String recipeId,MultipartFile[] files, boolean enablePublicReadAccess, HttpServletResponse response)
    {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.post_image_times");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String authorId=userRepository.getId();
        ArrayList<ImageRepository> images=new ArrayList<>();
        //check authority
        if(!validateAuthority(recipeId,authorId,response)) return null;
        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);

        try{
//           this.amazonS3.createBucket(awsS3Bucket+".demo1");
            //creating the file in the server
            for ( MultipartFile File :files) {
                //check image or not
                InputStream myInputStream = new ByteArrayInputStream(File.getBytes());
                if (!isImage(myInputStream)) {
                    log.error("Bad Request, this is not image!");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request, this is not image!");
                    statsd.recordExecutionTime("timer.post_image_success", System.currentTimeMillis() - startTime);
                    return null;
                }
            }
            for ( MultipartFile File :files) {
                String fileName=File.getOriginalFilename();
                File file = new File(fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write((File.getBytes()));
                fos.close();
                fileName = new Date().getTime() + "_" + fileName.replace(" ", "_");
                PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3Bucket, fileName, file);

                ImageRepository image = new ImageRepository(recipeRepository);
                this.amazonS3.putObject(putObjectRequest);
                GetObjectMetadataRequest getObjectMetadataRequest=new GetObjectMetadataRequest(this.awsS3Bucket,fileName);
                ObjectMetadata metadata=this.amazonS3.getObjectMetadata(getObjectMetadataRequest);
                image.setMd5(metadata.getETag());
                image.setSize(new DecimalFormat("0.0").format((metadata.getContentLength()*1.0)/1024)+" KB");
                image.setId(UUID.randomUUID().toString());
                image.setCreated_ts(new Date());
                image.setUpdated_ts(new Date());
                image.setBucketName(awsS3Bucket);
                image.setFileName(fileName);
                image.setRegion("http://s3.amazonaws.com");
                image.setUrl(this.amazonS3.getUrl(this.awsS3Bucket,fileName).toString());

                file.delete();
                images.add(image);
            }
            recipeRepository.setImage(images);
            recipeDao.save(recipeRepository);

        }catch(IOException| AmazonServiceException ex)
        {
         log.error("error [" + ex.getMessage() + "] occurred while uploading ");
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         statsd.recordExecutionTime("timer.post_image_success", System.currentTimeMillis() - startTime);
         return null;
        }
        Images ImageData=new Images(images);
        log.info("image uploaded");
        statsd.recordExecutionTime("timer.post_image_success", System.currentTimeMillis() - startTime);
        return  (JSONObject)JSON.toJSON(ImageData);
    }

//    @Async
    public JSONObject deleteFileFromS3Bucket( String recipeId, String imageId,HttpServletResponse response)
    {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.delete_image_times");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String authorId=userRepository.getId();
        if(!validateAuthority(recipeId,authorId,response)) return null;
        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);
        Images images=new Images(recipeRepository.getImage());
        boolean exist=false;
        try{
            for(ImageRepository image:images.getImage())
            {
                if(image.getId().equals(imageId)) {

                    exist = true;
                    amazonS3.deleteObject(new DeleteObjectRequest(image.getBucketName(), image.getFileName()));
                    imageDao.DeleteImage(image.getId());
                    log.info("image deleted");
                    statsd.recordExecutionTime("timer.delete_image", System.currentTimeMillis() - startTime);
                    return null;
                }
            }
            if(!exist)
            {
                log.error("Not Found");
                response.sendError(HttpServletResponse.SC_NOT_FOUND,"Not Found");
                return null;
            }

        }catch (IOException |AmazonServiceException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public JSONObject updateRecipeImage(String recipeId,String imageId,MultipartFile files,HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String authorId=userRepository.getId();
        if(!validateAuthority(recipeId,authorId,response)) return null;
        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);
        Images images=new Images(recipeRepository.getImage());
        boolean exist=false;
        try{
            for(ImageRepository image:images.getImage())
            {
                if(image.getId().equals(imageId)) {
                    exist = true;
                    InputStream myInputStream = new ByteArrayInputStream(files.getBytes());
                    if (!isImage(myInputStream)) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request, this is not image!");
                        return null;
                    }
                    amazonS3.deleteObject(new DeleteObjectRequest(image.getBucketName(), image.getFileName()));

                    String fileName=files.getOriginalFilename();
                    File file = new File(fileName);

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write((files.getBytes()));
                    fos.close();
                    fileName = new Date().getTime() + "_" + fileName.replace(" ", "_");
                    PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3Bucket, fileName, file);
                    this.amazonS3.putObject(putObjectRequest);

                    GetObjectMetadataRequest getObjectMetadataRequest=new GetObjectMetadataRequest(this.awsS3Bucket,fileName);
                    ObjectMetadata metadata=this.amazonS3.getObjectMetadata(getObjectMetadataRequest);
                    image.setMd5(metadata.getETag());
                    image.setSize(new DecimalFormat("0.0").format((metadata.getContentLength()*1.0)/1024)+" KB");

                    image.setUpdated_ts(new Date());
                    image.setBucketName(awsS3Bucket);
                    image.setFileName(fileName);
                    image.setRegion("http://s3.amazonaws.com");
                    image.setUrl(this.amazonS3.getUrl(this.awsS3Bucket,fileName).toString());
                    file.delete();


                    imageDao.save(image);

                    return (JSONObject)JSON.toJSON(image);

                }
            }
            if(!exist)
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,"Not Found");
                return null;
            }

        }catch (IOException |AmazonServiceException e)
        {
            e.printStackTrace();
        }
        return null;

    }
    @Override
    public JSONObject getRecipeImage(String recipeId, String imageId, HttpServletResponse response)
    {
        if (!recipeService.exist(recipeId,response)) {
            return null;
        }
        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);
        Images images=new Images(recipeRepository.getImage());
        boolean exist=false;
        try{
            for(ImageRepository image:images.getImage())
            {
                if(image.getId().equals(imageId)) {
                    exist = true;
                    return (JSONObject) JSON.toJSON(image);
                }
            }
            if(!exist)
            {
                response.sendError(HttpServletResponse.SC_FOUND,"Not Found");
                return null;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public boolean validateAuthority(String recipeId,String authorId,HttpServletResponse response)
    {
        if (!recipeService.exist(recipeId,response)) {
            return false;
        }
        Boolean ownRecipe = recipeService.ownRecipe(recipeId, authorId, response);
        if(!ownRecipe){
            try {
                log.error("you can't update others recipes");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "you can't update others recipes ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    public static boolean isImage(InputStream file)
    {
        try {
            return ImageIO.read(file) != null;
        } catch (Exception e) {
            return false;
        }
    }

}
