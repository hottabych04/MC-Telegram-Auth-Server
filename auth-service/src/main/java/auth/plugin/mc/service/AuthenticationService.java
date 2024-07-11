package auth.plugin.mc.service;

import auth.plugin.mc.database.repository.AccountRepository;
import auth.plugin.mc.dto.AccountJoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;

    public String join(AccountJoinReqDto acc){
        if (accountRepository.findByUuid(acc.getUuid()).isEmpty()){
            return "https://t.me/hottabych04";
        } else {
            return null;
        }
    }

}
