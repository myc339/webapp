package neu.edu.csye6225.assignment2.security;

import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;


    @Override
    protected  void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/v1/user").permitAll()
                .antMatchers(HttpMethod.GET,"/v1/user/self").authenticated()
                .antMatchers(HttpMethod.PUT,"/v1/user/self").authenticated()
                .antMatchers(HttpMethod.POST,"/v1/recipe").authenticated()
                .antMatchers(HttpMethod.GET,"/v1/recipe/{id}").permitAll()
                .antMatchers(HttpMethod.PUT,"/v1/recipe/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/v1/recipe/{id}").authenticated()
                .antMatchers(HttpMethod.GET,"/**").permitAll()
                .antMatchers(HttpMethod.POST,"/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/**").permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable();

        // 使用authenticationEntryPoint验证 user/password
        http.httpBasic().authenticationEntryPoint(authEntryPoint);

        // 把session禁掉
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(inMemoryUserDetailsManager());
    }
    @Autowired
    private UserDao userDao;
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        List<UserDetails> usersList=new ArrayList<>();
        //        users.put("user","pass,ROLE_USER,enabled");
        //add whatever other user you need
        List<UserRepository> list = userDao.findAll();
        for(UserRepository u: list){
            UserDetails userDetails = User.withUsername(u.getEmail_address()).password(u.getPassword()).roles("USER").build();
            usersList.add(userDetails);
        }
        return new InMemoryUserDetailsManager(usersList);
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        //这里访问数据库
//        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> mngConfig = auth.inMemoryAuthentication();
//        List<UserRepository> list = userDao.findAll();
//        for(UserRepository u: list){
//            UserDetails userDetails = User.withUsername(u.getEmail()).password(u.getPassword()).roles("USER").build();
//            mngConfig.withUser(userDetails);
//        }
//    }

}
