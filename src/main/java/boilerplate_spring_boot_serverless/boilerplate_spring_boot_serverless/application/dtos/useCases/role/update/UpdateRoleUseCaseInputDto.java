package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.role.update;

public class UpdateRoleUseCaseInputDto {
  public Integer id;
  public String name;
  public String description;

  public UpdateRoleUseCaseInputDto(Integer id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
