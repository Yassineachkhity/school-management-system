package org.openeye.departementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.openeye.departementservice.clients")
public class DepartementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartementServiceApplication.class, args);
    }

}
