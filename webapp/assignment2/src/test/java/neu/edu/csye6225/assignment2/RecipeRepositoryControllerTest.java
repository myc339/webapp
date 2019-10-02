package neu.edu.csye6225.assignment2;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc
public class RecipeRepositoryControllerTest {
    @Autowired
    private MockMvc mvc;
    @Test
    public void Test_Create_Recipe() throws Exception {
        RecipeRepository r = new RecipeRepository();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("4 ounces linguine pasta");
        ingredients.add("2 boneless, skinless chicken breast halves, sliced into thin strips");
        ingredients.add("2 teaspoons Cajun seasoning");
        ingredients.add("2 tablespoons butter");
        OrderedListRepository ol = new OrderedListRepository();
        ol.setPosition(1);
        ol.setItems("some text here");
        ArrayList<OrderedListRepository> orderlist = new ArrayList<>();
        r.setCook_time_in_min(15);
        r.setPrep_time_in_min(15);
        r.setTitle("Creamy Cajun Chicken Pasta");
        r.setCusine("Italian");
        r.setServings(2);
        r.setIngredients(ingredients);
        r.setSteps(orderlist);
        // nutrition_information ??
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mvc.perform(post("/v1/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andReturn();
        Assert.notNull(JSON.parseObject(mvcResult.getResponse().getContentAsString()), "Post Error");
    }
}
