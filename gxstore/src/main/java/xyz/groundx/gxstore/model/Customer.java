package xyz.groundx.gxstore.model;

import java.util.Objects;

public class Customer {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // JPA
    @SuppressWarnings("unused")
    protected Customer() {
    }

    public Customer(Long customerId) {
        this.customerId = customerId;
    }

    public Customer(Long customerId, String firstName, String lastName, String email, String password) {
        this(firstName, lastName, email, password);
        this.customerId = customerId;
    }

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Long getCustomerId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, email);
    }

    public void changeName(String firstName, String lastName) {
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null. " + this);
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null. " + this);
    }
}
