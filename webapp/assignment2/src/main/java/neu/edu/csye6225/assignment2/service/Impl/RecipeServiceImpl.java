package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.timgroup.statsd.StatsDClient;
import neu.edu.csye6225.assignment2.dao.ImageDao;
import neu.edu.csye6225.assignment2.dao.OrderedListDao;
import neu.edu.csye6225.assignment2.dao.RecipeDao;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.ImageRepository;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.helper.SNSMessageAttributes;
import neu.edu.csye6225.assignment2.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderedListDao orderedListDao;
    private static StatsDClient statsd;
    private AmazonS3 amazonS3;
    private String awsS3Bucket;
    private static final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);
    private AmazonSNS snsClient;
    private String SnsArn;
    @Autowired
    public RecipeServiceImpl(StatsDClient statsDClient, Region awsRegion, AWSCredentialsProvider awsCredentialsProvider,String snsArn, String awsS3Bucket) {
        this.statsd=statsDClient;
        this.snsClient= AmazonSNSClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(awsRegion.getName()).build();
        this.SnsArn=snsArn;
        this.amazonS3= AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3Bucket=awsS3Bucket;

    }

    @Override
    public JSONObject save(RecipeRepository recipeRepository, HttpServletResponse response)
    {
        long startTime=System.currentTimeMillis();

        statsd.incrementCounter("count.post_recipe_times");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String authorId=userRepository.getId();
        if(!checkRequestBody(recipeRepository, response)){
            statsd.recordExecutionTime("timer.post_recipe_fail", System.currentTimeMillis() - startTime);
            return null;
        }

        Date date =new Date();
        recipeRepository.setCreated_ts(date);
        recipeRepository.setUpdated_ts(date);
        recipeRepository.setId(UUID.randomUUID().toString());
        recipeRepository.setAuthor(authorId);
        recipeRepository.setTotal_time_in_min(recipeRepository.getCook_time_in_min()+recipeRepository.getPrep_time_in_min());
        for(OrderedListRepository o : recipeRepository.getSteps()){
            o.setRecipe(recipeRepository);
        }

        recipeRepository.setIngredients1(recipeRepository.getIngredients().toString());
        recipeDao.save(recipeRepository);
//        statsd.recordExecutionTime("POST_RECIPE_TIME", System.currentTimeMillis() - startTime);
        statsd.recordExecutionTime("time.post_recipe_success", System.currentTimeMillis() - startTime);
        log.info("RECIPE_CREATED");
        return (JSONObject) JSON.toJSON(recipeRepository);
    }

    @Override
    public JSONObject updateRecipe(RecipeRepository request, String recipeId,HttpServletResponse response) {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.put_recipe_times");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String authorId=userRepository.getId();
        if (!exist(recipeId, response)) {
            return null;
        }
        Boolean ownRecipe = ownRecipe(recipeId, authorId, response);
        if(!ownRecipe){
            try {
                log.error("you can't update others recipes");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You can't update others recipes ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            statsd.recordExecutionTime("time.put_recipe_fail", System.currentTimeMillis() - startTime);
            return null;
        }

        if(!checkRequestBody(request,response)){
            statsd.recordExecutionTime("time.put_recipe_fail", System.currentTimeMillis() - startTime);
            return null;
        }

        RecipeRepository recipe = recipeDao.getOne(recipeId);
        //load existing nutrition id
        request.getNutrition_information().setId(recipe.getNutrition_information().getId());
        //删除步骤以便更新
        for(OrderedListRepository o: recipe.getSteps()){
            orderedListDao.delete(o);
        }
        recipe.setUpdated_ts(new Date());
        recipe.setCook_time_in_min(request.getCook_time_in_min());
        recipe.setPrep_time_in_min(request.getPrep_time_in_min());
        recipe.setTotal_time_in_min(request.getCook_time_in_min()+request.getPrep_time_in_min());
        recipe.setIngredients(request.getIngredients());
        recipe.setCusine(request.getCusine());
        recipe.setNutrition_information(request.getNutrition_information());
        recipe.setServings(request.getServings());
        recipe.setSteps(request.getSteps());
        recipe.setTitle(request.getTitle());
        //重新设置新建步骤对应的recipe
        for(OrderedListRepository o : request.getSteps()){
            o.setRecipe(recipe);
        }

        recipeDao.save(recipe);
        log.info("recipe updated");
        statsd.recordExecutionTime("time.put_recipe_success", System.currentTimeMillis() - startTime);
        return (JSONObject)JSON.toJSON(recipe);
    }

    @Override
    public JSONObject deleteRecipe(String recipeId,HttpServletResponse response) {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.delete_recipe_times");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String authorId=userRepository.getId();
        if (!exist(recipeId, response)) {
            return null;
        }
        Boolean ownRecipe = ownRecipe(recipeId, authorId, response);
        if(!ownRecipe){
            try {
                log.error("you can't delete others recipes ");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "you can't delete others recipes ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            statsd.recordExecutionTime("time.delete_recipe_fail", System.currentTimeMillis() - startTime);
            return null;
        }

        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);

        Images images=new Images(recipeRepository.getImage());
        boolean exist=false;
        try{
            for(ImageRepository image:images.getImage()) {


                exist = true;
                amazonS3.deleteObject(new DeleteObjectRequest(image.getBucketName(), image.getFileName()));
                imageDao.DeleteImage(image.getId());
                log.info("image deleted");
                statsd.recordExecutionTime("timer.delete_image", System.currentTimeMillis() - startTime);
                return null;

            }
        }catch (AmazonServiceException e)
        {
            e.printStackTrace();
        }

        recipeDao.delete(recipeRepository);
        log.info("recipe deleted");
        statsd.recordExecutionTime("time.delete_recipe_success", System.currentTimeMillis() - startTime);
        return (JSONObject)JSON.toJSON(recipeRepository);
    }

    @Override
    public JSONObject getRecipe(String id, HttpServletResponse response) {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.get_recipe_times");
        if (!exist(id, response)) {
            statsd.recordExecutionTime("time.get_recipe_fail", System.currentTimeMillis() - startTime);
            return null;
        }
        RecipeRepository recipeRepository = recipeDao.getOne(id);
        statsd.recordExecutionTime("time.get_recipe_success", System.currentTimeMillis() - startTime);
        return (JSONObject)JSON.toJSON(recipeRepository);
    }

    public boolean checkRequestBody(RecipeRepository request, HttpServletResponse response){
        //check read only properties
        if(request.getAuthor()!=null || request.getId()!=null||request.getTotal_time_in_min()!=null||request.getCreated_ts()!=null
                ||request.getUpdated_ts()!=null){

            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You can't update field including id,created_ts,updated_ts," +
                        "author_id and total_time_in_min!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //check orderList
        for(OrderedListRepository o : request.getSteps()){
            if(o.getPosition() < 1){
                try {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Position filed of OrderList can't be smaller than 1!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

        }
        //check cook&pre time's Min & Max
        if(request.getCook_time_in_min()%5 != 0 || request.getPrep_time_in_min()%5 != 0 ||request.getCook_time_in_min() < 0 || request.getPrep_time_in_min() < 0){
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cook time & prepare time should be multiple of 5!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //check serving of recipe
        if(request.getServings() < 1 || request.getServings() >5){
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Serving's minimum is 1, maximum is 5!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //check ingredients's unique
        HashSet<String> set = new HashSet<>();
        for(String s : request.getIngredients()){
            if(set.contains(s)){
                try {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ingredients's item should be unique!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            set.add(s);
        }
        return true;
    }

    public boolean ownRecipe(String recipeId, String authorId, HttpServletResponse response){
        List<RecipeRepository> recipeList = recipeDao.findByAuthor(authorId);
        for (RecipeRepository r : recipeList){
            if(r.getId().equals(recipeId)){
                return true;
            }
        }
        return false;
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
    @Override
    public JSONObject getNewestRecipe(HttpServletResponse response)
    {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.get_newest_recipe_times");
        RecipeRepository recipeRepository = recipeDao.findNewestRecipe();
        statsd.recordExecutionTime("time.get_newest_recipe_success", System.currentTimeMillis() - startTime);
        return (JSONObject)JSON.toJSON(recipeRepository);
    }
    @Override
    public JSONObject getRecipeLinks(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        String user_mail=userRepository.getEmail_address();
        Date date =new Date();

        int mindif=(int) (date.getTime()-userRepository.getAccount_updated().getTime())/1000/60;
        String authorId = userRepository.getId();
        List<String> ids = recipeDao.getRecipeIdsByAuthor(authorId);
        String url = request.getRequestURL().toString();
        url = url.substring(0,url.length()-10);
        ArrayList<String> urls = new ArrayList<String>();
        String link ="";
        for (String id : ids){
            urls.add(url+"recipe/" + id);
        }
        for (String e: urls)
        {
            link+=e;
        }
        RecipeLinks links=new RecipeLinks();
        links.setLinks(urls);

        SNSMessageAttributes msg =new SNSMessageAttributes();
        msg.addAttribute("id",user_mail);
        msg.addAttribute("links",urls);
        if(mindif>=30 || userRepository.getAccount_updated().getTime()==userRepository.getAccount_created().getTime())
        {
            links.setMsg("request send");
            userRepository.setAccount_updated(date);
            userDao.save(userRepository);
            // Publish a message to an Amazon SNS topic.
            msg.setMessage(user_mail+"request to get all recipe links");
            msg.publish(snsClient,SnsArn);
//            PublishResult publishResponse = snsClient.publish(new PublishRequest()
//                    .addMessageAttributesEntry("id",new MessageAttributeValue().withStringValue(user_mail))
//                    .addMessageAttributesEntry("links",new MessageAttributeValue().withStringValue(link))
//                   .withTopicArn(SnsArn));

            // Print the MessageId of the message.
//            System.out.println("MessageId: " + publishResponse.getMessageId());
        }
        else links.setMsg("request ignored,you can't send multiple request within 30 mins");
        return (JSONObject) JSON.toJSON(links);

    }

}

class RecipeLinks{
    private List<String> links;
    private String msg;
    public RecipeLinks(){}
    public List<String> getLinks() {
        return links;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }
}