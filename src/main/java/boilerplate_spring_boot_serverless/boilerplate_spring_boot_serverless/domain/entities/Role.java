package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.domain.entities;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions.BusinessException;

public class Role {
  private String name;

  public Role(String name) {
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
