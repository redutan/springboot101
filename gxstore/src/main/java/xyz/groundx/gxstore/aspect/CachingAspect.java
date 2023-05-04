package xyz.groundx.gxstore.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.groundx.gxstore.model.Product;

import java.util.List;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
@Aspect
@Component
public class CachingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    // redis products cache
    private final RedisClient redisClient;

    public CachingAspect(ObjectMapper objectMapper,
                         @Value("${redis.host}") String redisHost,
                         @Value("${redis.port}") int redisPort) {
        this.objectMapper = objectMapper;
        this.redisClient = RedisClient.create(RedisURI.builder()
                                                      .withHost(redisHost)
                                                      .withPort(redisPort)
                                                      .build());
    }

    @SuppressWarnings("unchecked")
    @Around(value = "@annotation(TryCaching)")
    public Object caching(ProceedingJoinPoint joinPoint) throws Throwable {
        TryCaching caching = getTryCachingAnnotation(joinPoint);
        String key = caching.name();

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisCommands<String, String> cmd = connection.sync();
            String json = cmd.get(key);
            if (json == null) {
                Object result = joinPoint.proceed();
                saveToRedis(cmd, key, (List<Product>) result);
                return result;
            }
            return jsonToProducts(json);
        }
    }

    private TryCaching getTryCachingAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return AnnotationUtils.findAnnotation(signature.getMethod(), TryCaching.class);
    }

    private List<Product> jsonToProducts(String rawData) {
        try {
            return objectMapper.readValue(rawData, new TypeReference<List<Product>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToRedis(RedisCommands<String, String> sync, String key, List<Product> products) {
        try {
            // 아래를 주석처라히면 db를 2번 호출하므로 테스트는 실패함.
            String json = objectMapper.writeValueAsString(products);
            sync.set(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.warn("Fail to set redis", e);
        }
    }
}
