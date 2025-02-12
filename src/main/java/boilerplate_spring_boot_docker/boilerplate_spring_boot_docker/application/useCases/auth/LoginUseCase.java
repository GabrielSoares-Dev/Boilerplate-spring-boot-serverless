package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.auth;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login.LoginUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login.LoginUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.AuthServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private AuthServiceInterface authService;

  private String logContext = "LoginUseCase";

  private boolean invalidCredentials(LoginUseCaseInputDto input) {
    return !this.authService.validateCredentials(input.email, input.password);
  }

  public LoginUseCaseOutputDto run(LoginUseCaseInputDto input)
      throws BusinessException, IllegalArgumentException, UnsupportedEncodingException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    boolean isInvalidCredentials = this.invalidCredentials(input);
    this.loggerService.debug("is invalid credentials: ", isInvalidCredentials);

    if (isInvalidCredentials) {
      throw new BusinessException("Invalid credentials");
    }

    String token = this.authService.generateToken(input.email);
    this.loggerService.debug("generated token: ", token);

    LoginUseCaseOutputDto output = new LoginUseCaseOutputDto(token);
    this.loggerService.debug("output: ", output);

    this.loggerService.debug(String.format("Finish %s ", this.logContext));

    return output;
  }
}
