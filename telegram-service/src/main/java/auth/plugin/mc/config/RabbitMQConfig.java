package auth.plugin.mc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue loginReqQueue(){
        return new Queue("login_req_queue");
    }

    @Bean
    public TopicExchange authExchange(){
        return new TopicExchange("auth_req_exchange");
    }

    @Bean
    public Binding loginReqBinding(){
        return BindingBuilder.bind(loginQueue())
                .to(tgExchange())
                .with("login_req_routing_key");
    }

    @Bean
    public Queue loginQueue(){
        return new Queue("login_queue");
    }

    @Bean
    public Queue registerQueue(){
        return new Queue("register_queue");
    }

    @Bean
    public TopicExchange tgExchange(){
        return new TopicExchange("auth_resp_exchange");
    }

    @Bean
    public Binding loginBinding(){
        return BindingBuilder.bind(loginQueue())
                .to(tgExchange())
                .with("login_routing_key");
    }

    @Bean
    public Binding registerBinding(){
        return BindingBuilder.bind(registerQueue())
                .to(tgExchange())
                .with("register_routing_key");
    }

}
