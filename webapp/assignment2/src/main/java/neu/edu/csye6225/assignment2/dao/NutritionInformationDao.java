package neu.edu.csye6225.assignment2.dao;

import neu.edu.csye6225.assignment2.entity.NutritionInformationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionInformationDao extends JpaRepository<NutritionInformationRepository,String> {
}
