package auth.plugin.mc.dto;

import lombok.Value;

@Value
public class AccountJoinRespDto {
    private String username;
    private String uuid;
    private Boolean successAuth;
}
