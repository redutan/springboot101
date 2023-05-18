package xyz.groundx.gxstore.service.notify;

import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.Order;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.service.PlacedOrderNotifiable;

import java.util.Arrays;
import java.util.List;

public class CompositeNotificator implements PlacedOrderNotifiable {
    public List<PlacedOrderNotifiable> notificators;

    public CompositeNotificator(PlacedOrderNotifiable... notificators) {
        this.notificators = Arrays.stream(notificators).toList();
    }

    @Override
    public void notify(Order order, Product product, Customer buyer) {
        for (PlacedOrderNotifiable notificator : notificators) {
            notificator.notify(order, product, buyer);
        }
    }
}
