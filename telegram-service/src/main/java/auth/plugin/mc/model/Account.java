package auth.plugin.mc.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account {

    private String uuid;

    private String username;

    private String telegramId;

}
