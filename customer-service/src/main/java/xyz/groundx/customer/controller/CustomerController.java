package xyz.groundx.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.groundx.customer.model.Customer;
import xyz.groundx.customer.model.CustomerDto;
import xyz.groundx.customer.service.CustomerService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private static URI toLocation(Customer result) {
        Long customerId = result.getCustomerId();
        return ServletUriComponentsBuilder.fromCurrentRequest()
                                          .path("/{id}")
                                          .buildAndExpand(customerId)
                                          .toUri();
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody CustomerDto.Register body) {
        Customer result = customerService.register(body);

        URI location = toLocation(result);
        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping
    public List<CustomerDto.View> all() {
        return customerService.getAllCustomers();
    }
}
