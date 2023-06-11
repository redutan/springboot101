package xyz.groundx.gxstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class GxstoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(GxstoreApplication.class, args);
    }
}