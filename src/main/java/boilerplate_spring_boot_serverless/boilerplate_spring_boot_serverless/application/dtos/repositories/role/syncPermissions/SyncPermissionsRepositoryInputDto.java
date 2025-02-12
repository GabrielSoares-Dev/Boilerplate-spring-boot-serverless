package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.role.syncPermissions;

import java.util.List;

public class SyncPermissionsRepositoryInputDto {
  public String role;
  public List<String> permissions;

  public SyncPermissionsRepositoryInputDto(String role, List<String> permissions) {
    this.role = role;
    this.permissions = permissions;
  }
}
