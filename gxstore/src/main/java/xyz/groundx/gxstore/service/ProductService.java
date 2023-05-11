package xyz.groundx.gxstore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    // in-memory products cache(Not thread safe)
    private final HashMap<String, List<Product>> productsCache = new HashMap<>();

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productsTemplate("products:all", productRepository::findAll);
    }

    @Transactional(readOnly = true)
    public List<Product> getPromotions() {
        return productsTemplate("products:promotions", () -> productRepository.findAllByPromotionIsNotNull());
    }

    private List<Product> productsTemplate(String cacheKey, Supplier<List<Product>> origin) {
        List<Product> results = productsCache.get(cacheKey);
        if (results == null) {
            results = origin.get();
            productsCache.put(cacheKey, results);
        }
        return results;
    }
}
