package xyz.groundx.gxstore.service.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.Order;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.service.PlacedOrderNotifiable;

@Component
public class KakaotalkNotificator implements PlacedOrderNotifiable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void notifyToKakaotalk(Order order, Product product, Customer buyer) {
        log.info("""
                [Kakaotalk] %s %s 고객님,
                %s 상품을 구매하셨습니다.
                - 주문번호: %d
                - 구매가격: %s
                - 구매일시: %s

                감사합니다."""
                .formatted(buyer.getLastName(), buyer.getFirstName(), product.getProductName(), order.getOrderId(),
                        order.getPrice(), order.getPurchaseDate()));
    }

    @Override
    public void notify(Order order, Product product, Customer buyer) {
        this.notifyToKakaotalk(order, product, buyer);
    }
}
