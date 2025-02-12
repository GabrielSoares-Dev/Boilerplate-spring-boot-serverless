package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.unit.application.useCases.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.checkAuthentication.CheckAuthenticationUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.AuthServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.auth.CheckAuthenticationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CheckAuthenticationUseCaseTest {
  @Mock private LoggerServiceInterface loggerServiceInterface;

  @Mock private AuthServiceInterface authService;

  @InjectMocks private CheckAuthenticationUseCase useCase;

  private final CheckAuthenticationUseCaseInputDto defaultInput =
      new CheckAuthenticationUseCaseInputDto("test-token");

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testAuthenticated() {
    when(this.authService.validateToken(this.defaultInput.token)).thenReturn(true);
    assertDoesNotThrow(
        () -> {
          useCase.run(this.defaultInput);
        });

    verify(this.authService, times(1)).validateToken(this.defaultInput.token);
  }
}
