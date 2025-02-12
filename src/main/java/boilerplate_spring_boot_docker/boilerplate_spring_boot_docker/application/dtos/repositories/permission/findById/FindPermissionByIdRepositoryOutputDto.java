package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findById;

import java.time.LocalDateTime;

public class FindPermissionByIdRepositoryOutputDto {
  public Integer id;
  public String name;
  public String description;
  public LocalDateTime createdAt;

  public FindPermissionByIdRepositoryOutputDto(
      Integer id, String name, String description, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }
}
