package auth.plugin.mc.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDto {

    private String username;

    private String uuid;

    private String telegramId;
}
