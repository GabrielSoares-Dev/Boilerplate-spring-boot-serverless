package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.syncPermissions.SyncPermissionsRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.syncRoleWithPermissions.SyncRoleWithPermissionsUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncRoleWithPermissionsUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private RoleRepositoryInterface roleRepository;

  @Autowired private PermissionRepositoryInterface permissionRepository;

  private String logContext = "SyncRoleWithPermissionsUseCase";

  private boolean foundRole(String name) {
    return this.roleRepository.findByName(name).isPresent();
  }

  private void validRole(String role) throws BusinessException {
    if (!this.foundRole(role)) {
      throw new BusinessException("Invalid role");
    }
  }

  private boolean foundPermission(String name) {
    return this.permissionRepository.findByName(name).isPresent();
  }

  private void validPermissions(List<String> permissions) throws BusinessException {
    for (String permission : permissions) {
      if (!this.foundPermission(permission)) {
        throw new BusinessException("Invalid permission");
      }
    }
  }

  public void run(SyncRoleWithPermissionsUseCaseInputDto input) throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    this.validRole(input.role);

    this.validPermissions(input.permissions);

    this.roleRepository.syncPermissions(
        new SyncPermissionsRepositoryInputDto(input.role, input.permissions));

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
  }
}
