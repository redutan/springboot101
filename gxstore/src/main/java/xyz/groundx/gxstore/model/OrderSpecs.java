package xyz.groundx.gxstore.model;

import org.springframework.data.jpa.domain.Specification;

public class OrderSpecs {
    public static Specification<Order> equalCustomerId(Long customerId) {
        return (root, query, builder) -> builder.equal(root.get(Order_.customerId), customerId);
    }

    public static Specification<Order> equalProductId(Long productId) {
        return (root, query, builder) -> builder.equal(root.get(Order_.productId), productId);
    }

    public static Specification<Order> equalCustomerIdAndProductId(Long customerId, Long productId) {
        return equalCustomerId(customerId).and(equalProductId(productId));
    }
}
