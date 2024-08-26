package auth.plugin.mc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate(List<HttpMessageConverter<?>> messageConverters){
        return new RestTemplate(messageConverters);
    }

    @Bean
    public String pluginIp(@Value("${minecraft.plugin.ip}") String ip){
        return ip;
    }

    @Bean
    public String pluginPort(@Value("${minecraft.plugin.port}") String port){
        return port;
    }

}
