package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.entity.User;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            result.setMsg("email exists");
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
    public JSONObject updateSelf(User user) {
        CommonResult result=new CommonResult();
        User user2=userDao.findByEmail(user.getEmail());
        user.setEmail(user2.getEmail());
        if(user.checkUpdateInfo())
        {
            Date date =new Date();
            user.setAccount_updated(date);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
