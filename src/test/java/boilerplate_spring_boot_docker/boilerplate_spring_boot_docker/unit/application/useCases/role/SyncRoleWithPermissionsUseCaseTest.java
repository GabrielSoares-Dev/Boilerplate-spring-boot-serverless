package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.role;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findByName.FindPermissionByNameRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findByName.FindRoleByNameRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.syncPermissions.SyncPermissionsRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.syncRoleWithPermissions.SyncRoleWithPermissionsUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.SyncRoleWithPermissionsUseCase;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SyncRoleWithPermissionsUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private RoleRepositoryInterface roleRepository;

  @Mock private PermissionRepositoryInterface permissionRepository;

  @InjectMocks private SyncRoleWithPermissionsUseCase useCase;

  private final SyncRoleWithPermissionsUseCaseInputDto defaultInput =
      new SyncRoleWithPermissionsUseCaseInputDto("test-role", List.of("test-permission"));

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSynced() {
    FindRoleByNameRepositoryOutputDto findRoleByNameOutputMock =
        new FindRoleByNameRepositoryOutputDto(1, "test-role", "test-description");

    FindPermissionByNameRepositoryOutputDto findPermissionByNameOutputMock =
        new FindPermissionByNameRepositoryOutputDto(1, "test-permission", "test-description");

    when(this.roleRepository.findByName("test-role"))
        .thenReturn(Optional.of(findRoleByNameOutputMock));
    when(this.permissionRepository.findByName("test-permission"))
        .thenReturn(Optional.of(findPermissionByNameOutputMock));

    assertDoesNotThrow(
        () -> {
          this.useCase.run(this.defaultInput);
        });

    verify(this.roleRepository, times(1)).findByName("test-role");
    verify(this.permissionRepository, times(1)).findByName("test-permission");

    ArgumentCaptor<SyncPermissionsRepositoryInputDto> captor =
        ArgumentCaptor.forClass(SyncPermissionsRepositoryInputDto.class);
    verify(this.roleRepository, times(1)).syncPermissions(captor.capture());

    SyncPermissionsRepositoryInputDto capturedArgument = captor.getValue();
    assertEquals(this.defaultInput.role, capturedArgument.role);
    assertEquals(this.defaultInput.permissions, capturedArgument.permissions);
  }

  @Test
  void testNoSyncWhenRoleIsInvalid() {
    when(this.roleRepository.findByName("invalid role")).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              this.useCase.run(this.defaultInput);
            });
    assertEquals("Invalid role", exception.getMessage());

    verify(this.roleRepository, times(1)).findByName("test-role");
    verify(this.permissionRepository, times(0)).findByName("test-permission");
    verify(this.roleRepository, times(0))
        .syncPermissions(
            new SyncPermissionsRepositoryInputDto(
                this.defaultInput.role, this.defaultInput.permissions));
  }

  @Test
  void testNoSyncWhenPermissionIsInvalid() {
    FindRoleByNameRepositoryOutputDto findRoleByNameOutputMock =
        new FindRoleByNameRepositoryOutputDto(1, "test-role", "test-description");
    when(this.roleRepository.findByName("test-role"))
        .thenReturn(Optional.of(findRoleByNameOutputMock));
    when(this.permissionRepository.findByName("invalid permission")).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              this.useCase.run(this.defaultInput);
            });
    assertEquals("Invalid permission", exception.getMessage());

    verify(this.roleRepository, times(1)).findByName("test-role");
    verify(this.permissionRepository, times(1)).findByName("test-permission");
    verify(this.roleRepository, times(0))
        .syncPermissions(
            new SyncPermissionsRepositoryInputDto(
                this.defaultInput.role, this.defaultInput.permissions));
  }
}
