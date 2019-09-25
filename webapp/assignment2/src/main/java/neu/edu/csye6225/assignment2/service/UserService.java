package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.User;


public interface UserService {
    JSONObject findByAccountAndPassword(User user);
    JSONObject save(User user);
    JSONObject updateSelf(User request, User user);
}
