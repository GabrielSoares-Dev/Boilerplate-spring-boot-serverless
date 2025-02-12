package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.integration.http.authController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginIntegrationTest {
  @Autowired private MockMvc request;

  private ObjectMapper objectMapper;
  private String path = "/v1/auth/login";

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @Sql(
      value = "classpath:insert-admin-role.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = "classpath:insert-admin-permissions.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = "classpath:sync-permissions-with-admin-role.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "classpath:insert-users.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "classpath:reset-users.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Sql(
      value = "classpath:reset-role-has-permissions.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Sql(value = "classpath:reset-permissions.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Sql(value = "classpath:reset-roles.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testLogged() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("email", "john.doe@example.com");
    input.put("password", "Boilerplate@2023");

    String inputJson = objectMapper.writeValueAsString(input);
    ResultActions output =
        this.request.perform(
            post(this.path).contentType(MediaType.APPLICATION_JSON).content(inputJson));

    output.andExpect(status().isOk());
    output.andExpect(jsonPath("$.message").value("Authenticated"));
    output.andExpect(jsonPath("$.content.token").exists());
  }

  @Test
  public void testInvalidCredentials() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("email", "john.doe@example.com");
    input.put("password", "test");

    String inputJson = objectMapper.writeValueAsString(input);
    ResultActions output =
        this.request.perform(
            post(this.path).contentType(MediaType.APPLICATION_JSON).content(inputJson));

    output.andExpect(status().isUnauthorized());
    output.andExpect(jsonPath("$.message").value("Invalid credentials"));
  }

  @Test
  public void testEmptyFields() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("email", null);
    input.put("password", null);

    String inputJson = objectMapper.writeValueAsString(input);
    ResultActions output =
        this.request.perform(
            post(this.path).contentType(MediaType.APPLICATION_JSON).content(inputJson));

    output.andExpect(status().isUnprocessableEntity());
  }
}
