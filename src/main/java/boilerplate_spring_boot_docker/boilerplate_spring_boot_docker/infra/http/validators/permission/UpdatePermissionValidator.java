package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.permission;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdatePermissionValidator {
  @NotEmpty(message = "Name is required")
  @Size(max = 255, message = "Name must be less than 255 characters")
  public String name;

  @Size(max = 255, message = "Description must be less than 255 characters")
  public String description;
}
