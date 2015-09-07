package RestTest;

import org.junit.Before;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Tau on 06.09.2015.
 */
@SpringBootApplication
public class BootStarter {
    ConfigurableApplicationContext context;

    public void start(){
        System.setProperty("server.port","8081");
        context = SpringApplication.run(ClientBoot.class, new String[0]);
        context.getBean(ListenerController.class);
    }

    public void tearDown(){
        context.close();
        context.stop();
    }
}
