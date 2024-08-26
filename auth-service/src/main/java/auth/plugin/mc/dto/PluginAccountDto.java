package auth.plugin.mc.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PluginAccountDto {

    private String username;

    private String uuid;

}
