package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findAll.FindAllPermissionsRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.findAll.FindAllPermissionsUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAllPermissionsUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private PermissionRepositoryInterface repository;

  private String logContext = "FindAllPermissionsUseCase";

  public List<FindAllPermissionsUseCaseOutputDto> run() {
    this.loggerService.debug(String.format("Start %s", this.logContext));

    List<FindAllPermissionsRepositoryOutputDto> permissions = this.repository.findAll();

    List<FindAllPermissionsUseCaseOutputDto> output =
        permissions.stream()
            .map(
                permission ->
                    new FindAllPermissionsUseCaseOutputDto(
                        permission.id,
                        permission.name,
                        permission.description,
                        permission.createdAt))
            .collect(Collectors.toList());
    this.loggerService.debug("output: ", output);

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
    return output;
  }
}
