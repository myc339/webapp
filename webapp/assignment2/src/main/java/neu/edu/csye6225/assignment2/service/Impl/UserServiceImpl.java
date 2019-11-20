package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl  implements UserService {
    //    private final JSONObject jsonObject=new JSONObject(true);
    @Autowired
    private UserDao userDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private static StatsDClient statsd;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    public UserServiceImpl(InMemoryUserDetailsManager inMemoryUserDetailsManager,StatsDClient statsDClient) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.statsd=statsDClient;

    }
    @Override
    public JSONObject save(UserRepository userRepository,HttpServletResponse response)
    {
        long startTime=System.currentTimeMillis();

        statsd.incrementCounter("count.post_user_times");
        if(userDao.findQuery(userRepository.getEmail_address())!=null) {
            try {

                if (!this.inMemoryUserDetailsManager.userExists(userRepository.getEmail_address())) {
                    log.info("user not exist");
                    this.inMemoryUserDetailsManager.createUser(User.withUsername(userRepository.getEmail_address())
                            .password(userRepository.getPassword()).roles("USER").build());
                }
                log.info("user exist");
                log.error("email exists");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "email exists");
            } catch (IOException e) {
                e.printStackTrace();
            }
            statsd.recordExecutionTime("timer.post_user_fail", System.currentTimeMillis() - startTime);
            return null;
        }
        if(!userRepository.getEmail_address().matches("^[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"))
        {
            try {
                log.error("email format invalid");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "email format invalid");
            } catch (IOException e) {
                e.printStackTrace();
            }
            statsd.recordExecutionTime("timer.post_user_fail", System.currentTimeMillis() - startTime);
            return null;
        }
        // At least 8 length and no more than 16 length, include number,uppercase lowercase,and special character
        if(!userRepository.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}"))
        {
            try {
                log.error("password too weak!");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "password too weak!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            statsd.recordExecutionTime("timer.post_user_fail", System.currentTimeMillis() - startTime);
            return null;
        }

        Date date =new Date();
        userRepository.setAccount_created(date);
        userRepository.setAccount_updated(date);
        userRepository.setId(UUID.randomUUID().toString());
        userRepository.setPassword(bCryptPasswordEncoder.encode(userRepository.getPassword()));
        userDao.save(userRepository);
        inMemoryUserDetailsManager.createUser(User.withUsername(userRepository.getEmail_address()).password(userRepository.getPassword()).roles("USER").build());

        statsd.recordExecutionTime("timer.post_user_success", System.currentTimeMillis() - startTime);
        log.info("USER_CREATED");
        return (JSONObject)JSON.toJSON(userRepository);
    }

    @Override
    public JSONObject updateSelf(UserRepository request,HttpServletResponse response) {
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.put_user_times");
        List<UserRepository> list = userDao.findAll();
        if(!list.isEmpty() && list!=null) {
            for (UserRepository userRepo : list) {
                if (!this.inMemoryUserDetailsManager.userExists(userRepo.getEmail_address()))
                    this.inMemoryUserDetailsManager.createUser(User.withUsername(userRepo.getEmail_address()).password(userRepo.getPassword()).roles("USER").build());
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        if(request.checkUpdateInfo())
        {
            Date date =new Date();
            userRepository.setAccount_updated(date);
            if(!request.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}"))
            {
                try {
                    log.error("password too weak!");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "password too weak!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                statsd.recordExecutionTime("timer.put_user_fail", System.currentTimeMillis() - startTime);
                return null;
            }
            userRepository.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            userRepository.setFirst_name(request.getFirst_name());
            userRepository.setLast_name(request.getLast_name());
            inMemoryUserDetailsManager.updateUser(User.withUsername(userRepository.getEmail_address()).password(userRepository.getPassword()).roles("USER").build());
            userDao.save(userRepository);
            log.info("user info updated");
            statsd.recordExecutionTime("timer.put_user_success", System.currentTimeMillis() - startTime);
            return (JSONObject)JSON.toJSON(userRepository);
        }
        else{
            try {
                log.error("you can't update field besides first_name,last_name and password");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "you can't update field besides first_name,last_name and password");
            } catch (IOException e) {
                e.printStackTrace();
            }
            statsd.recordExecutionTime("timer.put_user_fail", System.currentTimeMillis() - startTime);

            return null;
        }

    }
    @Override
    public JSONObject getSelf(HttpServletRequest request){
        System.out.println(request.getHeader("Authorization"));
        List<UserRepository> list = userDao.findAll();
        if(!list.isEmpty() && list!=null) {
            for (UserRepository userRepo : list) {
                if (!this.inMemoryUserDetailsManager.userExists(userRepo.getEmail_address()))
                    this.inMemoryUserDetailsManager.createUser(User.withUsername(userRepo.getEmail_address()).password(userRepo.getPassword()).roles("USER").build());
            }
        }
        long startTime=System.currentTimeMillis();
        statsd.incrementCounter("count.get_user_times");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        if(userRepository !=null)
        {
            statsd.recordExecutionTime("timer.get_user_success", System.currentTimeMillis() - startTime);
            log.info("get_user_success");
            log.info( request.getHeader("Authorization"));
            return (JSONObject)JSON.toJSON(userRepository);

        }
        else
            log.error("not found  user");
        statsd.recordExecutionTime("timer.get_user_fail", System.currentTimeMillis() - startTime);
        return (JSONObject) JSON.toJSON(request.getHeader("Authorization"));
    }

}