package neu.edu.csye6225.assignment2;


import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.NutritionInformationRepository;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
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
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc
public class RecipeRepositoryContronllerTest {
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
    @Test
    public void insert_Recipe_for_Test() throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        MvcResult mvcResult=this.mvc.perform(post("/v1/recipe").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipeRepository))).
                andExpect(status().isCreated()).andReturn();

        id=String.valueOf(JSON.parseObject(mvcResult.getResponse().getContentAsString()).get("id"));
    }
    @Test
    public void TestID(){
        System.out.println("id="+id);
    }
}
