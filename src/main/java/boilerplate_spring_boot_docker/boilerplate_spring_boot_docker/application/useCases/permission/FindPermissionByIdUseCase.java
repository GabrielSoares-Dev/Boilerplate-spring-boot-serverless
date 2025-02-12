package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findById.FindPermissionByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findById.FindPermissionByIdUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findById.FindPermissionByIdUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindPermissionByIdUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private PermissionRepositoryInterface repository;

  private String logContext = "FindPermissionByIdUseCase";

  public FindPermissionByIdUseCaseOutputDto run(FindPermissionByIdUseCaseInputDto input)
      throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    Optional<FindPermissionByIdRepositoryOutputDto> searchPermission =
        this.repository.findById(input.id);

    boolean notFound = searchPermission.isEmpty();
    this.loggerService.debug("not found: ", notFound);

    if (notFound) {
      throw new BusinessException("Invalid id");
    }

    FindPermissionByIdRepositoryOutputDto permissionData = searchPermission.get();
    this.loggerService.debug("permission data: ", permissionData);

    FindPermissionByIdUseCaseOutputDto output =
        new FindPermissionByIdUseCaseOutputDto(
            permissionData.id,
            permissionData.name,
            permissionData.description,
            permissionData.createdAt);
    this.loggerService.debug("output: ", output);
    this.loggerService.debug(String.format("Finish %s ", this.logContext));

    return output;
  }
}
