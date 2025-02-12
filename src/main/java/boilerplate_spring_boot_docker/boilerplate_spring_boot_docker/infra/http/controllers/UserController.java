package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.controllers;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.user.create.CreateUserUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.user.CreateUserUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.helpers.BaseResponse;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.user.CreateUserValidator;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private CreateUserUseCase createUserUseCase;

  private String logContext = "UserController";

  @PostMapping
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CreateUserValidator input) {
    this.loggerService.debug(String.format("Start %s create with input: ", this.logContext), input);
    try {
      this.createUserUseCase.run(
          new CreateUserUseCaseInputDto(
              input.name, input.email, input.phoneNumber, input.password));
      return BaseResponse.success("User created successfully", HttpStatus.CREATED);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isAlreadyExistsError = errorMessage == "User already exists";
      if (isAlreadyExistsError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }
}
