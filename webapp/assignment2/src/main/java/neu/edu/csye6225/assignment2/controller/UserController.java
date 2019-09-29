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

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "v1/user/self",method= RequestMethod.GET)
    public JSONObject findByAccountAndPassword(HttpServletRequest request, HttpServletResponse response)
    {
        CommonResult result=new CommonResult();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findByEmail(auth.getName());
        result.setData(userRepository);
        if(userRepository !=null)
            return (JSONObject)JSON.toJSON(result);
        return null;

    }

    @RequestMapping(value="v1/user",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveUser(@RequestBody UserRepository request)
    {
        CommonResult result=new CommonResult();
        try{

           return  userService.save(request);
        }
        catch(Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("failure");
            return (JSONObject)JSON.toJSON(result);
        }
    }
    //only permit update firstname,lastname,password
    @RequestMapping(value="v1/user/self",method = RequestMethod.PUT)
    public JSONObject updateSelf(@RequestBody UserRepository request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findByEmail(auth.getName());
        CommonResult result=new CommonResult();
            try{
                return userService.updateSelf(request, userRepository);
            }catch (Exception e)
            {
                e.printStackTrace();
                result.setState(500);
                result.setMsg("failure");
                return (JSONObject)JSON.toJSON(result);
            }
    }

}
