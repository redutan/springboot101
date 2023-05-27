package xyz.groundx.gxstore.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.groundx.gxstore.exception.InvalidOrderException;
import xyz.groundx.gxstore.model.*;
import xyz.groundx.gxstore.repository.CustomerRepository;
import xyz.groundx.gxstore.repository.OrderRepository;
import xyz.groundx.gxstore.repository.ProductRepository;
import xyz.groundx.gxstore.service.mail.EmailSender;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final EmailSender emailSender;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository,
                        EmailSender emailSender) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.emailSender = emailSender;
    }

    @Transactional
    public OrderDto.Response placeOrder(OrderDto.Request command) {
        Long buyerId = command.customerId();
        Customer buyer = customerRepository.findById(buyerId)
                                           .orElseThrow(() -> new InvalidOrderException("존재하지 않는 고객입니다. " + buyerId));
        Long productId = command.productId();
        Product product = productRepository.findById(productId)
                                           .orElseThrow(() -> new InvalidOrderException("존재하지 않는 상품입니다. " + productId));

        checkDuplicatedOrder(buyerId, productId);
        Order placedOrder = addOrder(product, buyer);

        sendPlacedOrderMail(placedOrder, product, buyer);
        return new OrderDto.Response(placedOrder);
    }

    private void sendPlacedOrderMail(Order order, Product product, Customer buyer) {
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

    private void checkDuplicatedOrder(Long customerId, Long productId) {
        boolean isDuplicated = orderRepository.existsByCustomerIdAndProductId(customerId, productId);
//        boolean isDuplicated = orderRepository.exists(OrderSpecs.equalCustomerIdAndProductId(customerId, productId));
        if (isDuplicated) {
            throw new InvalidOrderException("같은 상품을 다시 주문할 수 없습니다.");
        }
    }

    private Order addOrder(Product product, Customer buyer) {
        return orderRepository.save(toOrder(product, buyer));
    }

    // 프로모션가가 있으면 프로포션 가격 아니면 상품가
    private Order toOrder(Product product, Customer buyer) {
        return new Order(product.getProductId(), buyer.getCustomerId(), product.getSoldPrice());
    }

    public List<OrderDto.Summary> getCustomerOrders(Long customerId) {
        return orderRepository.findCustomerOrders(customerId);
    }

    public List<OrderSummary> getCustomerOrders2(Long customerId) {
        return orderRepository.findCustomerOrders2(customerId);
    }
}
