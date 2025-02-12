package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.controllers;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login.LoginUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login.LoginUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.auth.LoginUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.helpers.BaseResponse;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.auth.LoginValidator;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private LoginUseCase loginUseCase;

  private String logContext = "AuthController";

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginValidator input)
      throws IllegalArgumentException, UnsupportedEncodingException {
    this.loggerService.debug(String.format("Start %s login with input: ", this.logContext), input);
    try {
      LoginUseCaseOutputDto output =
          this.loginUseCase.run(new LoginUseCaseInputDto(input.email, input.password));
      this.loggerService.debug("output: ", output);
      return BaseResponse.successWithContent("Authenticated", HttpStatus.OK, output);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isInvalidCredentialsError = errorMessage == "Invalid credentials";
      if (isInvalidCredentialsError) {
        httpStatus = HttpStatus.UNAUTHORIZED;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }
}
