package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import neu.edu.csye6225.assignment2.dao.RecipeDao;
import neu.edu.csye6225.assignment2.entity.ImageRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.service.AmazonS3ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {
    private String awsS3Bucket;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider,String awsS3Bucket)
    {
        this.amazonS3= AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3Bucket=awsS3Bucket;
    }
    @Async
    public JSONObject uploadFileToS3Bucket(MultipartFile files, boolean enablePublicReadAccess, String id, HttpServletResponse response)
    {
        String fileName=files.getOriginalFilename();
        ImageRepository image=new ImageRepository();
        if (!exist(id, response)) {
            return null;
        }
        RecipeRepository recipeRepository = recipeDao.getOne(id);
        try{
//            this.amazonS3.createBucket(awsS3Bucket+".demo");
            //creating the file in the server
            File file=new File(fileName);
            FileOutputStream fos=new FileOutputStream(file);
            fos.write((files.getBytes()));
            fos.close();
            fileName=new Date().getTime()+"_"+fileName.replace(" ","_");
            System.out.println("start");
            PutObjectRequest putObjectRequest =new PutObjectRequest(this.awsS3Bucket,fileName,file);
            System.out.println("end");
            if(enablePublicReadAccess)
            {
                putObjectRequest.withCannedAcl((CannedAccessControlList.PublicRead));
            }
            this.amazonS3.putObject(putObjectRequest);
            System.out.println("endput");
            image.setId(UUID.randomUUID().toString());
            image.setUrl("https://s3.amazonaws.com/"+awsS3Bucket+"/"+fileName);
            recipeRepository.setImage(image);
            recipeDao.save(recipeRepository);
            file.delete();

        }catch(IOException| AmazonServiceException ex)
        {
          logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
        return (JSONObject) JSON.toJSON(image);
    }
    @Async
    public JSONObject deleteFileFromS3Bucket( String id, String imageId,HttpServletResponse response)
    {
        RecipeRepository recipeRepository = recipeDao.getOne(id);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3Bucket,recipeRepository.getImage().getUrl().split("//")[2]));

        }catch (AmazonServiceException ex)
        {

        }
        return null;
    }
    public JSONObject getRecipeImage(String id,String imageid,HttpServletResponse response)
    {
        RecipeRepository recipeRepository = recipeDao.getOne(id);
        if(!recipeRepository.getImage().getId().equals(imageid))
        {
            try{
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"Not found");
            return null;
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        }
        return  (JSONObject)JSON.toJSON(recipeRepository.getImage());
    }
    public boolean exist(String recipeId, HttpServletResponse response){
        try {
            //ID不存在时，打印getOne获取的对象才会报错
            //别删
            System.out.println(recipeDao.getOne(recipeId));
            return true;
        }
        catch (Exception e){
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no such recipe with this ID!!!");
                return false;
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            return false;
        }
    }
}
