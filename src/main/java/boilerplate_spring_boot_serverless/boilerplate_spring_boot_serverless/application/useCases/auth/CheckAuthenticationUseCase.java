package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.useCases.auth;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.auth.checkAuthentication.CheckAuthenticationUseCaseInputDto;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions.BusinessException;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services.AuthServiceInterface;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services.LoggerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckAuthenticationUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private AuthServiceInterface authService;

  private String logContext = "CheckAuthenticationUseCase";

  public void run(CheckAuthenticationUseCaseInputDto input) throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    boolean isUnauthorized = !this.authService.validateToken(input.token);

    if (isUnauthorized) {
      throw new BusinessException("Unauthorized");
    }

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
  }
}
