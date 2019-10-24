package neu.edu.csye6225.assignment2.dao;

import neu.edu.csye6225.assignment2.entity.ImageRepository;
import neu.edu.csye6225.assignment2.entity.RecipeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDao extends JpaRepository<ImageRepository,String> {
    @Query(value = "delete from image where id=:id",nativeQuery = true)
    void DeleteImage(String id);
}
