package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserValidator {
  @NotEmpty(message = "Name is required")
  @Size(max = 255, message = "Name must be less than 255 characters")
  public String name;

  @NotEmpty(message = "Email is required")
  @Email(message = "Email should be valid")
  public String email;

  @NotEmpty(message = "Phone number is required")
  @Size(min = 11, max = 11, message = "Phone number must be 11 digits")
  public String phoneNumber;

  @NotEmpty(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
      message =
          "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
  public String password;
}
