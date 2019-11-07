package neu.edu.csye6225.assignment2;

import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.UserRepository;

import org.apache.commons.codec.binary.Base64;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class UserRepositoryControllerTest {
    @Autowired
    private MockMvc mvc;

    private static String email;
    private static UserRepository u;
    private static String password;
    private static String basicDigestHeaderValue ;
    private ObjectMapper objectMapper=new ObjectMapper();
    @BeforeClass
    public static void init()
    {

        password="1111Test!!";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        email=timestamp.getTime()/10+"@email.com";
        basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64((email+":"+password).getBytes()));
        u =new UserRepository(email,password,"test","admin");
    }
    @After
    public void reset()
    {
        u.setEmail_address(email);
        u.setPassword(password);
    }
    @Test
    public void Test_insert_User() throws Exception {
        mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isCreated());
    }
    @Test
    public void Test_insert_ExistEmail() throws Exception {
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }

    // password length <8
    @Test
    public void Test_insert_UserWithWrongPassword1() throws Exception {
        u.setPassword("1234");
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    //password with simple char
    @Test
    public void Test_insert_UserWithWrongPassword2() throws Exception {
        u.setPassword("aabbcc121343");
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    //complex password and length is more than 16
    @Test
    public void Test_insert_UserWithWrongPassword3() throws Exception {
        u.setPassword("132$Abc23132$Abc23132$Abc23132$Abc23");
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    //illegal Email format
    @Test
    public void Test_insert_UserWittIllegalEmail() throws Exception {
        u.setEmail_address("testmail.com");
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    @Test
    public void Test_Get_User() throws Exception {
        this.mvc.perform(get("/v1/user/self").header("Authorization", basicDigestHeaderValue)
        .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }
    @Test
    public void Test_Update_UserEmail() throws Exception {
        u.setEmail_address("test@exm.com");
        this.mvc.perform(put("/v1/user/self").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(u))).
                andExpect(status().isBadRequest());
    }
    @Test
    @Transactional
    public void Test_Update_User() throws Exception {
         UserRepository user=new UserRepository();
        user.setPassword("2222Test!!!");
        user.setFirst_name("joe_change");
        user.setLast_name("joycon_change");

        this.mvc.perform(put("/v1/user/self").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).
                andExpect(status().isNoContent());
    }



}




