package com.clincaloutcome;

import com.clincaloutcome.fileStorage.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class EUClinicalTrialApplication {

    public static void main(String[] args) {
        SpringApplication.run(EUClinicalTrialApplication.class, args);
    }
}
