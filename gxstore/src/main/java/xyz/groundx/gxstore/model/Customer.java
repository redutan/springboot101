package xyz.groundx.gxstore.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String firstName;
    private String lastName;
    @Column(name = "email", unique = true)
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
        super.equals(o);
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
