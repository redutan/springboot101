package xyz.groundx.gxstore.controller;

import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.groundx.gxstore.exception.InvalidOrderException;
import xyz.groundx.gxstore.model.OrderDto;
import xyz.groundx.gxstore.service.OrderService;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @ResponseStatus(CREATED)
    public OrderDto.Response order(@Valid @RequestBody OrderDto.Request req) {
        return orderService.placeOrder(req);
    }

    @GetMapping("/customers/{customerId}/orders")
    public List<OrderDto.Summary> customerOrders(@PathVariable Long customerId) {
        return orderService.getCustomerOrders(customerId);
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<ProblemDetail> handleInvalidOrderException(InvalidOrderException cause) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, cause.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                             .body(problemDetail);
    }
}
