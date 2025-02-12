package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.role;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findAll.FindAllRolesRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.findAll.FindAllRolesUseCaseOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAllRolesUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private RoleRepositoryInterface repository;

  private String logContext = "FindAllRolesUseCase";

  public List<FindAllRolesUseCaseOutputDto> run() {
    this.loggerService.debug(String.format("Start %s", this.logContext));

    List<FindAllRolesRepositoryOutputDto> roles = this.repository.findAll();

    List<FindAllRolesUseCaseOutputDto> output =
        roles.stream()
            .map(
                role ->
                    new FindAllRolesUseCaseOutputDto(
                        role.id, role.name, role.description, role.createdAt))
            .collect(Collectors.toList());
    this.loggerService.debug("output: ", output);

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
    return output;
  }
}
