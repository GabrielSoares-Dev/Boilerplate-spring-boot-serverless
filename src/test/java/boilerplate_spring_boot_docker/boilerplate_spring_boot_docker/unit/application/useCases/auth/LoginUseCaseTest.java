package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login.LoginUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login.LoginUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.AuthServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.auth.LoginUseCase;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private AuthServiceInterface authService;

  @InjectMocks private LoginUseCase useCase;

  private final LoginUseCaseInputDto defaultInput =
      new LoginUseCaseInputDto("john.doe@example.com", "Password@123");

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testLogged()
      throws BusinessException, IllegalArgumentException, UnsupportedEncodingException {
    when(this.authService.validateCredentials(this.defaultInput.email, this.defaultInput.password))
        .thenReturn(true);
    when(this.authService.generateToken(this.defaultInput.email)).thenReturn("token");

    LoginUseCaseOutputDto output = this.useCase.run(this.defaultInput);
    LoginUseCaseOutputDto expectedOutput = new LoginUseCaseOutputDto("token");
    assertEquals(expectedOutput.token, output.token);

    verify(this.authService, times(1))
        .validateCredentials(this.defaultInput.email, this.defaultInput.password);
    verify(this.authService, times(1)).generateToken(this.defaultInput.email);
  }

  @Test
  public void testInvalidCredentials()
      throws BusinessException, IllegalArgumentException, UnsupportedEncodingException {
    when(this.authService.validateCredentials(this.defaultInput.email, this.defaultInput.password))
        .thenReturn(false);

    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              useCase.run(this.defaultInput);
            });

    assertEquals("Invalid credentials", exception.getMessage());

    verify(this.authService, times(1))
        .validateCredentials(this.defaultInput.email, this.defaultInput.password);
    verify(this.authService, times(0)).generateToken(this.defaultInput.email);
  }
}
