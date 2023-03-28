package xyz.groundx.gxstore.repository;

import org.springframework.data.repository.Repository;
import xyz.groundx.gxstore.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends Repository<Customer, Long> {

    Customer save(Customer entity);

    List<Customer> findAll();

    Optional<Customer> findById(Long customerId);

    void deleteById(Long customerId);
}
