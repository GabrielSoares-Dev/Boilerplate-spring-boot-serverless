package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.role.create;

public class CreateRoleRepositoryInputDto {
  public String name;
  public String description;

  public CreateRoleRepositoryInputDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
