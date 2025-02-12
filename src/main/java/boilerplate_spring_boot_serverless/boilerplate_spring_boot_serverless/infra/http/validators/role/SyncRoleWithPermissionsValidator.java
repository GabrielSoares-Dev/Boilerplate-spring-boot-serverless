package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.http.validators.role;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class SyncRoleWithPermissionsValidator {
  @NotEmpty(message = "Role is required")
  public String role;

  @NotEmpty(message = "Permissions list cannot be empty")
  public List<String> permissions;
}
