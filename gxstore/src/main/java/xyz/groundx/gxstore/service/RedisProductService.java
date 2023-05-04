package xyz.groundx.gxstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.repository.ProductRepository;

import java.util.List;

//@Service
public class RedisProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    // redis products cache
    private final RedisClient redisClient;

    public RedisProductService(ProductRepository productRepository,
                               ObjectMapper objectMapper,
                               @Value("${redis.host}") String redisHost,
                               @Value("${redis.port}") int redisPort) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.redisClient = RedisClient.create(RedisURI.builder()
                                                      .withHost(redisHost)
                                                      .withPort(redisPort)
                                                      .build());
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getPromotions() {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisCommands<String, String> cmd = connection.sync();
            String promotionsJson = cmd.get("product:promotions");
            if (promotionsJson == null) {
                List<Product> promotions = productRepository.findAllByPromotionIsNotNull();
                setPromotionsToRedis(cmd, promotions);
                return promotions;
            }
            return jsonToPromotions(promotionsJson);
        }
    }

    private List<Product> jsonToPromotions(String rawData) {
        try {
            return objectMapper.readValue(rawData, new TypeReference<List<Product>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPromotionsToRedis(RedisCommands<String, String> sync, List<Product> promotions) {
        try {
            // 아래를 주석처라히면 db를 2번 호출하므로 테스트는 실패함.
            String promotionsJson = objectMapper.writeValueAsString(promotions);
            sync.set("product:promotions", promotionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
