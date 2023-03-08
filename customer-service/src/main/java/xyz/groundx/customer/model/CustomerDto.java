package xyz.groundx.customer.model;


public class CustomerDto {
    public record Register(String firstName, String lastName, String email, String password) {
    }

    public record View(Long customerId, String firstName, String lastName, String email) {
    }
}
