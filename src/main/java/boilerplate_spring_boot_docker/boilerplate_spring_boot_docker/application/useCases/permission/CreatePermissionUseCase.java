package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.permission;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.create.CreatePermissionRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.create.CreatePermissionUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.entities.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreatePermissionUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private PermissionRepositoryInterface repository;

  private String logContext = "CreatePermissionUseCase";

  private void validate(CreatePermissionUseCaseInputDto input) throws BusinessException {
    Permission entity = new Permission(input.name);
    entity.create();
  }

  private boolean foundBySameName(String name) {
    return this.repository.findByName(name).isPresent();
  }

  public void run(CreatePermissionUseCaseInputDto input) throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    this.validate(input);

    boolean foundBySameName = this.foundBySameName(input.name);
    this.loggerService.debug("found permission by same name: ", foundBySameName);

    if (foundBySameName) {
      throw new BusinessException("Permission already exists");
    }

    this.repository.create(new CreatePermissionRepositoryInputDto(input.name, input.description));

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
  }
}
