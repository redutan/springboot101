package xyz.groundx.gxstore.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecs {
    public static Specification<Customer> equalEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get(Customer_.email), email);
    }
}
