package auth.plugin.mc.service;

import auth.plugin.mc.database.entity.Account;
import auth.plugin.mc.database.entity.Registration;
import auth.plugin.mc.database.repository.AccountRepository;
import auth.plugin.mc.database.repository.RegistrationRepository;
import auth.plugin.mc.model.LoginResp;
import auth.plugin.mc.model.RegisterResp;
import auth.plugin.mc.model.dto.PluginAccountDto;
import auth.plugin.mc.model.dto.AuthPluginAccountDto;
import auth.plugin.mc.mapper.PluginAccountDtoMapper;
import auth.plugin.mc.util.AuthActionType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final RegistrationRepository registrationRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    private final PluginAccountDtoMapper pluginAccountDtoMapper;

    private final String pluginIp;
    private final String pluginPort;

    @Value("${telegram.bot.name}")
    private String botName;

    private ExecutorService authInviteThreadPool;

    @PostConstruct
    private void init(){
        authInviteThreadPool = Executors.newSingleThreadExecutor();
    }

    public void joinProduce(PluginAccountDto acc){

        Optional<Account> accountOptional = accountRepository.findByUuid(acc.getUuid());

        if (accountOptional.isEmpty()){

            authInviteThreadPool.execute(() -> registerInvite(acc));

        } else if (accountOptional.get().getTelegramId() == null) {

            authInviteThreadPool.execute(() -> registerInvite(acc));

        } else {

            authInviteThreadPool.execute(() -> loginInvite(acc));

        }

    }

    private void registerInvite(PluginAccountDto acc) {

        Account account = Account.builder()
                .uuid(acc.getUuid())
                .username(acc.getUsername())
                .build();

        accountRepository.save(account);

        String generedHash = Base64.getUrlEncoder().encodeToString(account.getUuid().getBytes(StandardCharsets.UTF_8));

        Registration registration = Registration.builder()
                .registrationHash(generedHash)
                .account(account)
                .build();

        registrationRepository.save(registration);

        String regUrl = ("https://t.me/" + botName + "?start=" + generedHash);

        AuthPluginAccountDto authAccountDto = genQrCode(account, regUrl);

        restTemplate.postForEntity(
                getHttpAddress(
                        "/auth/invite",
                        "action=" + AuthActionType.REGISTER.getValue()
                ),
                authAccountDto,
                AuthPluginAccountDto.class
        );

    }

    private void loginInvite(PluginAccountDto accDto){

        Account account = accountRepository.findByUuid(accDto.getUuid()).get();

        rabbitTemplate.convertAndSend("auth_req_exchange", "login_req_routing_key", account);

        String logUrl = ("https://t.me/" + botName);

        AuthPluginAccountDto authAccountDto = genQrCode(account, logUrl);

        restTemplate.postForEntity(
                getHttpAddress(
                        "/auth/invite",
                        "action=" + AuthActionType.LOGIN.getValue()
                ),
                authAccountDto,
                AuthPluginAccountDto.class
        );
    }

    private AuthPluginAccountDto genQrCode(Account account, String url) {

        ByteArrayOutputStream bout = QRCode.from(url)
                .withSize(127, 127)
                .to(ImageType.PNG)
                .stream();

        String base64QrCode = new String(Base64.getEncoder().encode(bout.toByteArray()), StandardCharsets.UTF_8);

        AuthPluginAccountDto authAccountDto = AuthPluginAccountDto.builder()
                .username(account.getUsername())
                .uuid(account.getUuid())
                .url(url)
                .qrCode(base64QrCode)
                .build();

        return authAccountDto;
    }

    @RabbitListener(queues = "register_queue")
    public void consumeRegister(RegisterResp resp){registerPlayer(resp);}

    @RabbitListener(queues = "login_queue")
    public void consumeLogin(LoginResp resp){
        loginPlayer(resp);
    }

    private void loginPlayer(LoginResp resp){

        Optional<Account> accountOptional = accountRepository.findByUuid(resp.getUuid());

        if (accountOptional.isEmpty()) {
            unauthorizedPlayer(resp.getUuid());
        }

        Account account = accountOptional.get();

        PluginAccountDto playerAcc = pluginAccountDtoMapper.map(account);

        restTemplate.postForEntity(
                getHttpAddress(
                        "/auth",
                        "action=" + AuthActionType.LOGIN.getValue()
                ),
                playerAcc,
                PluginAccountDto.class
        );

    }

    private void registerPlayer(RegisterResp resp){

        String uuid = new String(Base64.getUrlDecoder().decode(resp.getHash()), StandardCharsets.UTF_8);;

        Optional<Registration> registrationOpt = registrationRepository.findByAccount_Uuid(uuid);

        if (registrationOpt.isEmpty()) {
            unauthorizedPlayer(uuid);
        }

        Registration registration = registrationOpt.get();

        Account account = registration.getAccount();

        account.setTelegramId(resp.getTelegramId());

        accountRepository.save(account);

        PluginAccountDto playerAcc = pluginAccountDtoMapper.map(account);

        registrationRepository.delete(registration);

        restTemplate.postForEntity(
                getHttpAddress(
                        "/auth",
                        "action=" + AuthActionType.REGISTER.getValue()
                ),
                playerAcc,
                PluginAccountDto.class
        );

    }

    private void unauthorizedPlayer(String uuid){
        restTemplate.postForEntity(
                getHttpAddress("/auth/not"),
                uuid,
                String.class
        );
    }

    private String getHttpAddress(String path){
        return getHttpAddress(path, null);
    }

    private String getHttpAddress(String path, String param){
        String url = ("http://" + pluginIp + ":" + pluginPort + path);

        if (param != null) {
            url = url + "?" + param;
            return url;
        }

        return url;
    }
}
