package xyz.groundx.gxstore.service;

import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.Order;
import xyz.groundx.gxstore.model.Product;

public interface PlacedOrderNotifiable {
    void notify(Order order, Product product, Customer buyer);
}
