package xyz.groundx.gxstore.repository;

import xyz.groundx.gxstore.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer entity);

    List<Customer> findAll();

    Optional<Customer> findById(Long customerId);
}
