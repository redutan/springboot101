package xyz.groundx.gxstore.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.groundx.gxstore.model.Customer;

import java.util.List;

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

        Long customerId = save.getCustomerId();
        assertThat(customerId).isGreaterThan(0L);
        assertThat(save).extracting(Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                        .containsExactly("jordan", "jung", "jordan.jung@groundx.xyz");

        Customer customer = repository.findById(customerId)
                                      .orElse(null);
        assertThat(customer).isNotNull()
                            .isEqualTo(save);
    }

    @Test
    void findAll() {
        Customer entity = new Customer("jordan", "jung", "jordan.jung@groundx.xyz", "P@ssW0rd");
        Customer entity2 = new Customer("winnie", "byun", "winnie.byun@groundx.xyz", "SecretNumber");
        Customer entity3 = new Customer("ted", "jeong", "ted.jeong@groundx.xyz", "dragAndDrop");
        Customer save = repository.save(entity);
        Customer save2 = repository.save(entity2);
        Customer save3 = repository.save(entity3);

        List<Customer> customers = repository.findAll();

        assertThat(customers).size().isEqualTo(3);
        assertThat(customers).extracting(Customer::getCustomerId).allMatch(id -> id > 0L);
        assertThat(customers.get(0)).extracting(Customer::getCustomerId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                                    .contains(save.getCustomerId(), "jordan", "jung", "jordan.jung@groundx.xyz");
        assertThat(customers.get(1)).extracting(Customer::getCustomerId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                                    .contains(save2.getCustomerId(), "winnie", "byun", "winnie.byun@groundx.xyz");
        assertThat(customers.get(2)).extracting(Customer::getCustomerId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                                    .contains(save3.getCustomerId(), "ted", "jeong", "ted.jeong@groundx.xyz");
    }

    @Test
    void findAllByLastName() {
        Customer entity = new Customer("jordan", "jung", "jordan.jung@groundx.xyz", "P@ssW0rd");
        Customer entity2 = new Customer("winnie", "byun", "winnie.byun@groundx.xyz", "SecretNumber");
        Customer entity3 = new Customer("ted", "jeong", "ted.jeong@groundx.xyz", "dragAndDrop");
        Customer save = repository.save(entity);
        Customer save2 = repository.save(entity2);
        Customer save3 = repository.save(entity3);

        List<Customer> customers = repository.findAllByLastname("jung");

        assertThat(customers).size().isEqualTo(1);
        assertThat(customers).extracting(Customer::getCustomerId).allMatch(id -> id > 0L);
        assertThat(customers.get(0)).extracting(Customer::getCustomerId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                                    .contains(save.getCustomerId(), "jordan", "jung", "jordan.jung@groundx.xyz");
    }

    @Test
    void modify() {
        Customer entity = new Customer("jordan", "jung", "jordan.jung@groundx.xyz", "P@ssW0rd");

        Customer save = repository.save(entity);
        save.changeName("myeongju", "jeong");
        Customer save2 = repository.save(save);
        assertThat(save).extracting(Customer::getCustomerId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                        .contains(save2.getCustomerId(), "myeongju", "jeong", "jordan.jung@groundx.xyz");
    }

    @Test
    void deleteById() {
        Customer entity = new Customer("jordan", "jung", "jordan.jung@groundx.xyz", "P@ssW0rd");

        Customer save = repository.save(entity);
        Long customerId = save.getCustomerId();

        repository.deleteById(customerId);

        Customer customer = repository.findById(customerId)
                                      .orElse(null);
        assertThat(customer).isNull();
    }
}