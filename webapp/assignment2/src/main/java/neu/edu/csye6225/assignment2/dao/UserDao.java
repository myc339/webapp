package neu.edu.csye6225.assignment2.dao;

import neu.edu.csye6225.assignment2.entity.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserRepository,String> {
//     @Query(" select u from user u where email_address=?1 and password=?2")
//      User findByAccountAndPassword( String account,String password);
//      User findByAccount(String account);
      UserRepository findByEmail(String email);
      Optional findById(String id);
//      Optional findByToken(String token);
}
