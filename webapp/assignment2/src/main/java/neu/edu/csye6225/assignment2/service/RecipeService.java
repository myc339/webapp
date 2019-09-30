package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;

public interface RecipeService {
    JSONObject save(RecipeRepository recipeRepository, String authorId);
    JSONObject updateRecipe(RecipeRepository request, String authorId, String recipeId);
}
