package org.openeye.sectionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.openeye.sectionservice.clients")
public class SectionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SectionServiceApplication.class, args);
    }
}
