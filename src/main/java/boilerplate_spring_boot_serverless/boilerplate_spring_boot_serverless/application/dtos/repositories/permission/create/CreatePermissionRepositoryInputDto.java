package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.permission.create;

public class CreatePermissionRepositoryInputDto {
  public String name;
  public String description;

  public CreatePermissionRepositoryInputDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
