package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.integration.http.roleController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers.BaseAuthenticatedTest;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers.UserEmail;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.ResultActions;

public class SyncRoleWithPermissionsIntegrationTest extends BaseAuthenticatedTest {
  private String path = "/v1/role/sync-permissions";

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
      value = "classpath:insert-random-permissions.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = "classpath:reset-random-role-has-permissions.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Sql(
      value = "classpath:reset-random-roles.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Sql(
      value = "classpath:reset-random-permissions.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testSynced() throws Exception {
    Map<String, Object> input = new HashMap<>();
    input.put("role", "test-role");
    input.put("permissions", List.of("test-permission"));

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            post(this.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isOk());
    output.andExpect(jsonPath("$.message").value("Role sync successfully"));
  }

  @Test
  public void testNoSyncWhenRoleIsInvalid() throws Exception {
    Map<String, Object> input = new HashMap<>();
    input.put("role", "invalid-role");
    input.put("permissions", List.of("test-permission"));

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            post(this.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isBadRequest());
    output.andExpect(jsonPath("$.message").value("Invalid role"));
  }

  @Test
  @Sql(
      value = "classpath:insert-random-roles.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = "classpath:reset-random-roles.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testNoSyncWhenPermissionIsInvalid() throws Exception {
    Map<String, Object> input = new HashMap<>();
    input.put("role", "test-role");
    input.put("permissions", List.of("invalid-permission"));

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            post(this.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isBadRequest());
    output.andExpect(jsonPath("$.message").value("Invalid permission"));
  }

  @Test
  public void testNoSyncFieldsIsEmpty() throws Exception {
    Map<String, Object> input = new HashMap<>();
    input.put("role", null);
    input.put("permissions", null);

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            post(this.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void testAccessDenied() throws Exception {
    this.userEmail = UserEmail.TEST();
    this.generateAuthorizationToken();
    Map<String, Object> input = new HashMap<>();
    input.put("role", "test-role");
    input.put("permissions", List.of("test-permission"));

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            post(this.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isForbidden());
    output.andExpect(jsonPath("$.message").value("Access to this resource was denied"));
  }
}
