package xyz.groundx.gxstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.time.Duration;

@SpringBootApplication
public class GxstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(GxstoreApplication.class, args);
    }
}
