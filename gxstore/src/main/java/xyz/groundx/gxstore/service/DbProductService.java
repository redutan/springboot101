package xyz.groundx.gxstore.service;

import org.springframework.transaction.annotation.Transactional;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.repository.ProductRepository;

import java.util.List;

public class DbProductService implements ProductQueryable {
    private final ProductRepository productRepository;

    public DbProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getPromotions() {
        return productRepository.findAllByPromotionIsNotNull();
    }
}
