package xyz.groundx.gxstore.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.groundx.gxstore.model.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class RedisProductQueryProxyTest {
    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:5.0.3-alpine")
                    .withExposedPorts(6379);
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    // SUT
    @Autowired
    RedisProductQueryProxy service;

    @SpyBean
    DbProductService dbProductService;

    @AfterAll
    static void afterAll() {
        redis.close();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redis::getHost);
        registry.add("redis.port", redis::getFirstMappedPort);
    }

    @BeforeEach
    void setUp() {
        log.info("redis.host: {}", redis.getHost());
        log.info("redis.port: {}", redis.getFirstMappedPort());
    }

    @Test
    void getAllProducts() {
        // when
        List<Product> promotions = service.getAllProducts();
        List<Product> promotions2 = service.getAllProducts();    // cache-hit

        // then
        then(dbProductService).should().getAllProducts();

        assertThat(promotions).hasSize(6)
                              .usingRecursiveComparison()
                              .isEqualTo(promotions2);
    }

    @Test   // No cache
    void getPromotions() {
        // when
        List<Product> promotions = service.getPromotions();
        List<Product> promotions2 = service.getPromotions();

        // then
        then(dbProductService).should(times(2)).getPromotions();

        assertThat(promotions).hasSize(3)
                              .usingRecursiveComparison()
                              .isEqualTo(promotions2);
    }
}