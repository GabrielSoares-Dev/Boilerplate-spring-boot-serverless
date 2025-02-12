package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.create.CreateUserRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.findByEmail.FindUserByEmailRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.user.create.CreateUserUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.UserRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.EncryptionServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.user.CreateUserUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.enums.RoleEnum;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreateUserUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private EncryptionServiceInterface encryptionService;

  @Mock private UserRepositoryInterface repository;

  @InjectMocks private CreateUserUseCase useCase;

  private final CreateUserUseCaseInputDto defaultInput =
      new CreateUserUseCaseInputDto(
          "John Doe", "john.doe@example.com", "12345678901", "Password@123");

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreate() throws BusinessException {
    when(this.repository.findByEmail(this.defaultInput.email)).thenReturn(Optional.empty());
    when(this.encryptionService.encrypt(this.defaultInput.password)).thenReturn("encrypt-test");

    assertDoesNotThrow(
        () -> {
          this.useCase.run(this.defaultInput);
        });

    verify(this.repository, times(1)).findByEmail(this.defaultInput.email);
    verify(this.encryptionService, times(1)).encrypt(this.defaultInput.password);

    ArgumentCaptor<CreateUserRepositoryInputDto> captor =
        ArgumentCaptor.forClass(CreateUserRepositoryInputDto.class);
    verify(this.repository, times(1)).create(captor.capture());

    CreateUserRepositoryInputDto capturedArgument = captor.getValue();
    assertEquals(this.defaultInput.name, capturedArgument.name);
    assertEquals(this.defaultInput.email, capturedArgument.email);
    assertEquals(this.defaultInput.phoneNumber, capturedArgument.phoneNumber);
    assertEquals("encrypt-test", capturedArgument.password);
    assertEquals(RoleEnum.ADMIN, capturedArgument.role);
  }

  @Test
  public void testNotCreateIfFoundUserBySameEmail() throws BusinessException {
    String[] permissions = {"test-permission"};
    FindUserByEmailRepositoryOutputDto findByEmailOutputMock =
        new FindUserByEmailRepositoryOutputDto(
            1, "John Doe", "john.doe@example.com", "password-test", permissions, 1);

    when(this.repository.findByEmail(this.defaultInput.email))
        .thenReturn(Optional.of(findByEmailOutputMock));

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              this.useCase.run(this.defaultInput);
            });

    assertEquals("User already exists", exception.getMessage());

    verify(this.repository, times(1)).findByEmail(this.defaultInput.email);

    ArgumentCaptor<CreateUserRepositoryInputDto> captor =
        ArgumentCaptor.forClass(CreateUserRepositoryInputDto.class);
    verify(this.repository, times(0)).create(captor.capture());
  }
}
