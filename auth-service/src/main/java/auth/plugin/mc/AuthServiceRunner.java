package auth.plugin.mc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthServiceRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AuthServiceRunner.class);
        System.out.println(context);
    }

}
