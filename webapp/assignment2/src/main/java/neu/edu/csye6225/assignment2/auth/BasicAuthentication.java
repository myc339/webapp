package neu.edu.csye6225.assignment2.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.service.Impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class BasicAuthentication extends BasicAuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(BasicAuthentication.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            log.error("password or email wrong");
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
