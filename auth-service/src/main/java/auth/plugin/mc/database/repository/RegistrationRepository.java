package auth.plugin.mc.database.repository;

import auth.plugin.mc.database.entity.Registration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

    Optional<Registration> findByAccount_Uuid(String uuid);

}
