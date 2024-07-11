package auth.plugin.mc.integration.database.repository;

import auth.plugin.mc.database.entity.Account;
import auth.plugin.mc.database.repository.AccountRepository;
import auth.plugin.mc.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Rollback
class AccountRepositoryIT {

    private final AccountRepository accountRepository;

    @Test
    void findById(){
        Optional<Account> account = accountRepository.findById(2);
        assertTrue(account.isPresent());
    }

    @Test
    void findByUuid(){
        Optional<Account> account = accountRepository.findByUuid("sd8998435ntkjenmfd");
        assertTrue(account.isPresent());
    }

    @Test
    void findByTelegramId(){
        Optional<Account> account = accountRepository.findByTelegramId("134123565");
        assertTrue(account.isPresent());
    }

    @Test
    void save(){
        Account account = Account.builder()
                .username("testUser5")
                .uuid("b89fsd98hnt2kj234n")
                .telegramId("42390490")
                .build();

        Account savedAccount = accountRepository.save(account);

        Optional<Account> maybeAccount = accountRepository.findById(savedAccount.getId());

        assertTrue(maybeAccount.isPresent());
        assertEquals(savedAccount, maybeAccount.get());
    }


}