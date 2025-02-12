package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.controllers;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.create.CreateRoleUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.delete.DeleteRoleUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findAll.FindAllRolesUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findById.FindRoleByIdUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findById.FindRoleByIdUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.syncRoleWithPermissions.SyncRoleWithPermissionsUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.update.UpdateRoleUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.CreateRoleUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.DeleteRoleUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.FindAllRolesUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.FindRoleByIdUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.SyncRoleWithPermissionsUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role.UpdateRoleUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.helpers.BaseResponse;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.role.CreateRoleValidator;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.role.SyncRoleWithPermissionsValidator;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.validators.role.UpdateRoleValidator;
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
@RequestMapping("/v1/role")
public class RoleController {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private CreateRoleUseCase createRoleUseCase;

  @Autowired private FindAllRolesUseCase findAllRolesUseCase;

  @Autowired private FindRoleByIdUseCase findRoleByIdUseCase;

  @Autowired private UpdateRoleUseCase updateRoleUseCase;

  @Autowired private DeleteRoleUseCase deleteRoleUseCase;

  @Autowired private SyncRoleWithPermissionsUseCase syncRoleWithPermissionsUseCase;

  private String logContext = "RoleController";

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_ROLE')")
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CreateRoleValidator input) {
    this.loggerService.debug(String.format("Start %s create with input: ", this.logContext), input);
    try {
      this.createRoleUseCase.run(new CreateRoleUseCaseInputDto(input.name, input.description));
      return BaseResponse.success("Role created successfully", HttpStatus.CREATED);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isAlreadyExistsError = errorMessage == "Role already exists";
      if (isAlreadyExistsError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }

  @GetMapping
  @PreAuthorize("hasAuthority('READ_ALL_ROLES')")
  public ResponseEntity<Map<String, Object>> findAll() throws BusinessException {
    this.loggerService.debug(String.format("Start %s findAll", this.logContext));
    List<FindAllRolesUseCaseOutputDto> output = this.findAllRolesUseCase.run();
    this.loggerService.debug("output: ", output);

    return BaseResponse.successWithContent("Found roles", HttpStatus.OK, output);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('READ_ROLE')")
  public ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
    this.loggerService.debug(String.format("Start %s findById with input: ", this.logContext), id);
    try {

      FindRoleByIdUseCaseOutputDto output =
          this.findRoleByIdUseCase.run(new FindRoleByIdUseCaseInputDto(id));
      this.loggerService.debug("output: ", output);

      return BaseResponse.successWithContent("Role found", HttpStatus.OK, output);
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
  @PreAuthorize("hasAuthority('UPDATE_ROLE')")
  public ResponseEntity<Map<String, Object>> update(
      @PathVariable Integer id, @Valid @RequestBody UpdateRoleValidator input) {
    this.loggerService.debug(String.format("Start %s update with input: ", this.logContext), input);
    try {
      this.updateRoleUseCase.run(new UpdateRoleUseCaseInputDto(id, input.name, input.description));
      return BaseResponse.success("Role Updated successfully", HttpStatus.OK);
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
  @PreAuthorize("hasAuthority('DELETE_ROLE')")
  public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
    this.loggerService.debug(String.format("Start %s delete with input: ", this.logContext), id);
    try {
      this.deleteRoleUseCase.run(new DeleteRoleUseCaseInputDto(id));
      return BaseResponse.success("Role deleted successfully", HttpStatus.OK);
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

  @PostMapping("/sync-permissions")
  @PreAuthorize("hasAuthority('SYNC_ROLE_WITH_PERMISSIONS')")
  public ResponseEntity<Map<String, Object>> syncPermissions(
      @Valid @RequestBody SyncRoleWithPermissionsValidator input) {
    this.loggerService.debug(
        String.format("Start %s syncPermissions with input: ", this.logContext), input);
    try {
      this.syncRoleWithPermissionsUseCase.run(
          new SyncRoleWithPermissionsUseCaseInputDto(input.role, input.permissions));
      return BaseResponse.success("Role sync successfully", HttpStatus.OK);
    } catch (BusinessException exception) {
      String errorMessage = exception.getMessage();
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      boolean isInvalidPermissionError = errorMessage == "Invalid permission";
      boolean isInvalidRoleError = errorMessage == "Invalid role";

      if (isInvalidPermissionError || isInvalidRoleError) {
        httpStatus = HttpStatus.BAD_REQUEST;
      }
      this.loggerService.error("Error: ", errorMessage);
      return BaseResponse.error(errorMessage, httpStatus);
    }
  }
}
