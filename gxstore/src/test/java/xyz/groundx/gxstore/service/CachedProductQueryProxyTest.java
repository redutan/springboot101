package xyz.groundx.gxstore.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import xyz.groundx.gxstore.model.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CachedProductQueryProxyTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    // SUT
    @Autowired
    CachedProductQueryProxy service;

    @SpyBean
    DbProductService dbProductService;

    @Test
    void getAllProducts() {
        // when
        List<Product> promotions = service.getAllProducts();
        List<Product> promotions2 = service.getAllProducts();    // cache-hit

        // then
        then(dbProductService).should().getAllProducts();
        assertThat(promotions).hasSize(6);
        assertThat(promotions).isEqualTo(promotions2);
    }

    @Test
    void getPromotions() {
        // when
        List<Product> promotions = service.getPromotions();
        List<Product> promotions2 = service.getPromotions();    // cache-hit

        // then
        then(dbProductService).should().getPromotions();
        assertThat(promotions).hasSize(3);
        assertThat(promotions).isEqualTo(promotions2);
    }
}