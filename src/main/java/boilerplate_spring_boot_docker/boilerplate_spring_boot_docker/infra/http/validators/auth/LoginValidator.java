package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.auth;

import jakarta.validation.constraints.NotEmpty;

public class LoginValidator {
  @NotEmpty(message = "Email is required")
  public String email;

  @NotEmpty(message = "Password is required")
  public String password;
}
