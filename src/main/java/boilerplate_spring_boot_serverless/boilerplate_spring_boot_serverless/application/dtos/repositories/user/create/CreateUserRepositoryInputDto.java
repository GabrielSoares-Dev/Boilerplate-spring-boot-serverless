package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.user.create;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.domain.enums.RoleEnum;

public class CreateUserRepositoryInputDto {
  public String name;
  public String email;
  public String phoneNumber;
  public String password;
  public RoleEnum role;

  public CreateUserRepositoryInputDto(
      String name, String email, String phoneNumber, String password, RoleEnum role) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.role = role;
  }
}
