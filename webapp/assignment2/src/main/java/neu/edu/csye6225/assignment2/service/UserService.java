package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.UserRepository;


public interface UserService {
    JSONObject findByAccountAndPassword(UserRepository userRepository);
    JSONObject save(UserRepository userRepository);
    JSONObject updateSelf(UserRepository request, UserRepository userRepository);
}
