package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import neu.edu.csye6225.assignment2.service.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController

public class FileHandlerController {
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;

    @RequestMapping(value="/v1/recipe/{id}/image",method=RequestMethod.POST)
    public JSONObject attachRecipeImage(@PathVariable String id, @RequestPart(value = "file") MultipartFile file, HttpServletResponse response)
    {
        return this.amazonS3ClientService.uploadFileToS3Bucket(file, true,id,response);
    }
    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.GET)
    public JSONObject getRecipeImage(@PathVariable String id,@PathVariable String imageid,HttpServletResponse response)
    {
       return  this.amazonS3ClientService.deleteFileFromS3Bucket(id,imageid,response);
    }

    @RequestMapping(value="v1/recipe/{id}/image/{imageId}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipeImage(@PathVariable String id,@PathVariable String imageid,HttpServletResponse response)
    {
        return  this.amazonS3ClientService.deleteFileFromS3Bucket(id,imageid,response);
    }
}
