package neu.edu.csye6225.assignment2.dao;

import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeDao extends JpaRepository<RecipeRepository,String> {
    Optional findById(String id);
    List<RecipeRepository> findByAuthor(String authorId);
}
