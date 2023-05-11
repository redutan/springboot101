package xyz.groundx.gxstore.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.groundx.gxstore.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    void findAll() {
        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(6);
    }

    @Test
    void findAllByPromotionIsNotNull() {
        List<Product> products = productRepository.findAllByPromotionIsNotNull();

        assertThat(products).hasSize(3);
    }

    @Test
    void findAllByPriceBetween() {
        BigDecimal start = BigDecimal.valueOf(100L);
        BigDecimal end = BigDecimal.valueOf(200L);

        List<Product> products = productRepository.findAllByPriceBetween(start, end);

        assertThat(products).hasSize(2);
    }
}