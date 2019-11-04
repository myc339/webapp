package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
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
    private UserDao userDao;

    @Autowired
    private UserService userService;

    private static final StatsDClient statsd = new NonBlockingStatsDClient("my.prefix", "statsd-host", 8125);

    @RequestMapping(value = "v1/user/self",method= RequestMethod.GET)
    public JSONObject findByAccountAndPassword(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        if(userRepository !=null)
        {
            response.setStatus(HttpServletResponse.SC_OK);
            statsd.incrementCounter("user.find");
            return (JSONObject)JSON.toJSON(userRepository);
        }

        return null;

    }

    @RequestMapping(value="v1/user",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveUser(@RequestBody UserRepository request,HttpServletResponse response)
    {
        try{
            response.setStatus(HttpServletResponse.SC_CREATED);
            statsd.incrementCounter("user.save");
            return  userService.save(request,response);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
            try{
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                statsd.incrementCounter("user.update");
                return userService.updateSelf(request, userRepository,response);
            }catch (Exception e)
            {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
    }

}
