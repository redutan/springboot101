package xyz.groundx.gxstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.util.StringUtils;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.repository.CustomerRepository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
@EnableCaching
public class GxstoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(GxstoreApplication.class, args);
    }
}