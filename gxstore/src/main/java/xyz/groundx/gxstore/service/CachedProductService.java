package xyz.groundx.gxstore.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import xyz.groundx.gxstore.aspect.TryCaching;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CachedProductService implements ProductQueryable {
    private final ProductRepository productRepository;

    public CachedProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "product:all")
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @TryCaching(name = "product:promotions")
    @Transactional(readOnly = true)
    public List<Product> getPromotions(Sort sort) {
        // self-invocation: getAllProducts -> No Caching
        return this.getAllProducts().stream().filter(p -> p.getPromotion() != null).collect(Collectors.toList());
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
