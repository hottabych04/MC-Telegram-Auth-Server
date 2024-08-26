package auth.plugin.mc.http.controller;

import auth.plugin.mc.dto.PluginAccountDto;
import auth.plugin.mc.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PluginController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/join",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void accountJoin(@RequestBody PluginAccountDto req){
        authenticationService.joinProduce(req);
    }

}
