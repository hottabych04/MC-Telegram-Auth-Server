package auth.plugin.mc.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterResp {

    private String hash;

    private String telegramId;

}
