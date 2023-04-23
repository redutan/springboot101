package xyz.groundx.gxstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.groundx.gxstore.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
