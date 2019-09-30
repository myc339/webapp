package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.dao.RecipeDao;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.OrderedListRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        CommonResult result=new CommonResult();
//        RecipeRepository recipeRepository=new RecipeRepository();
        Date date =new Date();
        recipeRepository.setCreated_ts(date);
        recipeRepository.setUpdated_ts(date);
        recipeRepository.setId(UUID.randomUUID().toString());
        recipeRepository.setAuthor(authorId);
        recipeRepository.setTotal_time_in_min(recipeRepository.getCook_time_in_min()+recipeRepository.getPrep_time_in_min());
        for(OrderedListRepository o : recipeRepository.getSteps()){
            o.setRecipe(recipeRepository);
        }
        //System.out.println(JSON.toJSON(recipeRepository));
//        System.out.println(recipeRepository.getIngredients());
        System.out.println(recipeRepository.getIngredients().toString());
        recipeRepository.setIngredients1(recipeRepository.getIngredients().toString().replace("[","").replace("]",""));

       recipeDao.save(recipeRepository);

        result.setData(recipeRepository);
       return (JSONObject) JSON.toJSON(result);
    }

    @Override
    public JSONObject updateRecipe(RecipeRepository request, String authorId, String recipeId,HttpServletResponse response) {
        CommonResult result=new CommonResult();
        if(request.getAuthor()!=null || request.getId()!=null){
            result.setState(400);
            result.setMsg("Bad request");
            return (JSONObject)JSON.toJSON(result);
        }

        Boolean ownRecipe = exist(recipeId, authorId);

        if(!ownRecipe){
            result.setState(401);
            result.setMsg("Unauthorized");
            return (JSONObject) JSON.toJSON(result);
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

        result.setData(request);
        return (JSONObject)JSON.toJSON(result);

    }

    @Override
    public JSONObject deleteRecipe(String recipeId, String authorId,HttpServletResponse response) {
        CommonResult result=new CommonResult();
        Boolean ownRecipe = exist(recipeId, authorId);
        if(!ownRecipe){
            result.setState(401);
            result.setMsg("Unauthorized");
            return (JSONObject) JSON.toJSON(result);
        }

        RecipeRepository recipeRepository = recipeDao.getOne(recipeId);
        recipeDao.delete(recipeRepository);
        result.setState(200);
        result.setMsg("Success");
        return (JSONObject)JSON.toJSON(result);
    }

    public boolean exist(String recipeId, String authorId){
        //System.out.println(authorId + ":::::"+recipeId);
        List<RecipeRepository> recipeList = recipeDao.findByAuthor(authorId);
        //System.out.println("Before loop: "+ recipeList.get(0).getTitle());
        for (RecipeRepository r : recipeList){
            if(r.getId().equals(recipeId)){
                return true;
            }
        }
        return false;
    }
}
