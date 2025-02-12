package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.create.CreateRoleRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.create.CreateRoleUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRoleUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private RoleRepositoryInterface repository;

  private String logContext = "CreateRoleUseCase";

  private void validate(CreateRoleUseCaseInputDto input) throws BusinessException {
    Role entity = new Role(input.name);
    entity.create();
  }

  private boolean foundBySameName(String name) {
    return this.repository.findByName(name).isPresent();
  }

  public void run(CreateRoleUseCaseInputDto input) throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    this.validate(input);

    boolean foundBySameName = this.foundBySameName(input.name);
    this.loggerService.debug("found role by same name: ", foundBySameName);

    if (foundBySameName) {
      throw new BusinessException("Role already exists");
    }

    this.repository.create(new CreateRoleRepositoryInputDto(input.name, input.description));

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
  }
}
