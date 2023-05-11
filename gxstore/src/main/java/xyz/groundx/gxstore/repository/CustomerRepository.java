package xyz.groundx.gxstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import xyz.groundx.gxstore.model.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @NonNull
    @Override
    @Query(value = "select c from Customer c", nativeQuery = true)
    List<Customer> findAll();

    @Query("select c from Customer c where lastName=:lastName")
    List<Customer> findAllByLastname(@Param("lastName") String lastName);
}
