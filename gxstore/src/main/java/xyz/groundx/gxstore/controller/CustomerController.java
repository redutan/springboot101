package xyz.groundx.gxstore.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.groundx.gxstore.exception.CustomerNotFoundException;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.CustomerDto;
import xyz.groundx.gxstore.service.CustomerService;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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

//    @GetMapping("/{customerId}")
//    public ResponseEntity<CustomerDto.View> detail(@PathVariable Long customerId) {
//        CustomerDto.View detail = customerService.getCustomer(customerId);
//        if (detail == null) {
//            return ResponseEntity.notFound()
//                                 .build();
//        }
//        return ResponseEntity.ok(detail);
//    }

    @GetMapping("/{customerId}")
    public CustomerDto.View detail(@PathVariable Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @PutMapping("/{customerId}")
    public CustomerDto.View modify(@PathVariable Long customerId, @RequestBody CustomerDto.Modify body) {
        return customerService.modifyCustomer(customerId, body);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleCustomerNotFoundException(CustomerNotFoundException cause) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, cause.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                             .body(problemDetail);
    }

}
