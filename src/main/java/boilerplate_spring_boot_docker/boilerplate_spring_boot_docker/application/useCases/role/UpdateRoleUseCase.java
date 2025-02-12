package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findById.FindRoleByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.update.UpdateRoleRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.update.UpdateRoleUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateRoleUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private RoleRepositoryInterface repository;

  private String logContext = "UpdateRoleUseCase";

  public void run(UpdateRoleUseCaseInputDto input) throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    Optional<FindRoleByIdRepositoryOutputDto> searchRole = this.repository.findById(input.id);

    boolean notFound = searchRole.isEmpty();
    this.loggerService.debug("not found: ", notFound);

    if (notFound) {
      throw new BusinessException("Invalid id");
    }

    this.repository.update(
        new UpdateRoleRepositoryInputDto(input.id, input.name, input.description));

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
  }
}
