package auth.plugin.mc.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PluginAccountDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String uuid;

}
