package xyz.groundx.gxstore.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import xyz.groundx.gxstore.model.*;

import java.util.List;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements CustomizedOrderRepository {
    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public List<OrderDto.Summary> findCustomerOrders(Long customerId) {
        QOrder o = QOrder.order;
        QProduct p = QProduct.product;
        return from(o)
                .innerJoin(p)   // inner join
                .on(o.productId.eq(p.productId))        // on
                .fetchJoin()    // 연관 엔티티 한 번에 조회
                .where(o.customerId.eq(customerId))    // where
                .select(new QOrderDto_Summary(o.orderId, o.productId, o.customerId, p.productName,
                        p.smallImage, p.imgAlt, o.price, o.purchaseDate))    // Projection to DTO
                .fetch();
    }
}
