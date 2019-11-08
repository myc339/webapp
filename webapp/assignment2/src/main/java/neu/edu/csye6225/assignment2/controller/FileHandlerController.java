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

import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

@RestController

public class FileHandlerController {
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;
    @Autowired
    private UserDao userDao;
    public long getDuration(long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static final StatsDClient statsd=new NonBlockingStatsDClient("ccwebapp.","locahost",8125);
    @Async
    @RequestMapping(value="/v1/recipe/{id}/image",method=RequestMethod.POST)
    public JSONObject attachRecipeImage(@PathVariable String id, @RequestPart(value = "image") MultipartFile[] file, HttpServletResponse response)
    {
        statsd.incrementCounter("endpoint.http.image.attach");
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
//        statsd.recordExecutionTime("endpoint.http.filehandler.post.queryTime", getDuration(startTime));
        response.setStatus(HttpServletResponse.SC_CREATED);
        long startTime2 = System.currentTimeMillis();
        JSONObject tmp = this.amazonS3ClientService.uploadFileToS3Bucket(id,userRepository.getId(),file, true,response);
//        statsd.recordExecutionTime("endpoint.http.filehandler.post.s3", getDuration(startTime2));
        statsd.recordExecutionTime("endpoint.http.filehandler.post.executeTime", getDuration(startTime));
        return tmp;
    }
    @Async
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.GET)
    public JSONObject getRecipeImage(@PathVariable String id,@PathVariable String imageId,HttpServletResponse response)
    {
        statsd.incrementCounter("endpoint.http.image.get");
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
//        statsd.recordExecutionTime("endpoint.http.filehandler.get.queryTime", getDuration(startTime));
        long startTime2 = System.currentTimeMillis();
        JSONObject tmp = this.amazonS3ClientService.getRecipeImage(id,imageId,response);
//        statsd.recordExecutionTime("endpoint.http.filehandler.get.s3", getDuration(startTime2));
        statsd.recordExecutionTime("endpoint.http.filehandler.get.executeTime", getDuration(startTime));
        return tmp;
    }
    @Async
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipeImage(@PathVariable String id,@PathVariable String imageId,HttpServletResponse response)
    {
        statsd.incrementCounter("endpoint.http.image.delete");
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
//        statsd.recordExecutionTime("endpoint.http.filehandler.delete.queryTime", getDuration(startTime));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        long startTime2 = System.currentTimeMillis();
        JSONObject tmp = this.amazonS3ClientService.deleteFileFromS3Bucket(id,userRepository.getId(),imageId,response);
//        statsd.recordExecutionTime("endpoint.http.filehandler.delete.s3", getDuration(startTime2));
        statsd.recordExecutionTime("endpoint.http.filehandler.delete.executeTime", getDuration(startTime));
        return tmp;
    }
    @Async
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.PUT)
    public JSONObject updateRecipeImage(@PathVariable String id,@PathVariable String imageId,@RequestPart(value = "image") MultipartFile file,HttpServletResponse response)
    {
        statsd.incrementCounter("endpoint.http.image.update");
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
//        statsd.recordExecutionTime("endpoint.http.filehandler.put.queryTime", getDuration(startTime));
        long startTime2 = System.currentTimeMillis();
        JSONObject tmp = this.amazonS3ClientService.updateRecipeImage(id,userRepository.getId(),imageId,file,response);
//        statsd.recordExecutionTime("endpoint.http.filehandler.put.s3", getDuration(startTime2));
        statsd.recordExecutionTime("endpoint.http.filehandler.put.executeTime", getDuration(startTime));
        return tmp;
    }
}
