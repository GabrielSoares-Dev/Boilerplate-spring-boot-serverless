package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findByName;

public class FindPermissionByNameRepositoryOutputDto {
  public Integer id;
  public String name;
  public String description;

  public FindPermissionByNameRepositoryOutputDto(Integer id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
