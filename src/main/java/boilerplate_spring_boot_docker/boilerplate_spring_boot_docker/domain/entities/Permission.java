package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.entities;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;

public class Permission {
  private String name;

  public Permission(String name) {
    this.name = name;
  }

  private boolean isInvalidName() {
    return this.name == null || this.name.isEmpty();
  }

  public void create() throws BusinessException {
    if (this.isInvalidName()) {
      throw new BusinessException("Invalid name");
    }
  }
}
