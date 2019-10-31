package neu.edu.csye6225.assignment2;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.NutritionInformationRepository;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.JVM)
public class FileHandlerControllerTest {
    @Autowired
    private MockMvc mvc;
    private static String email1;
    private static UserRepository u1;
    private static String password;
    private static String token1;
    private static NutritionInformationRepository nutritionInformationRepository;
    private static ArrayList<OrderedListRepository> steps;
    private static ArrayList<String> ingredients1;
    private static RecipeRepository recipeRepository;
    private static MockMultipartFile file;
    private static String recipe_id="";
    private static String image_id="";
    private ObjectMapper objectMapper=new ObjectMapper();
    @BeforeClass
    public static void init()
    {
        password ="1111Test!!";
        email1 = UUID.randomUUID().toString()+"@email.com";
        token1 = "Basic " + new String(Base64.encodeBase64((email1+":"+password).getBytes()));
        u1 = new UserRepository(email1,password,"test","admin");
        nutritionInformationRepository=
                new NutritionInformationRepository(100,4,100, 53.7f,53.7f);
        steps=new ArrayList<>(Arrays.asList(new OrderedListRepository(1,"some text here"),
                new OrderedListRepository(2,"some text here")));
        ingredients1=new ArrayList<>(Arrays.asList("4 ounces linguine pasta",
                "2 boneless, skinless chicken breast halves, sliced into thin strips",
                "2 teaspoons Cajun seasoning",
                "2 tablespoons butter"));
        recipeRepository=new RecipeRepository(15,15,"Creamy Cajun Chicken Pasta",
                "Italian",2,ingredients1,nutritionInformationRepository,steps);
        file = new MockMultipartFile(
                    "test.png",
                    "test.png".getBytes());
    }
    @Test
    public void AddUsers() throws Exception {
        mvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u1))).andExpect(status().isCreated());
    }
    @Test
    public void insert_Recipe_for_Test() throws Exception {
        MvcResult mvcResult=this.mvc.perform(post("/v1/recipe").header("Authorization", token1).accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipeRepository))).
                andExpect(status().isCreated()).andReturn();

        recipe_id=String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("id"));
    }
    @Test
    public void Test_Attach_Recipe_Image() throws Exception {
        MvcResult mvcResult=this.mvc.perform(multipart("/v1/recipe/"+recipe_id+"/image")
                .file(file)
                .header("Authorization", token1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        image_id=String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("id"));
    }
    @Test
    public void Test_Get_Recipe_Image() throws Exception {
        this.mvc.perform(get("/v1/recipe/"+recipe_id+"/image/{imageId}", image_id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void Test_Delete_Recipe() throws Exception {
        this.mvc.perform(delete("/v1/recipe/"+recipe_id+"/image/{imageId}", image_id)
                .header("Authorization", token1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
