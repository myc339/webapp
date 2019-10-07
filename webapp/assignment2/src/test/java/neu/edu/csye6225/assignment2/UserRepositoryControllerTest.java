package neu.edu.csye6225.assignment2;

import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc
//@Rollback(true)
//@Transactional
public class UserRepositoryControllerTest {
    @Autowired
    private MockMvc mvc;
    final UserRepository u=new UserRepository("testcase@email.com","132$Abc23","test","admin");
    @Test
    public void Test_insert_User1() throws Exception {
        u.setEmail_address("test@email.com");
        u.setPassword("1111Test!!");
        ObjectMapper objectMapper=new ObjectMapper();
        mvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u))).andExpect(status().isCreated());
    }
    @Test
    public void Test_insert_User2() throws Exception {
        u.setEmail_address("test1@email.com");
        u.setPassword("1111Test!!");
        ObjectMapper objectMapper=new ObjectMapper();
        mvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u))).andExpect(status().isCreated());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_User() throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isCreated());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_ExistEmail() throws Exception {
        u.setEmail_address("test@email.com");
        ObjectMapper objectMapper=new ObjectMapper();
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }

    // password length <8
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWithWrongPassword1() throws Exception {
        u.setPassword("1234");
        ObjectMapper objectMapper=new ObjectMapper();
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    //password with simple char
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWithWrongPassword2() throws Exception {
        u.setPassword("aabbcc121343");
        ObjectMapper objectMapper=new ObjectMapper();
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    //complex password and length is more than 16
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWithWrongPassword3() throws Exception {
        u.setPassword("132$Abc23132$Abc23132$Abc23132$Abc23");
        ObjectMapper objectMapper=new ObjectMapper();
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    //complex password
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWittIllegalEmail() throws Exception {
        u.setEmail_address("testmail.com");
        ObjectMapper objectMapper=new ObjectMapper();
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isBadRequest());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void Test_Update_User() throws Exception {
        UserRepository u=new UserRepository();
        u.setPassword("2222Test!!!");
        u.setFirst_name("joe_change");
        u.setLast_name("joycon_change");
        ObjectMapper objectMapper=new ObjectMapper();
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        this.mvc.perform(get("/v1/user/self").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(u))).
                andExpect(status().isOk());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void Test_Update_UserEmail() throws Exception {
        u.setEmail_address("test@exm.com");
        ObjectMapper objectMapper=new ObjectMapper();
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        this.mvc.perform(put("/v1/user/self").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(u))).
                andExpect(status().isBadRequest());
    }
    @Test
//    @Transactional
//    @Rollback(true)
    public void Test_Get_User() throws Exception {
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        this.mvc.perform(get("/v1/user/self").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

}




