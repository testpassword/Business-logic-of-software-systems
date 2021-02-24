package testpassword.lab1.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import testpassword.lab1.models.Advert;

@Repository public interface AdvertRepo extends CrudRepository<Advert, Long> {
}