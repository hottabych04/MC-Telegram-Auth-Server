package auth.plugin.mc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue loginQueue(){
        return new Queue("login_resp_queue");
    }

    @Bean
    public Queue registerQueue(){
        return new Queue("register_resp_queue");
    }

    @Bean
    public Queue notAuthQueue(){
        return new Queue("not_auth_resp_queue");
    }


    @Bean
    public TopicExchange authExchange(){
        return new TopicExchange("auth_resp_exchange");
    }

    @Bean
    public Binding loginBinding(){
        return BindingBuilder.bind(loginQueue())
                .to(authExchange())
                .with("login_resp_routing_key");
    }

    @Bean
    public Binding registerBinding(){
        return BindingBuilder.bind(registerQueue())
                .to(authExchange())
                .with("register_resp_routing_key");
    }

    @Bean
    public Binding notAuthBinding(){
        return BindingBuilder.bind(notAuthQueue())
                .to(authExchange())
                .with("not_auth_resp_routing_key");
    }

}
