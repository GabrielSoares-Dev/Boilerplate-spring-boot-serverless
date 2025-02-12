package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.update;

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
