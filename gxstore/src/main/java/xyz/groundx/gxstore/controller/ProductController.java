package xyz.groundx.gxstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.service.ProductQueryable;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
    public List<Product> promotions(@SortDefault(value = "promotion") Sort sort) {
        return productService.getPromotions(sort);
    }

    @GetMapping("/products/page")
    public Page<Product> productPage(@PageableDefault(size = 4, sort = "productName") Pageable pageable, Sort sort) {
        return productService.getProducts(pageable);
    }
}
