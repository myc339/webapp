package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.dao.RecipeDao;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    public RecipeServiceImpl( ) {

    }

    @Override
    public JSONObject save(RecipeRepository recipeRepository,String authorId, HttpServletResponse response)
    {
        if(!checkRequestBody(recipeRepository, response)){
            return null;
        }

        Date date =new Date();
        recipeRepository.setCreated_ts(date);
        recipeRepository.setUpdated_ts(date);
        recipeRepository.setId(UUID.randomUUID().toString());
        recipeRepository.setAuthor(authorId);
        recipeRepository.setTotal_time_in_min(recipeRepository.getCook_time_in_min()+recipeRepository.getPrep_time_in_min());
        for(OrderedListRepository o : recipeRepository.getSteps()){
            o.setRecipe(recipeRepository);
        }
        System.out.println(recipeRepository.getIngredients().toString());
        recipeRepository.setIngredients1(recipeRepository.getIngredients().toString());
        recipeDao.save(recipeRepository);
       return (JSONObject) JSON.toJSON(recipeRepository);
    }

    @Override
    public JSONObject updateRecipe(RecipeRepository request, String authorId, String recipeId,HttpServletResponse response) {
        if (!exist(recipeId, response)) {
            return null;
        }
        Boolean ownRecipe = ownRecipe(recipeId, authorId, response);
        if(!ownRecipe){
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You can't update others recipes ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        if(!checkRequestBody(request,response)){
            return null;
        }

        RecipeRepository recipe = recipeDao.getOne(recipeId);
        request.setId(recipeId);
        request.setAuthor(authorId);
        request.setUpdated_ts(new Date());
        request.setCreated_ts(recipe.getCreated_ts());
        request.setTotal_time_in_min(request.getCook_time_in_min()+request.getPrep_time_in_min());
        for(OrderedListRepository o : request.getSteps()){
            o.setRecipe(request);
        }
        recipeDao.save(request);
        return (JSONObject)JSON.toJSON(request);

    }

    @Override
    public JSONObject deleteRecipe(String recipeId, String authorId,HttpServletResponse response) {
        if (!exist(recipeId, response)) {
            return null;
        }
        Boolean ownRecipe = ownRecipe(recipeId, authorId, response);
        if(!ownRecipe){
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "you can't delete others recipes ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);
        recipeDao.delete(recipeRepository);
        return (JSONObject)JSON.toJSON(recipeRepository);
    }

    @Override
    public JSONObject getRecipe(String id, HttpServletResponse response) {
        if (!exist(id, response)) {
            return null;
        }
        RecipeRepository recipeRepository = recipeDao.getOne(id);
        return (JSONObject)JSON.toJSON(recipeRepository);
    }

    public boolean checkRequestBody(RecipeRepository request, HttpServletResponse response){
        //check read only properties
        if(request.getAuthor()!=null || request.getId()!=null||request.getTotal_time_in_min()!=null||request.getCreated_ts()!=null
                ||request.getUpdated_ts()!=null){

            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You can't update field including id,created_ts,updated_ts," +
                        "author_id and total_time_in_min!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //check orderList
        for(OrderedListRepository o : request.getSteps()){
            if(o.getPosition() < 1){
                try {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Position filed of OrderList can't be smaller than 1!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

        }
        //check cook&pre time's Min & Max
        if(request.getCook_time_in_min()%5 != 0 || request.getPrep_time_in_min()%5 != 0 ||request.getCook_time_in_min() < 0 || request.getPrep_time_in_min() < 0){
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cook time & prepare time should be multiple of 5!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //check serving of recipe
        if(request.getServings() < 1 || request.getServings() >5){
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Serving's minimum is 1, maximum is 5!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //check ingredients's unique
        HashSet<String> set = new HashSet<>();
        for(String s : request.getIngredients()){
            if(set.contains(s)){
                try {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ingredients's item should be unique!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            set.add(s);
        }
        return true;
    }

    public boolean ownRecipe(String recipeId, String authorId, HttpServletResponse response){
        List<RecipeRepository> recipeList = recipeDao.findByAuthor(authorId);
        for (RecipeRepository r : recipeList){
            if(r.getId().equals(recipeId)){
                return true;
            }
        }
        return false;
    }

    public boolean exist(String recipeId, HttpServletResponse response){
        try {
            //ID不存在时，打印getOne获取的对象才会报错
            System.out.print(recipeDao.getOne(recipeId));
            return true;
        }
        catch (Exception e){
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no such recipe with this ID!!!");
                return false;
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            return false;
        }
    }
}
