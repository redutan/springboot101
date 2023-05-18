package xyz.groundx.gxstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    public record Request(@NotNull @JsonProperty("customerId") Long customerId,
                          @NotNull @JsonProperty("productId") Long productId) {
    }

    public static class Response {
        @JsonProperty("orderId")
        private Long orderId;
        @JsonProperty("productId")
        private Long productId;
        @JsonProperty("customerId")
        private Long customerId;
        @JsonProperty("price")
        private BigDecimal price;
        @JsonProperty("purchaseDate")
        private LocalDateTime purchaseDate;

        public Response() {
        }

        public Response(Long orderId, Long productId, Long customerId, BigDecimal price, LocalDateTime purchaseDate) {
            this.purchaseDate = purchaseDate;
            this.price = price;
            this.customerId = customerId;
            this.productId = productId;
            this.orderId = orderId;
        }

        public Response(Order order) {
            this(order.getOrderId(), order.getProductId(), order.getCustomerId(), order.getPrice(), order.getPurchaseDate());
        }

        public LocalDateTime getPurchaseDate() {
            return purchaseDate;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public Long getProductId() {
            return productId;
        }

        public Long getOrderId() {
            return orderId;
        }
    }

    public record Summary(Long orderId, Long productId, Long customerId, String productName,
                          String smallImage, String imgAlt, BigDecimal price, LocalDateTime purchaseDate) {

    }
}
