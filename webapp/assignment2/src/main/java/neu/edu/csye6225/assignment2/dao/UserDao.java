package neu.edu.csye6225.assignment2.dao;

import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserRepository,String> {
      @Query(value = "select * from user u where email_address=:email",nativeQuery = true)
      UserRepository findQuery(String email);
}
