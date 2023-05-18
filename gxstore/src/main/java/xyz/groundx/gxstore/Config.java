package xyz.groundx.gxstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.groundx.gxstore.repository.ProductRepository;
import xyz.groundx.gxstore.service.CachedProductQueryProxy;
import xyz.groundx.gxstore.service.CachedProductService;
import xyz.groundx.gxstore.service.DbProductService;
import xyz.groundx.gxstore.service.RedisProductQueryProxy;
import xyz.groundx.gxstore.service.notify.CompositeNotificator;
import xyz.groundx.gxstore.service.notify.EmailNotificator;
import xyz.groundx.gxstore.service.notify.KakaotalkNotificator;

@Configuration
public class Config {
    @Bean
    RedisProductQueryProxy redisProductQueryProxy(DbProductService dbProductService,
                                                  ObjectMapper objectMapper,
                                                  @Value("${redis.host:localhost}") String host,
                                                  @Value("${redis.port:6379}") int port) {
        return new RedisProductQueryProxy(dbProductService, objectMapper, host, port);
    }

    @Primary
    @Bean
    CachedProductQueryProxy cachedProductQueryProxy(DbProductService dbProductService) {
        return new CachedProductQueryProxy(dbProductService);
    }

    @Bean
    DbProductService dbProductService(ProductRepository productRepository) {
        return new DbProductService(productRepository);
    }

    @Bean
    CachedProductService cachingAspectProductService(ProductRepository productRepository) {
        return new CachedProductService(productRepository);
    }

    @Primary
    @Bean
    CompositeNotificator compositeNotificator(EmailNotificator emailNotificator, KakaotalkNotificator kakaotalkNotificator) {
        return new CompositeNotificator(emailNotificator, kakaotalkNotificator);
    }
}
