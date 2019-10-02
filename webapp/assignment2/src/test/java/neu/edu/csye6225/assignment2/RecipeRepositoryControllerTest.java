package neu.edu.csye6225.assignment2;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes =Assignment2Application.class )
@AutoConfigureMockMvc
public class RecipeRepositoryControllerTest {
    @Autowired
    private MockMvc mvc;
    @Test
    @Transactional
    @Rollback(true)
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
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        this.mvc.perform(post("/v1/recipe")
                .header("Authorization", basicDigestHeaderValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andReturn();
    }


    @Test
    @Transactional
    @Rollback(true)
    public void Test_Get_Recipe() throws Exception {
        this.mvc.perform(get("/v1/recipe/{id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    @Rollback(true)
    public void Test_Update_Recipe() throws Exception {
        RecipeRepository r = new RecipeRepository();
        r.setCook_time_in_min(30);
        r.setPrep_time_in_min(20);
        // nutrition_information ??
        ObjectMapper objectMapper = new ObjectMapper();
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        this.mvc.perform(put("/v1/recipe/{id}")
                .header("Authorization", basicDigestHeaderValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    @Transactional
    @Rollback(true)
    public void Test_Delete_Recipe() throws Exception {
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("test@email.com:1111Test!!").getBytes()));
        this.mvc.perform(delete("/v1/recipe/{id}")
                .header("Authorization", basicDigestHeaderValue)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
