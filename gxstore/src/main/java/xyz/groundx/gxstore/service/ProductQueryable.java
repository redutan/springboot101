package xyz.groundx.gxstore.service;

import xyz.groundx.gxstore.model.Product;

import java.util.List;

public interface ProductQueryable {
    List<Product> getAllProducts();

    List<Product> getPromotions();
}
