package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.unit.application.useCases.role;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.role.create.CreateRoleRepositoryInputDto;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.role.findByName.FindRoleByNameRepositoryOutputDto;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.role.create.CreateRoleUseCaseInputDto;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions.BusinessException;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.useCases.role.CreateRoleUseCase;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreateRoleUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private RoleRepositoryInterface repository;

  @InjectMocks private CreateRoleUseCase useCase;

  private final CreateRoleUseCaseInputDto defaultInput =
      new CreateRoleUseCaseInputDto("test-name", "test-description");

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

    ArgumentCaptor<CreateRoleRepositoryInputDto> captor =
        ArgumentCaptor.forClass(CreateRoleRepositoryInputDto.class);
    verify(this.repository, times(1)).create(captor.capture());

    CreateRoleRepositoryInputDto capturedArgument = captor.getValue();
    assertEquals(this.defaultInput.name, capturedArgument.name);
    assertEquals(this.defaultInput.description, capturedArgument.description);
  }

  @Test
  public void testNotCreatedIfFoundBySameName() throws BusinessException {
    FindRoleByNameRepositoryOutputDto findByNameOutputMock =
        new FindRoleByNameRepositoryOutputDto(1, "test-name", "test-description");

    when(this.repository.findByName(this.defaultInput.name))
        .thenReturn(Optional.of(findByNameOutputMock));

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              this.useCase.run(this.defaultInput);
            });

    assertEquals("Role already exists", exception.getMessage());

    verify(this.repository, times(1)).findByName(this.defaultInput.name);

    ArgumentCaptor<CreateRoleRepositoryInputDto> captor =
        ArgumentCaptor.forClass(CreateRoleRepositoryInputDto.class);
    verify(this.repository, times(0)).create(captor.capture());
  }
}
