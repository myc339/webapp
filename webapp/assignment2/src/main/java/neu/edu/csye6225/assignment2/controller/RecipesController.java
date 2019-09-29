package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController

public class RecipesController {

    @RequestMapping(value="v1/recipe",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveRecipe(@RequestBody UserRepository request)
    {
        return null;
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.PUT)
    public JSONObject updateRecipe(@RequestBody UserRepository request, @PathVariable int id){
        return null;
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipe(@RequestBody UserRepository request, @PathVariable int id){
        return null;
    }

    @RequestMapping(value = "v1/recipe/{id}",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response, @PathVariable int id)
    {
        return null;
    }
}
