package xyz.groundx.gxstore.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.groundx.gxstore.model.Customer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository repository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveAndFindById() {
        Customer entity = new Customer("jordan", "jung", "jordan.jung@groundx.xyz", "P@ssW0rd");

        Customer save = repository.save(entity);

        assertThat(save).extracting(Customer::getCustomerId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                        .contains(1L, "jordan", "jung", "jung", "jordan.jung@groundx.xyz");
    }
}