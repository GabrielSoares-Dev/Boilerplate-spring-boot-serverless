package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.role.findById;

import java.time.LocalDateTime;

public class FindRoleByIdUseCaseOutputDto {
  public Integer id;
  public String name;
  public String description;
  public LocalDateTime createdAt;

  public FindRoleByIdUseCaseOutputDto(
      Integer id, String name, String description, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }
}
