package neu.edu.csye6225.assignment2;

import com.alibaba.fastjson.JSON;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_User() throws Exception {
        UserRepository u=new UserRepository();
        u.setEmail_address("test1@email.com");
        u.setPassword("132$Abc23");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
        MvcResult mvcResult=
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isOk()).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("200"),"Register success");
    }
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_ExistEmail() throws Exception {
        UserRepository u=new UserRepository();
        u.setEmail_address("test@email.com");
        u.setPassword("132$Abc23");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
        MvcResult mvcResult=
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isOk()).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("400"),"Email exist");
    }

    // password length <8
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWithWrongPassword1() throws Exception {
        UserRepository u=new UserRepository();
        u.setEmail_address("testpassword@mail.com");
        u.setPassword("1234");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
        MvcResult mvcResult=
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isOk()).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("400"),"Password length <8");
    }
    //password with simple char
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWithWrongPassword2() throws Exception {
        UserRepository u=new UserRepository();
        u.setEmail_address("testpassword@mail.com");
        u.setPassword("aabbcc121343");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
        MvcResult mvcResult=
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isOk()).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("400"),"passwords are simple char combined");
    }
    //complex password and length is more than 16
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWithWrongPassword3() throws Exception {
        UserRepository u=new UserRepository();
        u.setEmail_address("test1@email.com");
        u.setPassword("132$Abc23132$Abc23132$Abc23132$Abc23");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
        MvcResult mvcResult=
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isOk()).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("400"),"password too complex");
    }
    //complex password
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_UserWittIllegalEmail() throws Exception {
        UserRepository u=new UserRepository();
        u.setEmail_address("testmail.com");
        u.setPassword("1234%sdA42");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
        MvcResult mvcResult=
                mvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u))).andExpect(status().isOk()).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("400"),"Email invalid");
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
        UserRepository u=new UserRepository();
        u.setEmail_address("test@exm.com");
        ObjectMapper objectMapper=new ObjectMapper();
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        MvcResult mvcResult= this.mvc.perform(put("/v1/user/self").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(u))).
                andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("400"),"Bad request");

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




