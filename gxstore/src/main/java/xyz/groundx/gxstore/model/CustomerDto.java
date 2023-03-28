package xyz.groundx.gxstore.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerDto {
    public record Register(@NotBlank String firstName,
                           String lastName,
                           @NotBlank @Email String email,
                           @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$") String password) {
        public Customer toCustomer() {
            return new Customer(firstName, lastName, email, password);
        }
    }

    public record View(Long customerId, String firstName, String lastName, String email) {
        public View(Customer source) {
            this(source.getCustomerId(),
                    source.getFirstName(),
                    source.getLastName(),
                    source.getEmail());
        }
    }

    public record Modify(@NotBlank(message = "{xyz.groundx.gxstore.first-name.not-blank}") String firstName,
                         String lastName) {
    }
}
