package neu.edu.csye6225.assignment2;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.User;
import neu.edu.csye6225.assignment2.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc

public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
//    @MockBean
//    private UserService userService;
    @Test
    @Transactional
    @Rollback(true)
    public void Test_insert_User() throws Exception {
        User u=new User();
        u.setEmail("testchen12@mail.com");
        u.setPassword("132$Abc23");
        u.setFirst_name("joe");
        u.setLast_name("joycon");
        ObjectMapper objectMapper=new ObjectMapper();
//        given(userService.save(u))
        MvcResult mvcResult= mvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u))).andReturn();
        Assert.isTrue(String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("state")).equals("200"),"Register success");

        }


}


