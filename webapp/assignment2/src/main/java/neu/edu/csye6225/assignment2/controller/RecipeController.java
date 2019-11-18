package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3Client;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import neu.edu.csye6225.assignment2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController

public class RecipeController {

    @Autowired
    private RecipeService recipeService;


    @RequestMapping(value="v1/recipe",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveRecipe( @RequestBody RecipeRepository requestBody, HttpServletResponse response)
    {
        try {
            response.setStatus(HttpServletResponse.SC_CREATED);
            return recipeService.save(requestBody,response);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.PUT)
    public JSONObject updateRecipe(@RequestBody RecipeRepository requestBody, HttpServletRequest request, HttpServletResponse response, @PathVariable String id){


        try {
            response.setStatus(HttpServletResponse.SC_OK);
            return recipeService.updateRecipe(requestBody, id, response);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipe(@PathVariable String id,HttpServletResponse response){

        try{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return recipeService.deleteRecipe(id,response);
        }catch (Exception e)
        {
            e.printStackTrace();
            return  null;
        }
    }

    @RequestMapping(value = "v1/recipe/{id}",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
        try{
            response.setStatus(HttpServletResponse.SC_OK);
            return recipeService.getRecipe(id, response);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value = "v1/recipes",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_OK);
            return recipeService.getNewestRecipe(response);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value = "v1/myrecipes",method= RequestMethod.GET)
    public JSONObject getRecipeLinks(HttpServletRequest request, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_OK);
            return recipeService.getRecipeLinks(request,response);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value = "v1/sns/handle-bounces",method = RequestMethod.POST)
    public JSONObject handle_ses_bounces(HttpServletRequest request,HttpServletResponse response)
    {
        try{

            return recipeService.handSES_Bounces(request,response);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
