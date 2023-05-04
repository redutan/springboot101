package xyz.groundx.gxstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.service.ProductQueryable;

import java.util.List;

@RestController
public class ProductController {
    private final ProductQueryable productService;

    public ProductController(ProductQueryable productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> products() {
        return productService.getAllProducts();
    }

    @GetMapping("/promotions")
    public List<Product> promotions() {
        return productService.getPromotions();
    }
}
