package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.unit.domain.entities.role;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions.BusinessException;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.domain.entities.Role;
import org.junit.jupiter.api.Test;

public class CreateRoleTest {
  @Test
  public void testValidToCreate() {
    assertDoesNotThrow(
        () -> {
          Role entity = new Role("test-name");
          entity.create();
        });
  }

  @Test
  public void testInvalidName() {
    BusinessException exception =
        assertThrows(
            BusinessException.class,
            () -> {
              Role entity = new Role("");
              entity.create();
            });
    assertEquals("Invalid name", exception.getMessage());
  }
}
