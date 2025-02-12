package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findById.FindRoleByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findById.FindRoleByIdUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findById.FindRoleByIdUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindRoleByIdUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private RoleRepositoryInterface repository;

  private String logContext = "FindRoleByIdUseCase";

  public FindRoleByIdUseCaseOutputDto run(FindRoleByIdUseCaseInputDto input)
      throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    Optional<FindRoleByIdRepositoryOutputDto> searchRole = this.repository.findById(input.id);

    boolean notFound = searchRole.isEmpty();
    this.loggerService.debug("not found: ", notFound);

    if (notFound) {
      throw new BusinessException("Invalid id");
    }

    FindRoleByIdRepositoryOutputDto roleData = searchRole.get();
    this.loggerService.debug("role data: ", roleData);

    FindRoleByIdUseCaseOutputDto output =
        new FindRoleByIdUseCaseOutputDto(
            roleData.id, roleData.name, roleData.description, roleData.createdAt);
    this.loggerService.debug("output: ", output);
    this.loggerService.debug(String.format("Finish %s ", this.logContext));

    return output;
  }
}
