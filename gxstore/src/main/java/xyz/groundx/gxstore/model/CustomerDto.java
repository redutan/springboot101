package xyz.groundx.gxstore.model;


public class CustomerDto {
    public record Register(String firstName, String lastName, String email, String password) {
        public Customer toCustomer() {
            return new Customer(firstName, lastName, email, password);
        }
    }

    public record View(Long customerId, String firstName, String lastName, String email) {
        public View(Customer source) {
            this(source.getCustomerId(), source.getFirstName(), source.getLastName(), source.getEmail());
        }
    }

    public record Modify(String firstName, String lastName) {
    }
}
