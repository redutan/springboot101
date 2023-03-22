package xyz.groundx.gxstore.controller;

import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.groundx.gxstore.exception.CustomerNotFoundException;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.CustomerDto;
import xyz.groundx.gxstore.service.CustomerService;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;

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
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDto.Register body) {
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
    public CustomerDto.View modify(@PathVariable Long customerId, @Valid @RequestBody CustomerDto.Modify body) {
        return customerService.modifyCustomer(customerId, body);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }


//    @DeleteMapping("/{customerId}")
//    @ResponseStatus(NO_CONTENT)
//    public void delete(@PathVariable Long customerId) {
//        customerService.deleteCustomer(customerId);
//        ResponseEntity.noContent().build();
//    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCustomerNotFoundException(CustomerNotFoundException cause) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, cause.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                             .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException cause, Locale locale) {
        var fieldErrors = cause.getFieldErrors();
        ProblemDetail body = cause.getBody();
        body.setProperty("fieldErrors", fieldErrors);
        return ResponseEntity.status(BAD_REQUEST)
                             .body(body);
    }

}
