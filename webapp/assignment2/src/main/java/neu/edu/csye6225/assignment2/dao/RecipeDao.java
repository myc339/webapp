package neu.edu.csye6225.assignment2.dao;

import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeDao extends JpaRepository<RecipeRepository,String> {
    List<RecipeRepository> findByAuthor(String authorId);
    @Query(value = "select * from recipe ORDER BY created_ts desc limit 0,1",nativeQuery = true)
    RecipeRepository findNewestRecipe();
    @Query(value = "select id from recipe where author=:author",nativeQuery = true)
    List<String> getRecipeIdsByAuthor(String author);
}
