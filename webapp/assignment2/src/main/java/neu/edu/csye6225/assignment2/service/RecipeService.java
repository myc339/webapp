package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;

import javax.servlet.http.HttpServletResponse;

public interface RecipeService {

    JSONObject save(RecipeRepository recipeRepository,String authorId, HttpServletResponse response);
    JSONObject updateRecipe(RecipeRepository request, String authorId, String recipeId,HttpServletResponse response);
    JSONObject deleteRecipe(String id, String authorId,HttpServletResponse response);
    JSONObject getRecipe(String id);
}
