package org.example.synthetichumancorestarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SyntheticHumanCoreStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyntheticHumanCoreStarterApplication.class, args);
    }

}
