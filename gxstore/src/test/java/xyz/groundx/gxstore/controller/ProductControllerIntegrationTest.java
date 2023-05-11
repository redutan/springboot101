package xyz.groundx.gxstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProductControllerIntegrationTest {
    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:5.0.3-alpine")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redis::getHost);
        registry.add("redis.port", redis::getFirstMappedPort);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Test
    public void products(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/products")
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(6))
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].img").value("img/strings.png"))
           .andExpect(jsonPath("$[0].small_img").value("img/img-small/strings.png"))
           .andExpect(jsonPath("$[0].imgalt").value("string"))
           .andExpect(jsonPath("$[0].price").value(100.00f))
           .andExpect(jsonPath("$[0].promotion").value(nullValue()))
           .andExpect(jsonPath("$[0].desc").value(""))
           .andExpect(jsonPath("$[0].productname").value("Strings"));
    }

    @Test
    public void promotions(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/promotions")
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(3))
           .andExpect(jsonPath("$[0].id").value(4L))
           .andExpect(jsonPath("$[0].img").value("img/flute.jpeg"))
           .andExpect(jsonPath("$[1].id").value(2L))
           .andExpect(jsonPath("$[1].img").value("img/redguitar.jpeg"));
    }

    @Test
    public void productPage(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/products/page")
//                   .param("page", "1")
//                   .param("size", "4")
.contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.first").value(true))
           .andExpect(jsonPath("$.last").value(false))
           .andExpect(jsonPath("$.totalElements").value(6))
           .andExpect(jsonPath("$.totalPages").value(2))
           .andExpect(jsonPath("$.numberOfElements").value(4))
           .andExpect(jsonPath("$.content.length()").value(4))
           .andExpect(jsonPath("$.content[0].id").value(1L))
           .andExpect(jsonPath("$.content[0].img").value("img/strings.png"))
           .andExpect(jsonPath("$.content[0].small_img").value("img/img-small/strings.png"))
           .andExpect(jsonPath("$.content[0].imgalt").value("string"))
           .andExpect(jsonPath("$.content[0].price").value(100.00f))
           .andExpect(jsonPath("$.content[0].promotion").value(nullValue()))
           .andExpect(jsonPath("$.content[0].desc").value(""))
           .andExpect(jsonPath("$.content[0].productname").value("Strings"));
    }
    /*
기본은 프로모션가 오름차순으로 정렬하시오.
  /promotions == /promotions?sort=promotion == /promotions?sort=promotion,asc
그리고 프로모션가 내림차순으로 정렬 가능해야함
  /promotions?sort=promotion,desc
그리고 금액으로 정렬도 가능해야함
  /promotions?sort=price
마지막으로 프로모션가로 정렬하고 같으면 금액으로 정렬되게함
  /promotions?sort=promotion&sort=price
     */
//
//    @Test
//    void promotionsSort(@Autowired MockMvc mvc) throws Exception {
//        mvc.perform(get("/promotions")
////           .andExpect(status().isOk())
//    }
}
