package neu.edu.csye6225.assignment2;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.NutritionInformationRepository;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@ActiveProfiles("test")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.JVM)
public class RecipeRepositoryControllerTest {
    @Autowired
    private MockMvc mvc;
    final NutritionInformationRepository nutritionInformationRepository=
            new NutritionInformationRepository(100,4,100, 53.7f,53.7f);
    ArrayList<OrderedListRepository> steps=new ArrayList<>(Arrays.asList(new OrderedListRepository(1,"some text here"),
            new OrderedListRepository(2,"some text here")));
    ArrayList<String> ingredients1=new ArrayList<>(Arrays.asList("4 ounces linguine pasta",
            "2 boneless, skinless chicken breast halves, sliced into thin strips",
            "2 teaspoons Cajun seasoning",
            "2 tablespoons butter"));
    final RecipeRepository recipeRepository=new RecipeRepository(15,15,"Creamy Cajun Chicken Pasta",
            "Italian",2,ingredients1,nutritionInformationRepository,steps);
    static String id="";

    private static String email1,email2;
    private static UserRepository u1,u2;
    private static String password;
    private static String token1 ;
    private static String token2 ;
    private ObjectMapper objectMapper=new ObjectMapper();
    @BeforeClass
    public static void init()
    {
        password="1111Test!!";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        email1="TESTRECIPE@email1.com";
        email2="RECIPE@email2.com";
        token1="Basic " + new String(Base64.encodeBase64((email1+":"+password).getBytes()));
        token2="Basic " + new String(Base64.encodeBase64((email2+":"+password).getBytes()));
        System.out.println("token1:"+token1);
        System.out.println("token2:"+token2);
        u1 =new UserRepository(email1,password,"test","admin");
        u2 =new UserRepository(email2,password,"test","admin");
    }
    @Test
    public void AddUsers1() throws Exception {
        mvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u1))).andExpect(status().isCreated());
    }
    @Test
    public void AddUsers2() throws Exception {
        mvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u2))).andExpect(status().isCreated());
    }
    @Test
    public void insert_Recipe_for_Test() throws Exception {
        MvcResult mvcResult=this.mvc.perform(post("/v1/recipe").header("Authorization", token1).accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipeRepository))).
                andExpect(status().isCreated()).andReturn();

        id=String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("id"));
    }

    @Test
    public void Test_Get_Recipe() throws Exception {
        this.mvc.perform(get("/v1/recipe/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void Test_Update_Recipe() throws Exception {
        recipeRepository.setCook_time_in_min(30);
        recipeRepository.setPrep_time_in_min(20);
        this.mvc.perform(put("/v1/recipe/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token1)
                .content(objectMapper.writeValueAsString(recipeRepository)))
                .andExpect(status().isOk());
    }

    //return 400
    @Test
    public void Test_Update_Not_permitted_Recipe_Field() throws Exception {
        recipeRepository.setAuthor(UUID.randomUUID().toString());
        this.mvc.perform(put("/v1/recipe/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token1)
                .content(objectMapper.writeValueAsString(recipeRepository)))
                .andExpect(status().isBadRequest());
    }
    //401 unauthorized
    @Test
    public void Test_Update_OthersRecipe() throws Exception {
        recipeRepository.setCook_time_in_min(30);
        recipeRepository.setPrep_time_in_min(20);
        this.mvc.perform(put("/v1/recipe/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token2)
                .content(objectMapper.writeValueAsString(recipeRepository)))
                .andExpect(status().isUnauthorized());
    }

    // delete yourself recipe
    @Test
    @Transactional
    public void Test_Delete_Recipe() throws Exception {
        this.mvc.perform(delete("/v1/recipe/{id}",id)
                .header("Authorization", token1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    //delete others recipe
    @Test
    public void Test_Delete_OthersRecipe() throws Exception {
        this.mvc.perform(delete("/v1/recipe/{id}",id)
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
