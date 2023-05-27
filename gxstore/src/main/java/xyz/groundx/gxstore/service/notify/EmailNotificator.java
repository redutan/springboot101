package xyz.groundx.gxstore.service.notify;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.Order;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.service.PlacedOrderNotifiable;
import xyz.groundx.gxstore.service.mail.EmailSender;

@Component
public class EmailNotificator implements PlacedOrderNotifiable {
    private EmailSender emailSender;

    public EmailNotificator(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void notifyToEmail(Order order, Product product, Customer buyer) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no_reply@groundx.xyz");
        message.setTo(buyer.getEmail());
        message.setSubject("주문 알림 GxStore");
        message.setText("""
                [Email] %s %s 고객님,
                %s 상품을 구매하셨습니다.
                - 주문번호: %d
                - 구매가격: %s
                - 구매일시: %s

                감사합니다."""
                .formatted(buyer.getLastName(), buyer.getFirstName(), product.getProductName(), order.getOrderId(),
                        order.getPrice(), order.getPurchaseDate()));

        emailSender.sendEmail(message);
    }

    @Override
    public void notify(Order order, Product product, Customer buyer) {
        this.notifyToEmail(order, product, buyer);
    }
}
