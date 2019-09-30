package neu.edu.csye6225.assignment2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
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
    private UserDao userDao;

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value="v1/recipe",method = RequestMethod.POST,produces="application/json", consumes="application/json")
    @ResponseBody
    public JSONObject saveRecipe( @RequestBody RecipeRepository requestBody, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findByEmail(auth.getName());

        CommonResult result=new CommonResult();

        try{
            return recipeService.save(requestBody,userRepository.getId(),response);
        }
        catch(Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            result.setState(500);
            result.setMsg("failure");
            return (JSONObject) JSON.toJSON(result);
        }
    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.PUT)
    public JSONObject updateRecipe(@RequestBody RecipeRepository requestBody, HttpServletRequest request, HttpServletResponse response, @PathVariable String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findByEmail(auth.getName());

        CommonResult result=new CommonResult();

        try{
            return recipeService.updateRecipe(requestBody, userRepository.getId(), id,response);
        }
        catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.setState(500);
            result.setMsg("failure");
            return (JSONObject) JSON.toJSON(result);
        }

    }

    @RequestMapping(value="v1/recipe/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteRecipe(@PathVariable String id,HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRepository userRepository =userDao.findByEmail(auth.getName());

        CommonResult result=new CommonResult();

        try{
            return recipeService.deleteRecipe(id, userRepository.getId(),response);
        }
        catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.setState(500);
            result.setMsg("failure");
            return (JSONObject) JSON.toJSON(result);
        }
    }

    @RequestMapping(value = "v1/recipe/{id}",method= RequestMethod.GET)
    public JSONObject findRecipeById(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
        UserRepository userRepository = userDao.getOne(id);
        CommonResult result = new CommonResult();

        try {
            return recipeService.getRecipe(id);
        }
        catch(Exception e) {
            e.printStackTrace();
            result.setState(404);
            result.setMsg("Not Found");
            return (JSONObject) JSON.toJSON(result);
        }
    }
}
