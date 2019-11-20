package neu.edu.csye6225.assignment2.auth;

import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class BasicAuthentication extends BasicAuthenticationEntryPoint {
    @Autowired
    private UserDao userDao;
    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)throws IOException, ServletException {
        List<UserRepository> list = userDao.findAll();
        for(UserRepository userRepo:list) {
            if(!inMemoryUserDetailsManager.userExists(userRepo.getEmail_address()))
                inMemoryUserDetailsManager.createUser(User.withUsername(userRepo.getEmail_address()).password(userRepo.getPassword()).roles("USER").build());
        }

        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"password or email wrong");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("user");
        super.afterPropertiesSet();
    }
}
