package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.UserRepository;

import javax.servlet.http.HttpServletResponse;


public interface UserService {
    JSONObject findByAccountAndPassword(UserRepository userRepository, HttpServletResponse response);
    JSONObject save(UserRepository userRepository,HttpServletResponse response);
    JSONObject updateSelf(UserRepository request, UserRepository userRepository,HttpServletResponse response);
}
