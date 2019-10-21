package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface AmazonS3ClientService {
        JSONObject uploadFileToS3Bucket(MultipartFile files, boolean enablePublicReadAccess, String id, HttpServletResponse response);
        JSONObject deleteFileFromS3Bucket(String id, String imageId,HttpServletResponse response);
}
