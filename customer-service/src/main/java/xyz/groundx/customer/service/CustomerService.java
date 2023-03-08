package xyz.groundx.customer.service;

import xyz.groundx.customer.model.Customer;
import xyz.groundx.customer.model.CustomerDto;

import java.util.List;

import static java.util.Collections.emptyList;

public class CustomerService {
    public Customer register(CustomerDto.Register body) {
        return null;
    }

    public List<CustomerDto.View> getAllCustomers() {
        return emptyList();
    }
}
