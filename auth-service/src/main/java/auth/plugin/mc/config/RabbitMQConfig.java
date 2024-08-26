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
        return new Queue("login_req_queue");
    }

    @Bean
    public Queue registerQueue(){
        return new Queue("register_req_queue");
    }

    @Bean
    public TopicExchange tgExchange(){
        return new TopicExchange("auth_req_exchange");
    }

    @Bean
    public Binding loginBinding(){
        return BindingBuilder.bind(loginQueue())
                .to(tgExchange())
                .with("login_req_routing_key");
    }

    @Bean
    public Binding registerBinding(){
        return BindingBuilder.bind(registerQueue())
                .to(tgExchange())
                .with("register_req_routing_key");
    }
}
