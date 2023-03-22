package xyz.groundx.gxstore.service;

import xyz.groundx.gxstore.exception.CustomerNotFoundException;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.CustomerDto;
import xyz.groundx.gxstore.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer register(CustomerDto.Register body) {
        Customer entity = body.toCustomer();
        return customerRepository.save(entity);
    }

    public List<CustomerDto.View> getAllCustomers() {
        List<Customer> results = customerRepository.findAll();
        return results.stream()
                      .map(CustomerDto.View::new)
                      .collect(Collectors.toList());
    }

    public CustomerDto.View getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                                 .map(CustomerDto.View::new)
                                 .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public CustomerDto.View modifyCustomer(Long customerId, CustomerDto.Modify command) {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new CustomerNotFoundException(customerId));
        customer.changeName(command.firstName(), command.lastName());
        Customer saved = customerRepository.save(customer);
        return new CustomerDto.View(saved);
    }

    public void deleteCustomer(Long customerId) {
        // TODO
    }
}
