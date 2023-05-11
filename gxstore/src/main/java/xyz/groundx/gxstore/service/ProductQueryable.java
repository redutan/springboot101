package xyz.groundx.gxstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import xyz.groundx.gxstore.model.Product;

import java.util.List;

import static org.springframework.data.domain.Sort.unsorted;

public interface ProductQueryable {
    List<Product> getAllProducts();

    default List<Product> getPromotions()  {
        return this.getPromotions(unsorted());
    }

    List<Product> getPromotions(Sort sort);

    Page<Product> getProducts(Pageable pageable);
}
