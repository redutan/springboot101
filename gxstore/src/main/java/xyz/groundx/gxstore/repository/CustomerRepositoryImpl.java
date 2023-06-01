package xyz.groundx.gxstore.repository;

import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.Order;
import xyz.groundx.gxstore.model.QCustomer;
import xyz.groundx.gxstore.model.QOrder;

import java.util.List;

public class CustomerRepositoryImpl extends QuerydslRepositorySupport implements CustomizedCustomerRepository {
    public CustomerRepositoryImpl() {
        super(Customer.class);
    }

    @Override
    public List<Customer> findAllByLastname(String lastName) {
        QCustomer c = QCustomer.customer;
        return from(c)
                .where(c.lastName.eq(lastName))
                .select(c)
                .fetch();
    }
}
