package neu.edu.csye6225.assignment2;

import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.csye6225.assignment2.entity.NutritionInformationRepository;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
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

        // create ingredients list
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("4 ounces linguine pasta");
        ingredients.add("2 boneless, skinless chicken breast halves, sliced into thin strips");
        ingredients.add("2 teaspoons Cajun seasoning");
        ingredients.add("2 tablespoons butter");

        // create order list repository
        OrderedListRepository ol = new OrderedListRepository();
        ol.setPosition(1);
        ol.setItems("some text here");
        ArrayList<OrderedListRepository> orderlist = new ArrayList<>();

        // create nutrition information
        NutritionInformationRepository n = new NutritionInformationRepository();
        n.setCalories(100);
        n.setCarbohydrates_in_grams(4);
        n.setSodium_in_mg(100);

        // set information in recipe
        r.setCook_time_in_min(15);
        r.setPrep_time_in_min(15);
        r.setTitle("Creamy Cajun Chicken Pasta");
        r.setCusine("Italian");
        r.setServings(2);
        r.setIngredients(ingredients);
        r.setSteps(orderlist);
        r.setNutrition_information(n);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mvc.perform(post("/v1/recipe")
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
        ObjectMapper objectMapper = new ObjectMapper();
        this.mvc.perform(put("/v1/recipe/{id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    @Transactional
    @Rollback(true)
    public void Test_Delete_Recipe() throws Exception {
        this.mvc.perform(delete("/v1/recipe/{id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
