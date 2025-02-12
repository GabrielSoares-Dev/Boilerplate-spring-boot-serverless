package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.syncRoleWithPermissions;

import java.util.List;

public class SyncRoleWithPermissionsUseCaseInputDto {
  public String role;
  public List<String> permissions;

  public SyncRoleWithPermissionsUseCaseInputDto(String role, List<String> permissions) {
    this.role = role;
    this.permissions = permissions;
  }
}
