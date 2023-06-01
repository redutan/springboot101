package xyz.groundx.gxstore.repository;

import xyz.groundx.gxstore.model.OrderDto;

import java.util.List;

public interface CustomizedOrderRepository {
    List<OrderDto.Summary> findCustomerOrders(Long customerId);
}
