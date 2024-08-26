package auth.plugin.mc.mapper;

import auth.plugin.mc.dto.AccountDto;
import auth.plugin.mc.dto.PluginAccountDto;
import org.springframework.stereotype.Component;

@Component
public class PluginAccountDtoMapper implements Mapper<AccountDto, PluginAccountDto>{

    @Override
    public PluginAccountDto map(AccountDto object) {
        return PluginAccountDto.builder()
                .username(object.getUsername())
                .uuid(object.getUuid())
                .build();
    }

}
