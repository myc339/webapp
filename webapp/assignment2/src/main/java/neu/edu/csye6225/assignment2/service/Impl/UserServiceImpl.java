package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserDao userDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    @Autowired
    public UserServiceImpl(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }
    @Override
    public JSONObject findByAccountAndPassword(UserRepository userRepository, HttpServletResponse response) {
        CommonResult result=new CommonResult();
        UserRepository userRepository2 =userDao.findByEmail(userRepository.getEmail());
        if(userRepository2 ==null||!bCryptPasswordEncoder.matches(userRepository.getPassword(), userRepository2.getPassword()))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setState(400);
            result.setMsg("user not exists or password is not correct");
            return (JSONObject)JSON.toJSON(result);
        }
        result.setData(userRepository2);
        return (JSONObject)JSON.toJSON(result);
    }
    @Override
    public JSONObject save(UserRepository userRepository,HttpServletResponse response)
    {
        CommonResult result=new CommonResult();
        if(userDao.findByEmail(userRepository.getEmail())!=null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setState(400);
            result.setMsg("Bad Request,Account exists");
            return (JSONObject)JSON.toJSON(result);
        }
        if(!userRepository.getEmail().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setState(400);
            result.setMsg("Bad Request,check your email format");
            return (JSONObject)JSON.toJSON(result);
        }
        // At least 8 length and no more than 16 length, include number,uppercase lowercase,and special character
        if(!userRepository.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}"))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setState(400);
            result.setMsg("Bad Request,please follow password requirement");
            return (JSONObject)JSON.toJSON(result);
        }
        Date date =new Date();
        userRepository.setAccount_created(date);
        userRepository.setAccount_updated(date);
        userRepository.setId(UUID.randomUUID().toString());
        userRepository.setPassword(bCryptPasswordEncoder.encode(userRepository.getPassword()));
        userDao.save(userRepository);
        inMemoryUserDetailsManager.createUser(User.withUsername(userRepository.getEmail()).password(userRepository.getPassword()).roles("USER").build());
        result.setData(userRepository);
        return (JSONObject)JSON.toJSON(result);
    }

    @Override
    public JSONObject updateSelf(UserRepository request, UserRepository userRepository,HttpServletResponse response) {
        CommonResult result=new CommonResult();
        if(request.checkUpdateInfo())
        {
            Date date =new Date();
            userRepository.setAccount_updated(date);
            if(!userRepository.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}"))
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.setState(400);
                result.setMsg("Bad Request,please follow password requirement");
                return (JSONObject)JSON.toJSON(result);
            }
            userRepository.setPassword(bCryptPasswordEncoder.encode(userRepository.getPassword()));
            userRepository.setFirst_name(request.getFirst_name());
            userRepository.setLast_name(request.getLast_name());
            inMemoryUserDetailsManager.createUser(User.withUsername(userRepository.getEmail()).password(userRepository.getPassword()).roles("USER").build());
            userDao.save(userRepository);
            result.setData(userRepository);
            return (JSONObject)JSON.toJSON(result);
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setState(400);
            result.setMsg("Bad request");
            return (JSONObject)JSON.toJSON(result);
        }

    }

}
