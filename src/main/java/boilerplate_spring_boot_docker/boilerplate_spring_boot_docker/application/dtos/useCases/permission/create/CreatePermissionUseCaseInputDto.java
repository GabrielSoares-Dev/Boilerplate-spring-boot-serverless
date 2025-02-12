package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.permission.create;

public class CreatePermissionUseCaseInputDto {
  public String name;
  public String description;

  public CreatePermissionUseCaseInputDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
