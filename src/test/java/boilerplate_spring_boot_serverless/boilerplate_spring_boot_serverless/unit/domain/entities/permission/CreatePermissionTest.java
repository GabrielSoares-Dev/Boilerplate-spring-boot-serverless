package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.unit.domain.entities.permission;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions.BusinessException;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.domain.entities.Permission;
import org.junit.jupiter.api.Test;

public class CreatePermissionTest {
  @Test
  public void testValidToCreate() {
    assertDoesNotThrow(
        () -> {
          Permission entity = new Permission("test-name");
          entity.create();
        });
  }

  @Test
  public void testInvalidName() {
    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              Permission entity = new Permission("");
              entity.create();
            });
    assertEquals("Invalid name", exception.getMessage());
  }
}
