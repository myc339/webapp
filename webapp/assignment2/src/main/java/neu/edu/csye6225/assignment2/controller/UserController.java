package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "v1/user/self",method= RequestMethod.GET)
    public JSONObject findByAccountAndPassword(HttpServletRequest request, HttpServletResponse response)
    {


        try {
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.getSelf();
        }catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

    }

    @RequestMapping(value="v1/user",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveUser(@RequestBody UserRepository request,HttpServletResponse response)
    {
        try{
            response.setStatus(HttpServletResponse.SC_CREATED);
            return userService.save(request,response);
        }
        catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }
    //only permit update firstname,lastname,password
    @RequestMapping(value="v1/user/self",method = RequestMethod.PUT)
    public JSONObject updateSelf(@RequestBody UserRepository request,HttpServletResponse response){

        try{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return userService.updateSelf(request,response);
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

}
