package project.sec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SecApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecApplication.class, args);
    }

}