package xyz.groundx.gxstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.groundx.gxstore.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByPromotionIsNotNull();
}
