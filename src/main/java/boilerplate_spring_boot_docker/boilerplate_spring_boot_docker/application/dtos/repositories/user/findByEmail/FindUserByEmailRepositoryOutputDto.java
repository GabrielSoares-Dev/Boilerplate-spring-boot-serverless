package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.findByEmail;

public class FindUserByEmailRepositoryOutputDto {
  public Integer id;
  public String name;
  public String email;
  public String password;
  public String[] permissions;
  public Integer roleId;

  public FindUserByEmailRepositoryOutputDto(
      Integer id,
      String name,
      String email,
      String password,
      String[] permissions,
      Integer roleId) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.permissions = permissions;
    this.roleId = roleId;
  }
}
