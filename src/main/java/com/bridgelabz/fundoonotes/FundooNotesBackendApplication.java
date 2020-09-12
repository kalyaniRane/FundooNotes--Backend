package com.bridgelabz.fundoonotes;

import com.bridgelabz.fundoonotes.properties.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableConfigurationProperties({FileProperties.class})
public class FundooNotesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundooNotesBackendApplication.class, args);
    }

}
