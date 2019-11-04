package neu.edu.csye6225.assignment2.controller;

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

import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

@RestController

public class RecipeController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RecipeService recipeService;

    private static final StatsDClient statsd = new NonBlockingStatsDClient("my.prefix", "localhost", 8125);

    @RequestMapping(value="v1/recipe",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveRecipe( @RequestBody RecipeRepository requestBody, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_CREATED);
        statsd.incrementCounter("recipe.save");
        return recipeService.save(requestBody,userRepository.getId(),response);
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.PUT)
    public JSONObject updateRecipe(@RequestBody RecipeRepository requestBody, HttpServletRequest request, HttpServletResponse response, @PathVariable String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_OK);
        statsd.incrementCounter("recipe.update");
        return recipeService.updateRecipe(requestBody, userRepository.getId(), id,response);
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipe(@PathVariable String id,HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        statsd.incrementCounter("recipe.delete");
        return recipeService.deleteRecipe(id, userRepository.getId(),response);
    }

    @RequestMapping(value = "v1/recipe/{id}",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
        response.setStatus(HttpServletResponse.SC_OK);
        statsd.incrementCounter("recipe.find");
        return recipeService.getRecipe(id, response);
    }
    @RequestMapping(value = "v1/recipes",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        statsd.incrementCounter("recipe.find");
        return recipeService.getNewestRecipe(response);
    }


}
