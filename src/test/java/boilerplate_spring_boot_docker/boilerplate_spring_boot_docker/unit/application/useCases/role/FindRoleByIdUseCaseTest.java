package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findById.FindRoleByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findById.FindRoleByIdUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findById.FindRoleByIdUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.FindRoleByIdUseCase;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FindRoleByIdUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private RoleRepositoryInterface repository;

  @InjectMocks private FindRoleByIdUseCase useCase;

  private final FindRoleByIdUseCaseInputDto defaultInput = new FindRoleByIdUseCaseInputDto(1);

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFound() throws BusinessException {
    LocalDateTime mockDateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    FindRoleByIdRepositoryOutputDto findByIdOutputMock =
        new FindRoleByIdRepositoryOutputDto(1, "test-name", "test-description", mockDateTime);
    when(this.repository.findById(this.defaultInput.id))
        .thenReturn(Optional.of(findByIdOutputMock));

    FindRoleByIdUseCaseOutputDto output = this.useCase.run(this.defaultInput);

    FindRoleByIdUseCaseOutputDto expectedOutput =
        new FindRoleByIdUseCaseOutputDto(1, "test-name", "test-description", mockDateTime);

    assertEquals(expectedOutput.id, output.id);
    assertEquals(expectedOutput.name, output.name);
    assertEquals(expectedOutput.description, output.description);
    assertEquals(expectedOutput.createdAt, output.createdAt);

    verify(this.repository, times(1)).findById(this.defaultInput.id);
  }

  @Test
  public void testNotFound() throws BusinessException {
    when(this.repository.findById(this.defaultInput.id)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              this.useCase.run(this.defaultInput);
            });
    assertEquals("Invalid id", exception.getMessage());

    verify(this.repository, times(1)).findById(this.defaultInput.id);
  }
}
