package neu.edu.csye6225.assignment2.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neu.edu.csye6225.assignment2.common.CommonResult;
import neu.edu.csye6225.assignment2.dao.RecipeDao;
import neu.edu.csye6225.assignment2.dao.UserDao;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import neu.edu.csye6225.assignment2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public JSONObject save(RecipeRepository recipeRepository, String authorId)
    {
        CommonResult result=new CommonResult();

        Date date =new Date();
        recipeRepository.setCreated_ts(date);
        recipeRepository.setUpdated_ts(date);
        recipeRepository.setId(UUID.randomUUID().toString());
        recipeRepository.setAuthor_id(authorId);
        recipeRepository.setTotal_time_in_min(recipeRepository.getCook_time_in_min()+recipeRepository.getPrep_time_in_min());
//        for(OrderedListRepository o : recipeRepository.getSteps()){
//            o.setRecipe(recipeRepository);
//        }
        System.out.println(JSON.toJSON(recipeRepository));

        recipeDao.save(recipeRepository);

        result.setData(recipeRepository);
        return (JSONObject) JSON.toJSON(result);
    }
}
