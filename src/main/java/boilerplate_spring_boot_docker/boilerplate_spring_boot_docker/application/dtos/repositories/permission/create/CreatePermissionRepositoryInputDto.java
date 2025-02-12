package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.create;

public class CreatePermissionRepositoryInputDto {
  public String name;
  public String description;

  public CreatePermissionRepositoryInputDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
