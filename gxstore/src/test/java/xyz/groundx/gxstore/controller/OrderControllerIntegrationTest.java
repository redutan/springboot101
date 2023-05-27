package xyz.groundx.gxstore.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.groundx.gxstore.service.mail.EmailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIntegrationTest {
    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:5.0.3-alpine")
                    .withExposedPorts(6379);

    @SpyBean
    EmailSender emailSender;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redis::getHost);
        registry.add("redis.port", redis::getFirstMappedPort);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Order(1)
    @Test
    public void order(@Autowired MockMvc mvc) throws Exception {
        String requestJson = """
                {
                    "productId": 4,
                    "customerId": 2
                }""";
        mvc.perform(post("/orders")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(requestJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.orderId").value(1L))
           .andExpect(jsonPath("$.productId").value(4L))
           .andExpect(jsonPath("$.customerId").value(2L))
           .andExpect(jsonPath("$.price").value(190.00f))
           .andExpect(jsonPath("$.purchaseDate").exists());

        then(emailSender).should().sendEmail(any());
    }

    @Order(2)
    @Test
    public void customerOrders(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/customers/{customerId}/orders", 2L)
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(1))
           .andExpect(jsonPath("$[0].orderId").value(1L))
           .andExpect(jsonPath("$[0].productId").value(4L))
           .andExpect(jsonPath("$[0].customerId").value(2L))
           .andExpect(jsonPath("$[0].productName").value("Flute"))
           .andExpect(jsonPath("$[0].smallImage").value("img/img-small/flute.jpeg"))
           .andExpect(jsonPath("$[0].imgAlt").value("flute"))
           .andExpect(jsonPath("$[0].price").value(190.00f))
           .andExpect(jsonPath("$[0].purchaseDate").exists());
    }

    @Order(3)
    @Test
    public void customerOrders2(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/customers/{customerId}/orders2", 2L)
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(1))
           .andExpect(jsonPath("$[0].orderId").value(1L))
           .andExpect(jsonPath("$[0].productId").value(4L))
           .andExpect(jsonPath("$[0].customerId").value(2L))
           .andExpect(jsonPath("$[0].productName").value("Flute"))
           .andExpect(jsonPath("$[0].smallImage").value("img/img-small/flute.jpeg"))
           .andExpect(jsonPath("$[0].imgAlt").value("flute"))
           .andExpect(jsonPath("$[0].price").value(190.00f))
           .andExpect(jsonPath("$[0].purchaseDate").exists());
    }
}
