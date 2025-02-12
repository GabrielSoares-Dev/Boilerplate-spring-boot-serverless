package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.permission.update;

public class UpdatePermissionRepositoryInputDto {
  public Integer id;
  public String name;
  public String description;

  public UpdatePermissionRepositoryInputDto(Integer id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
