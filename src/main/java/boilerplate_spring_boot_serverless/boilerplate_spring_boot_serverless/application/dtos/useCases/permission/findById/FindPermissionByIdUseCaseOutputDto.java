package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.permission.findById;

import java.time.LocalDateTime;

public class FindPermissionByIdUseCaseOutputDto {
  public Integer id;
  public String name;
  public String description;
  public LocalDateTime createdAt;

  public FindPermissionByIdUseCaseOutputDto(
      Integer id, String name, String description, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }
}
