package auth.plugin.mc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public String botToken(@Value("${telegram.token}") String token){
        return token;
    }

}
