package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.entity.User;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserDao userDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public CommonResult findByAccountAndPassword(User user) {
        CommonResult result=new CommonResult();
        User user2=userDao.findByEmail(user.getEmail());
        if(user2==null)
        {
            result.setState(400);
            result.setMsg("user not exists");
            result.setData(user);
            return result;
        }
        if(!bCryptPasswordEncoder.matches(user.getPassword(),user2.getPassword()))
        {
            result.setState(400);
            result.setMsg("password is not correct");
            result.setData(user);
            return result;
        }
        String token= UUID.randomUUID().toString();
//        User user1=user1.get();
        result.setMsg("login succeed");
        Date date =new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
//        result.setToken(token+timestamp);
        return result;
    }
    @Override
    public String save(User user)
    {
        CommonResult result=new CommonResult();
        if(userDao.findByEmail(user.getEmail())!=null) {
            result.setState(400);
            user.setPassword("");
            result.setData(user);
            result.setMsg("email exists");
            return JSON.toJSONString(result);
        }
        Date date =new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        user.setAccount_created(timestamp);
        user.setAccount_updated(timestamp);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
        user.setPassword("");
        result.setData(user);
//      System.out.println(user.toString());
        return JSON.toJSONString(result);
    }
    @Override
    public Optional findByToken(String token)
    {
        String id=token.split(",")[0];
        Optional user=userDao.findById(id);
        if(user.isPresent()){
            return Optional.of(user);
        }
        return Optional.empty();
    }
    @Override
    public CommonResult updateSelf(User user) {
        CommonResult result=new CommonResult();
        User user2=userDao.findByEmail(user.getEmail());
        user.setEmail(user2.getEmail());
        if(user.checkUpdateInfo())
        {
            Date date =new Date();
            Timestamp timestamp=new Timestamp(date.getTime());
            user.setAccount_updated(timestamp);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDao.save(user);
            return result;
        }
        else{
            result.setState(401);
            result.setMsg("Bad request");
            return result;
        }

    }

}
