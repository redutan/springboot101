package xyz.groundx.gxstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import xyz.groundx.gxstore.model.Product;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CachedProductQueryProxy implements ProductQueryable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ProductQueryable target;
    private final ConcurrentHashMap<String, List<Product>> productsCache = new ConcurrentHashMap<>();

    public CachedProductQueryProxy(ProductQueryable target) {
        this.target = target;
    }

    @Override
    public List<Product> getAllProducts() {
        return productsTemplate("products:all", target::getAllProducts);
    }

    @Override
    public List<Product> getPromotions(Sort sort) {
        return productsTemplate("products:promotions", () -> target.getPromotions(sort));
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return target.getProducts(pageable);
    }

    private List<Product> productsTemplate(String cacheKey, Supplier<List<Product>> origin) {
        List<Product> results = productsCache.get(cacheKey);
        if (results != null) {
            log.info("Cache hit!: {}", cacheKey);
            return results;
        }
        List<Product> value = origin.get();
        productsCache.put(cacheKey, value);
        return value;
    }
}
