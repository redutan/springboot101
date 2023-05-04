package xyz.groundx.gxstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.groundx.gxstore.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class RedisProductQueryProxy implements ProductQueryable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ProductQueryable target;
    private final ObjectMapper objectMapper;
    // redis products cache
    private final RedisClient redisClient;

    public RedisProductQueryProxy(ProductQueryable target,
                                  ObjectMapper objectMapper,
                                  String redisHost,
                                  int redisPort) {
        this.target = target;
        this.objectMapper = objectMapper;
        this.redisClient = RedisClient.create(RedisURI.builder()
                                                      .withHost(redisHost)
                                                      .withPort(redisPort)
                                                      .build());
    }

    @Override
    public List<Product> getAllProducts() {
        final String key = "product:all";
        try (var connection = redisClient.connect()) {
            RedisCommands<String, String> cmd = connection.sync();
            String json = cmd.get(key);
            if (json != null) {
                log.info("Cache hit!: {}", key);
                return jsonDeserialize(json);
            }
            List<Product> products = target.getAllProducts();
            cmd.set(key, jsonSerialize(products));
            return products;
        }
    }

    private List<Product> jsonDeserialize(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Product>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String jsonSerialize(List<Product> products) {
        try {
            return objectMapper.writeValueAsString(products);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getPromotions() {
        return this.getAllProducts().stream()
                   .filter(p -> p.getPromotion() != null)
                   .collect(Collectors.toList());
    }
}
