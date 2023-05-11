package xyz.groundx.gxstore.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import xyz.groundx.gxstore.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByPromotionIsNotNull();
    List<Product> findAllByPromotionIsNotNull(Sort sort);

    List<Product> findAllByPriceBetween(BigDecimal start, BigDecimal end);
}
