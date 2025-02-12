package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.integration.http.roleController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers.BaseAuthenticatedTest;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers.UserEmail;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.ResultActions;

public class UpdateRoleIntegrationTest extends BaseAuthenticatedTest {
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
  public void testUpdated() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("name", "test-role");
    input.put("description", "test-description");

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            put(this.path + "/300")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isOk());
    output.andExpect(jsonPath("$.message").value("Role Updated successfully"));
  }

  @Test
  public void testInvalidId() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("name", "test-name");
    input.put("description", "test-description");

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            put(this.path + "/299")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isBadRequest());
    output.andExpect(jsonPath("$.message").value("Invalid id"));
  }

  @Test
  public void testEmptyFields() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("name", null);
    input.put("description", null);

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            put(this.path + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void testAccessDenied() throws Exception {
    this.userEmail = UserEmail.TEST();
    this.generateAuthorizationToken();

    Map<String, String> input = new HashMap<>();
    input.put("name", "test-name");
    input.put("description", "test-description");

    String inputJson = new ObjectMapper().writeValueAsString(input);

    ResultActions output =
        this.request.perform(
            put(this.path + "/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .header("Authorization", this.tokenFormatted));

    output.andExpect(status().isForbidden());
    output.andExpect(jsonPath("$.message").value("Access to this resource was denied"));
  }
}
