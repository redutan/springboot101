package xyz.groundx.gxstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderSummary {
    Long getOrderId();

    Long getProductId();

    Long getCustomerId();

    String getProductName();

    String getSmallImage();

    String getImgAlt();

    BigDecimal getPrice();

    LocalDateTime getPurchaseDate();
}
