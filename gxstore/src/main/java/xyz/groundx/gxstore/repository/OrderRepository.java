package xyz.groundx.gxstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.groundx.gxstore.model.Order;
import xyz.groundx.gxstore.model.OrderSummary;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>, CustomizedOrderRepository {

    @Query("""
            select o.orderId as orderId, o.productId as productId, o.customerId as customerId,
                   p.productName as productName, p.smallImage as smallImage, p.imgAlt as imgAlt, o.price as price,
                   o.purchaseDate as purchaseDate
              from Order o inner join Product p on o.productId = p.productId
             where o.customerId = :customerId""")
    List<OrderSummary> findCustomerOrders2(@Param("customerId") Long customerId);


    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);

}
