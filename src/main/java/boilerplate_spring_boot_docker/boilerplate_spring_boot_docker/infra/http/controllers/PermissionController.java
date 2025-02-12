package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.controllers;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.create.CreatePermissionUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.delete.DeletePermissionUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findAll.FindAllPermissionsUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findById.FindPermissionByIdUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findById.FindPermissionByIdUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.update.UpdatePermissionUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.CreatePermissionUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.DeletePermissionUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.FindAllPermissionsUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.FindPermissionByIdUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission.UpdatePermissionUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.helpers.BaseResponse;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.permission.CreatePermissionValidator;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.permission.UpdatePermissionValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/permission")
public class PermissionController {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private CreatePermissionUseCase createPermissionUseCase;

  @Autowired private FindAllPermissionsUseCase findAllPermissionsUseCase;

  @Autowired private FindPermissionByIdUseCase findPermissionByIdUseCase;

  @Autowired private UpdatePermissionUseCase updatePermissionUseCase;

  @Autowired private DeletePermissionUseCase deletePermissionUseCase;

  private String logContext = "PermissionController";

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
  public ResponseEntity<Map<String, Object>> create(
      @Valid @RequestBody CreatePermissionValidator input) {
    this.loggerService.debug(String.format("Start %s create with input: ", this.logContext), input);
    try {
      this.createPermissionUseCase.run(
          new CreatePermissionUseCaseInputDto(input.name, input.description));
      return BaseResponse.success("Permission created successfully", HttpStatus.CREATED);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isAlreadyExistsError = errorMessage == "Permission already exists";
      if (isAlreadyExistsError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }

  @GetMapping
  @PreAuthorize("hasAuthority('READ_ALL_PERMISSIONS')")
  public ResponseEntity<Map<String, Object>> findAll() throws BusinessException {
    this.loggerService.debug(String.format("Start %s findAll", this.logContext));
    List<FindAllPermissionsUseCaseOutputDto> output = this.findAllPermissionsUseCase.run();
    this.loggerService.debug("output: ", output);

    return BaseResponse.successWithContent("Found permissions", HttpStatus.OK, output);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('READ_PERMISSION')")
  public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
    this.loggerService.debug(String.format("Start %s findById with input: ", this.logContext), id);
    try {

      FindPermissionByIdUseCaseOutputDto output =
          this.findPermissionByIdUseCase.run(new FindPermissionByIdUseCaseInputDto(id));
      this.loggerService.debug("output: ", output);

      return BaseResponse.successWithContent("Permission found", HttpStatus.OK, output);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isInvalidIdError = "Invalid id" == errorMessage;

      if (isInvalidIdError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error:", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('UPDATE_PERMISSION')")
  public ResponseEntity<Map<String, Object>> update(
      @PathVariable Integer id, @Valid @RequestBody UpdatePermissionValidator input) {
    this.loggerService.debug(String.format("Start %s update with input: ", this.logContext), input);
    try {
      this.updatePermissionUseCase.run(
          new UpdatePermissionUseCaseInputDto(id, input.name, input.description));
      return BaseResponse.success("Permission Updated successfully", HttpStatus.OK);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isInvalidIdError = "Invalid id" == errorMessage;

      if (isInvalidIdError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
  public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
    this.loggerService.debug(String.format("Start %s delete with input: ", this.logContext), id);
    try {
      this.deletePermissionUseCase.run(new DeletePermissionUseCaseInputDto(id));
      return BaseResponse.success("Permission deleted successfully", HttpStatus.OK);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isInvalidIdError = "Invalid id" == errorMessage;

      if (isInvalidIdError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }
}
