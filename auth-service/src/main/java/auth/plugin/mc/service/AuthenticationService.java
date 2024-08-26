package auth.plugin.mc.service;

import auth.plugin.mc.database.entity.Account;
import auth.plugin.mc.database.repository.AccountRepository;
import auth.plugin.mc.dto.AccountDto;
import auth.plugin.mc.dto.PluginAccountDto;
import auth.plugin.mc.dto.AuthPluginAccountDto;
import auth.plugin.mc.mapper.PluginAccountDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    private final PluginAccountDtoMapper pluginAccountDtoMapper;

    private final String pluginIp;
    private final String pluginPort;

    public void joinProduce(PluginAccountDto acc){

        if (accountRepository.findByUuid(acc.getUuid()).isEmpty()){
            rabbitTemplate.convertAndSend("auth_req_exchange", "register_req_routing_key", acc.getUsername());
        } else {
            rabbitTemplate.convertAndSend("auth_req_exchange", "login_req_routing_key", acc.getUsername());
        }

    }

    @RabbitListener(queues = "register_invite_queue")
    public void consumeRegisterInvite(AuthPluginAccountDto acc){
        restTemplate.postForEntity(getHttpAddress("/register/invite"), acc, AuthPluginAccountDto.class);
    }

    @RabbitListener(queues = "login_invite_queue")
    public void consumeLoginInvite(AuthPluginAccountDto acc){
        restTemplate.postForEntity(getHttpAddress("/login/invite"), acc, AuthPluginAccountDto.class);
    }

    @RabbitListener(queues = "register_queue")
    public void consumeRegister(AccountDto acc){
        registerPlayer(acc);
    }

    @RabbitListener(queues = "login_queue")
    public void consumeLogin(AccountDto acc){
        loginPlayer(acc);
    }

    @RabbitListener(queues = "not_auth_queue")
    public void consumeNotAuth(AccountDto acc){
        notAuthPlayer(acc);
    }

    private void loginPlayer(AccountDto acc){

        PluginAccountDto playerAcc = pluginAccountDtoMapper.map(acc);

        restTemplate.postForEntity(getHttpAddress("/login"), playerAcc, PluginAccountDto.class);

    }

    private void registerPlayer(AccountDto acc){

        Account account = Account.builder()
                .username(acc.getUsername())
                .uuid(acc.getUuid())
                .telegramId(acc.getTelegramId())
                .build();

        accountRepository.save(account);

        PluginAccountDto playerAcc = pluginAccountDtoMapper.map(acc);

        restTemplate.postForEntity(getHttpAddress("/register"), playerAcc, PluginAccountDto.class);

    }

    private void notAuthPlayer(AccountDto acc) {

        PluginAccountDto playerAcc = pluginAccountDtoMapper.map(acc);

        restTemplate.postForEntity(getHttpAddress("/not/auth"), playerAcc, PluginAccountDto.class);

    }

    private String getHttpAddress(String path){
        return ("http://" + pluginIp + ":" + pluginPort + path);
    }
}
