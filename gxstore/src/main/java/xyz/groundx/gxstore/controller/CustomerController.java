package xyz.groundx.gxstore.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Tag(name = "Customer", description = "고객 API")
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

    @Operation(summary = "고객 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공", headers = @Header(name = HttpHeaders.LOCATION, description = "등록된 고객 리소스 URI", required = true), content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> register(
            @Parameter(description = "고객 등록 RequestBody", required = true) @Valid @RequestBody CustomerDto.Register body) {
        Customer result = customerService.register(body);

        URI location = toLocation(result);
        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping
    public List<CustomerDto.View> all() {
        return customerService.getAllCustomers();
    }


    @Operation(summary = "고객 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "Not found customer", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Invalid customerId", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{customerId}")
    public CustomerDto.View detail(@Parameter(description = "조회할 고객 ID", required = true, example = "1") @PathVariable Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @Hidden
    @PutMapping("/{customerId}")
    public CustomerDto.View modify(@PathVariable Long customerId, @Valid @RequestBody CustomerDto.Modify body) {
        return customerService.modifyCustomer(customerId, body);
    }

    @Operation(summary = "고객 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@Parameter(description = "삭제할 고객 ID", required = true, example = "1") @PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

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
