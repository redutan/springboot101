package xyz.groundx.gxstore.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerDto {
    @Schema(description = "고객 등록 DTO")
    public record Register(@Schema(description = "이름", example = "Jordan") @NotBlank String firstName,
                           @Schema(description = "성", example = "Jung") String lastName,
                           @Schema(description = "이메일", example = "jordan.jung@groundx.xyz") @NotBlank @Email String email,
                           @Schema(description = "비밀번호", example = "1234QWERqwer!@#$") @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$") String password) {
        public Customer toCustomer() {
            return new Customer(firstName, lastName, email, password);
        }
    }

    @Schema(description = "고객 상세 DTO")
    public record View(@Schema(description = "고객 ID", example = "1") Long customerId,
                       @Schema(description = "이름", example = "Jordan") String firstName,
                       @Schema(description = "성", example = "Jung") String lastName,
                       @Schema(description = "이메일", example = "jordan.jung@groundx.xyz") String email) {
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
