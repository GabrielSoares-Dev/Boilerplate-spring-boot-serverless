package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.permission;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.create.CreatePermissionRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findByName.FindPermissionByNameRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.create.CreatePermissionUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.CreatePermissionUseCase;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreatePermissionUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private PermissionRepositoryInterface repository;

  @InjectMocks private CreatePermissionUseCase useCase;

  private final CreatePermissionUseCaseInputDto defaultInput =
      new CreatePermissionUseCaseInputDto("test-name", "test-description");

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreated() throws BusinessException {
    when(this.repository.findByName(this.defaultInput.name)).thenReturn(Optional.empty());
    assertDoesNotThrow(
        () -> {
          this.useCase.run(this.defaultInput);
        });

    verify(this.repository, times(1)).findByName(this.defaultInput.name);

    ArgumentCaptor<CreatePermissionRepositoryInputDto> captor =
        ArgumentCaptor.forClass(CreatePermissionRepositoryInputDto.class);
    verify(this.repository, times(1)).create(captor.capture());

    CreatePermissionRepositoryInputDto capturedArgument = captor.getValue();
    assertEquals(this.defaultInput.name, capturedArgument.name);
    assertEquals(this.defaultInput.description, capturedArgument.description);
  }

  @Test
  public void testNotCreatedIfFoundBySameName() throws BusinessException {
    FindPermissionByNameRepositoryOutputDto findByNameOutputMock =
        new FindPermissionByNameRepositoryOutputDto(1, "test-name", "test-description");

    when(this.repository.findByName(this.defaultInput.name))
        .thenReturn(Optional.of(findByNameOutputMock));

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              this.useCase.run(this.defaultInput);
            });

    assertEquals("Permission already exists", exception.getMessage());

    verify(this.repository, times(1)).findByName(this.defaultInput.name);

    ArgumentCaptor<CreatePermissionRepositoryInputDto> captor =
        ArgumentCaptor.forClass(CreatePermissionRepositoryInputDto.class);
    verify(this.repository, times(0)).create(captor.capture());
  }
}
