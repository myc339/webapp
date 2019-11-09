package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import com.timgroup.statsd.StatsDClient;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface AmazonS3ClientService {
        JSONObject uploadFileToS3Bucket(String recipeId,MultipartFile[] files, boolean enablePublicReadAccess,  HttpServletResponse response);
        JSONObject deleteFileFromS3Bucket(String recipeId, String imageId,HttpServletResponse response);
        JSONObject updateRecipeImage(String recipeId,String imageId,MultipartFile files,HttpServletResponse response);
        JSONObject getRecipeImage(String recipeId,String imageId,HttpServletResponse response);
}
