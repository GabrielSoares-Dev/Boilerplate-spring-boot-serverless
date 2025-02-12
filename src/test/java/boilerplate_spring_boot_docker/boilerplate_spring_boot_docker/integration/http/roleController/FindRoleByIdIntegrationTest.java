package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.integration.http.roleController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers.BaseAuthenticatedTest;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers.UserEmail;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.ResultActions;

public class FindRoleByIdIntegrationTest extends BaseAuthenticatedTest {
  private String path = "/v1/role";

  @BeforeEach
  public void setupEach() throws IllegalArgumentException, UnsupportedEncodingException {
    this.userEmail = UserEmail.ADMIN();
    this.generateAuthorizationToken();
  }

  @Test
  @Sql(
      value = "classpath:insert-random-roles.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = "classpath:reset-random-roles.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testFounded() throws Exception {
    ResultActions output =
        this.request.perform(get(this.path + "/300").header("Authorization", this.tokenFormatted));

    output.andExpect(status().isOk());
    output.andExpect(jsonPath("$.message").value("Role found"));
    output.andExpect(jsonPath("$.content.id").value(300));
    output.andExpect(jsonPath("$.content.name").value("test-role"));
  }

  @Test
  public void testInvalidId() throws Exception {

    ResultActions output =
        this.request.perform(get(this.path + "/200").header("Authorization", this.tokenFormatted));

    output.andExpect(status().isBadRequest());
    output.andExpect(jsonPath("$.message").value("Invalid id"));
  }

  @Test
  public void testAccessDenied() throws Exception {
    this.userEmail = UserEmail.TEST();
    this.generateAuthorizationToken();

    ResultActions output =
        this.request.perform(get(this.path + "/200").header("Authorization", this.tokenFormatted));

    output.andExpect(status().isForbidden());
    output.andExpect(jsonPath("$.message").value("Access to this resource was denied"));
  }
}
