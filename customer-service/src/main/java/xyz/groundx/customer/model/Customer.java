package xyz.groundx.customer.model;

import jakarta.persistence.Entity;

@Entity
public class Customer {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Customer() {
    }

    public Customer(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
