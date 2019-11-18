package neu.edu.csye6225.assignment2.service;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RecipeService {

    JSONObject save(RecipeRepository recipeRepository, HttpServletResponse response);
    JSONObject updateRecipe(RecipeRepository request, String recipeId,HttpServletResponse response);
    JSONObject deleteRecipe(String id,HttpServletResponse response);
    JSONObject getRecipe(String id, HttpServletResponse response);
    JSONObject getNewestRecipe(HttpServletResponse response);
    JSONObject getRecipeLinks(HttpServletRequest request, HttpServletResponse response);
    JSONObject handSES_Bounces(HttpServletRequest request, HttpServletResponse response);
}
