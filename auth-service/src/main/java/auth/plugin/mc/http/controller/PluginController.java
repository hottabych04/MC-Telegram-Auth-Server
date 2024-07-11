package auth.plugin.mc.http.controller;

import auth.plugin.mc.dto.AccountJoinReqDto;
import auth.plugin.mc.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PluginController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/join",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String accountJoin(@RequestBody AccountJoinReqDto req){
        return authenticationService.join(req);
    }

}
