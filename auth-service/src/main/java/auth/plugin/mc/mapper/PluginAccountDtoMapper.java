package auth.plugin.mc.mapper;

import auth.plugin.mc.database.entity.Account;
import auth.plugin.mc.model.dto.PluginAccountDto;
import org.springframework.stereotype.Component;

@Component
public class PluginAccountDtoMapper implements Mapper<Account, PluginAccountDto>{

    @Override
    public PluginAccountDto map(Account object) {
        return PluginAccountDto.builder()
                .username(object.getUsername())
                .uuid(object.getUuid())
                .build();
    }

}
