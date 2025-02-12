package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.role.create;

public class CreateRoleUseCaseInputDto {
  public String name;
  public String description;

  public CreateRoleUseCaseInputDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
