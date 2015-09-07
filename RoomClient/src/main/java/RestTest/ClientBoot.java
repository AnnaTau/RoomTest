package RestTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Tau on 06.09.2015.
 */
@SpringBootApplication
public class ClientBoot {
    public static void main(String[] args) {
        System.setProperty("server.port","8081");
        SpringApplication.run(ClientBoot.class, args);
    }
}
