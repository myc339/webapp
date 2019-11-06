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

    public long getDuration(long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static final StatsDClient statsd = new NonBlockingStatsDClient("my.prefix", "localhost", 8125);

    @RequestMapping(value="v1/recipe",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveRecipe( @RequestBody RecipeRepository requestBody, HttpServletResponse response)
    {
        statsd.incrementCounter("endpoint.http.recipe.post");
        long startTime = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        statsd.recordExecutionTime("endpoint.http.recipe.post.queryTime", getDuration(startTime));
        response.setStatus(HttpServletResponse.SC_CREATED);
        JSONObject tmp = recipeService.save(requestBody,userRepository.getId(),response);
        statsd.recordExecutionTime("endpoint.http.recipe.post.executeTime", getDuration(startTime));
        return tmp;
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.PUT)
    public JSONObject updateRecipe(@RequestBody RecipeRepository requestBody, HttpServletRequest request, HttpServletResponse response, @PathVariable String id){
        statsd.incrementCounter("endpoint.http.recipe.put");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_OK);
        return recipeService.updateRecipe(requestBody, userRepository.getId(), id,response);
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipe(@PathVariable String id,HttpServletResponse response){
        statsd.incrementCounter("endpoint.http.recipe.delete");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findQuery(auth.getName());
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return recipeService.deleteRecipe(id, userRepository.getId(),response);
    }

    @RequestMapping(value = "v1/recipe/{id}",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
        statsd.incrementCounter("endpoint.http.recipe.get");
        response.setStatus(HttpServletResponse.SC_OK);
        return recipeService.getRecipe(id, response);
    }
    @RequestMapping(value = "v1/recipes",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response) {
        statsd.incrementCounter("endpoint.http.recipe.get");
        response.setStatus(HttpServletResponse.SC_OK);
        return recipeService.getNewestRecipe(response);
    }


}
