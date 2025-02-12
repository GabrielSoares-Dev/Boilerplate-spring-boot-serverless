package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findAll.FindAllRolesRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findAll.FindAllRolesUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.FindAllRolesUseCase;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FindAllRolesUseCaseTest {
  @Mock private LoggerServiceInterface loggerService;

  @Mock private RoleRepositoryInterface repository;

  @InjectMocks private FindAllRolesUseCase useCase;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFounded() {
    FindAllRolesRepositoryOutputDto role1 =
        new FindAllRolesRepositoryOutputDto(1, "Role 1", "Description 1", LocalDateTime.now());
    FindAllRolesRepositoryOutputDto role2 =
        new FindAllRolesRepositoryOutputDto(2, "Role 2", "Description 2", LocalDateTime.now());
    List<FindAllRolesRepositoryOutputDto> roles = Arrays.asList(role1, role2);

    when(repository.findAll()).thenReturn(roles);

    List<FindAllRolesUseCaseOutputDto> output = useCase.run();

    assertEquals(2, output.size());
    assertEquals("Role 1", output.get(0).name);
    assertEquals("Role 2", output.get(1).name);

    verify(this.repository, times(1)).findAll();
  }
}
