package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.role.create;

public class CreateRoleUseCaseInputDto {
  public String name;
  public String description;

  public CreateRoleUseCaseInputDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
