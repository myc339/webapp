package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.service.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController

public class FileHandlerController {
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;
    @Autowired
    private UserDao userDao;
    @Async
    @RequestMapping(value="/v1/recipe/{id}/image",method=RequestMethod.POST)
    public JSONObject attachRecipeImage(@PathVariable String id, @RequestPart(value = "image") MultipartFile[] file, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return this.amazonS3ClientService.uploadFileToS3Bucket(id,userRepository.getId(),file, true,response);
    }
    @Async
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.GET)
    public JSONObject getRecipeImage(@PathVariable String id,@PathVariable String imageId,HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
       return  this.amazonS3ClientService.getRecipeImage(id,imageId,response);
    }
    @Async
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipeImage(@PathVariable String id,@PathVariable String imageId,HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return  this.amazonS3ClientService.deleteFileFromS3Bucket(id,userRepository.getId(),imageId,response);
    }
    @Async
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.PUT)
    public JSONObject updateRecipeImage(@PathVariable String id,@PathVariable String imageId,@RequestPart(value = "image") MultipartFile file,HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        return  this.amazonS3ClientService.updateRecipeImage(id,userRepository.getId(),imageId,file,response);
    }
}
