package auth.plugin.mc.dto;

import lombok.Value;

@Value
public class AccountJoinReqDto {
    private String username;
    private String uuid;
}
