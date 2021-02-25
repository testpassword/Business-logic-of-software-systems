package testpassword.lab1.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import testpassword.lab1.models.User;

@Repository public interface UserRepo extends CrudRepository<User, Long> {

    User getByEmail(String email);

    User getByUserId(long id);

    boolean existsByEmail(String email);
}