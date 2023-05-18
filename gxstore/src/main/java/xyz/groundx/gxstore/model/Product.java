package xyz.groundx.gxstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Optional;

@Table(name = "products") // !!
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long productId;
    @Column(name = "image", nullable = false)
    @JsonProperty("img")
    private String image;
    @Column(name = "small_img", nullable = false)
    @JsonProperty("small_img")
    private String smallImage;
    @JsonProperty("imgalt")
    private String imgAlt;
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    private BigDecimal promotion;

    @Column(name = "product_name", nullable = false)
    @JsonProperty("productname")
    private String productName;

    @Column(name = "description", nullable = false)
    @JsonProperty("desc")
    private String description;

    protected Product() {

    }

    public Product(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getImage() {
        return image;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public String getImgAlt() {
        return imgAlt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPromotion() {
        return promotion;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getSoldPrice() {
        return Optional.ofNullable(promotion)
                       .orElse(price);
    }
}
