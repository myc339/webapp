package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.helper.MetricsConfig;
import neu.edu.csye6225.assignment2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public long getDuration(long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private  final StatsDClient statsd = new NonBlockingStatsDClient("","localhost",8125);

    @RequestMapping(value = "v1/user/self",method= RequestMethod.GET)
    public JSONObject findByAccountAndPassword(HttpServletRequest request, HttpServletResponse response)
    {

        statsd.incrementCounter("endpoint.http.user.get");
        statsd.count("endpoint.user.get",1);
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        statsd.recordExecutionTime("endpoint.http.user.get.queryTime", getDuration(startTime));
        if(userRepository !=null)
        {
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject tmp = (JSONObject)JSON.toJSON(userRepository);
            statsd.recordExecutionTime("endpoint.http.user.get.executeTime", getDuration(startTime));
            return tmp;
        }
        statsd.recordExecutionTime("endpoint.http.user.get.executeTime", getDuration(startTime));
        return null;

    }

    @RequestMapping(value="v1/user",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveUser(@RequestBody UserRepository request,HttpServletResponse response)
    {
        statsd.incrementCounter("endpoint.http.user.post");
        long startTime = System.currentTimeMillis();
        try{
            response.setStatus(HttpServletResponse.SC_CREATED);
            JSONObject tmp = userService.save(request,response);
            statsd.recordExecutionTime("endpoint.http.user.post.executeTime", getDuration(startTime));
            return tmp;
        }
        catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            statsd.recordExecutionTime("endpoint.http.user.post.executeTime", getDuration(startTime));
            return null;
        }
    }
    //only permit update firstname,lastname,password
    @RequestMapping(value="v1/user/self",method = RequestMethod.PUT)
    public JSONObject updateSelf(@RequestBody UserRepository request,HttpServletResponse response){
        statsd.incrementCounter("endpoint.http.user.put");
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        statsd.recordExecutionTime("endpoint.http.user.put.queryTime", getDuration(startTime));
            try{
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                JSONObject tmp = userService.updateSelf(request, userRepository,response);
                statsd.recordExecutionTime("endpoint.http.user.put.executeTime", getDuration(startTime));
                return tmp;
            }catch (Exception e)
            {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                statsd.recordExecutionTime("endpoint.http.user.put.executeTime", getDuration(startTime));
                return null;
            }
    }

}
