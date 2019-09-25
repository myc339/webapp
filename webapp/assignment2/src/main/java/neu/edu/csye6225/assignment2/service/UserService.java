package neu.edu.csye6225.assignment2.service;

import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.entity.User;

import java.util.Optional;


public interface UserService {
    CommonResult findByAccountAndPassword(User user);
    String save(User user);
    CommonResult updateSelf(User user);
    Optional findByToken(String token);
}
