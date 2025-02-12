package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.syncPermissions;

import java.util.List;

public class SyncPermissionsRepositoryInputDto {
  public String role;
  public List<String> permissions;

  public SyncPermissionsRepositoryInputDto(String role, List<String> permissions) {
    this.role = role;
    this.permissions = permissions;
  }
}
