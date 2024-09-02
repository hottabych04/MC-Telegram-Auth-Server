package auth.plugin.mc.model.dto;

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
