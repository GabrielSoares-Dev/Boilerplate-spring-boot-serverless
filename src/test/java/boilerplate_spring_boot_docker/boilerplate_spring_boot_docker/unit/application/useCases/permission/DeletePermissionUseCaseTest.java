package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.permission;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findById.FindPermissionByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.delete.DeletePermissionUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.DeletePermissionUseCase;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DeletePermissionUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private PermissionRepositoryInterface repository;

  @InjectMocks private DeletePermissionUseCase useCase;

  private final DeletePermissionUseCaseInputDto defaultInput =
      new DeletePermissionUseCaseInputDto(1);

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testDeleted() throws BusinessException {
    LocalDateTime mockDateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    FindPermissionByIdRepositoryOutputDto findByIdOutputMock =
        new FindPermissionByIdRepositoryOutputDto(1, "test-name", "test-description", mockDateTime);

    when(this.repository.findById(this.defaultInput.id))
        .thenReturn(Optional.of(findByIdOutputMock));

    assertDoesNotThrow(
        () -> {
          this.useCase.run(this.defaultInput);
        });

    verify(this.repository, times(1)).findById(this.defaultInput.id);
    verify(this.repository, times(1)).delete(this.defaultInput.id);
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
    verify(this.repository, times(0)).delete(this.defaultInput.id);
  }
}
