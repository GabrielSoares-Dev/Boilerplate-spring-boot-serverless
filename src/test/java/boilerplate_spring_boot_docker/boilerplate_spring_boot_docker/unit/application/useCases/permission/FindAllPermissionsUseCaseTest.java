package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findAll.FindAllPermissionsRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findAll.FindAllPermissionsUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.FindAllPermissionsUseCase;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FindAllPermissionsUseCaseTest {
  @Mock private LoggerServiceInterface loggerService;

  @Mock private PermissionRepositoryInterface repository;

  @InjectMocks private FindAllPermissionsUseCase useCase;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFounded() {
    FindAllPermissionsRepositoryOutputDto permission1 =
        new FindAllPermissionsRepositoryOutputDto(
            1, "Permission 1", "Description 1", LocalDateTime.now());
    FindAllPermissionsRepositoryOutputDto permission2 =
        new FindAllPermissionsRepositoryOutputDto(
            2, "Permission 2", "Description 2", LocalDateTime.now());
    List<FindAllPermissionsRepositoryOutputDto> permissions =
        Arrays.asList(permission1, permission2);

    when(repository.findAll()).thenReturn(permissions);

    List<FindAllPermissionsUseCaseOutputDto> output = useCase.run();

    assertEquals(2, output.size());
    assertEquals("Permission 1", output.get(0).name);
    assertEquals("Permission 2", output.get(1).name);

    verify(this.repository, times(1)).findAll();
  }
}
