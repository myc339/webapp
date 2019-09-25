package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSON;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.entity.User;
import neu.edu.csye6225.assignment2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping(value = "/auth",method= RequestMethod.GET)
    public CommonResult findByAccountAndPassword(@RequestBody  User request)
    {
        CommonResult result=new CommonResult();
        try{
            return userService.findByAccountAndPassword(request);
        }catch(Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("failure");
            return result;
        }
    }
    @RequestMapping(value="/register",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public String SaveUser(@RequestBody User request)
    {
        CommonResult result=new CommonResult();
        try{

           return  userService.save(request);
        }
        catch(Exception e){
            e.printStackTrace();
            result.setState(500);
            result.setMsg("failure");
            return JSON.toJSONString(result);
        }
    }
    //only permit update firstname,lastname,password
    @RequestMapping(value="/update",method = RequestMethod.PUT)
    public CommonResult updateSelf(@RequestBody User request){
        CommonResult result=new CommonResult();
            try{
                return userService.updateSelf(request);
            }catch (Exception e)
            {
                e.printStackTrace();
                result.setState(500);
                result.setMsg("failure");
                return result;
            }
    }

}
