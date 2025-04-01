package com.iruyeon.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@SpringBootApplication
@EnableJpaAuditing
public class IruyeonApplication {

    public static void main(String[] args) {
        SpringApplication.run(IruyeonApplication.class, args);
    }

}
