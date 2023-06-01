package xyz.groundx.gxstore.repository;

import xyz.groundx.gxstore.model.Customer;

import java.util.List;

public interface CustomizedCustomerRepository {

    List<Customer> findAllByLastname(String lastName);
}
