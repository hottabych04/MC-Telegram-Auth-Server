package auth.plugin.mc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthPluginAccountDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("url")
    private String url;

}
