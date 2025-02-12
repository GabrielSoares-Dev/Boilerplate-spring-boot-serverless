package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findByName;

public class FindRoleByNameRepositoryOutputDto {
  public Integer id;
  public String name;
  public String description;

  public FindRoleByNameRepositoryOutputDto(Integer id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
