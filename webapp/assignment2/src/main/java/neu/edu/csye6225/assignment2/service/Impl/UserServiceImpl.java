package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.entity.User;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserDao userDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public JSONObject findByAccountAndPassword(User user) {
        CommonResult result=new CommonResult();
        User user2=userDao.findByEmail(user.getEmail());
        if(user2==null||!bCryptPasswordEncoder.matches(user.getPassword(),user2.getPassword()))
        {
            result.setState(400);
            result.setMsg("user not exists or password is not correct");
            return (JSONObject)JSON.toJSON(result);
        }
        result.setData(user2);
        return (JSONObject)JSON.toJSON(result);
    }
    @Override
    public JSONObject save(User user)
    {
        CommonResult result=new CommonResult();
        if(userDao.findByEmail(user.getEmail())!=null) {
            result.setState(400);
            result.setMsg("Bad Request,Account exists");
            return (JSONObject)JSON.toJSON(result);
        }
        if(!user.getEmail().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"))
        {
            result.setState(400);
            result.setMsg("Bad Request,check your email format");
            return (JSONObject)JSON.toJSON(result);
        }
        // At least 8 length and no more than 16 length, include number,uppercase lowercase,and special character
        if(!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}"))
        {
            result.setState(400);
            result.setMsg("Bad Request,please follow password requirement");
            return (JSONObject)JSON.toJSON(result);
        }
        Date date =new Date();
        user.setAccount_created(date);
        user.setAccount_updated(date);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
        result.setData(user);
        return (JSONObject)JSON.toJSON(result);
    }

    @Override
    public JSONObject updateSelf(User request,User user) {
        CommonResult result=new CommonResult();

        if(request.checkUpdateInfo())
        {
            Date date =new Date();
            user.setAccount_updated(date);

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setFirst_name(request.getFirst_name());
            user.setLast_name(request.getLast_name());
            userDao.save(user);
            result.setData(user);
            return (JSONObject)JSON.toJSON(result);
        }
        else{
            result.setState(401);
            result.setMsg("Bad request");
            return (JSONObject)JSON.toJSON(result);
        }

    }

}
