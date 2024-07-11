package auth.plugin.mc.database.repository;

import auth.plugin.mc.database.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUuid(String uuid);

    Optional<Account> findByTelegramId(String telegramId);
}
