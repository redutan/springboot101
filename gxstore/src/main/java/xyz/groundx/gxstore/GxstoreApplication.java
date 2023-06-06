package xyz.groundx.gxstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.testcontainers.containers.FixedHostPortGenericContainer;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class GxstoreApplication {
    static {
        new FixedHostPortGenericContainer<>("redis:5.0.3-alpine").withFixedExposedPort(6379, 16379);
    }

    public static void main(String[] args) {
        SpringApplication.run(GxstoreApplication.class, args);
    }
}