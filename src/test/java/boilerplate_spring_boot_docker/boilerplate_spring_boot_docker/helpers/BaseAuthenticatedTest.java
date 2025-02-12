package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.helpers;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.AuthServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.enums.PermissionEnum;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.enums.RoleEnum;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Permission;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Role;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.User;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.permission.PermissionJpaRepository;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.role.RoleJpaRepository;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.user.UserJpaRepository;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class BaseAuthenticatedTest {
  @Autowired private PermissionJpaRepository permissionJpaRepository;

  @Autowired private RoleJpaRepository roleJpaRepository;

  @Autowired private UserJpaRepository userJpaRepository;

  @Autowired private AuthServiceInterface authService;

  @Autowired protected MockMvc request;

  protected String userEmail;

  protected String tokenFormatted;

  private void createPermissions() {
    List<String[]> permissionData =
        List.of(
            new String[] {PermissionEnum.CREATE_PERMISSION.toString(), "create permission"},
            new String[] {PermissionEnum.READ_ALL_PERMISSIONS.toString(), "read all permissions"},
            new String[] {PermissionEnum.DELETE_PERMISSION.toString(), "delete permission"},
            new String[] {PermissionEnum.READ_PERMISSION.toString(), "read permission"},
            new String[] {PermissionEnum.UPDATE_PERMISSION.toString(), "update permission"},
            new String[] {PermissionEnum.CREATE_ROLE.toString(), "create role"},
            new String[] {PermissionEnum.READ_ALL_ROLES.toString(), "read all roles"},
            new String[] {PermissionEnum.DELETE_ROLE.toString(), "to delete role"},
            new String[] {PermissionEnum.READ_ROLE.toString(), "read role"},
            new String[] {PermissionEnum.UPDATE_ROLE.toString(), "update role"},
            new String[] {
              PermissionEnum.SYNC_ROLE_WITH_PERMISSIONS.toString(), "sync role with permissions"
            });

    List<Permission> permissions =
        permissionData.stream()
            .map(data -> new Permission(data[0], data[1]))
            .collect(Collectors.toList());

    permissionJpaRepository.saveAll(permissions);
  }

  private void createAdminRole() {
    Role role = new Role();
    role.setName(RoleEnum.ADMIN.toString());
    role.setDescription("role to management system");
    roleJpaRepository.save(role);
  }

  private void createTestRole() {
    Role role = new Role();
    role.setName("TEST");
    role.setDescription("role to test");
    roleJpaRepository.save(role);
  }

  private void syncAdminPermissions() {
    Role adminRole = roleJpaRepository.findByName(RoleEnum.ADMIN.toString()).get();

    List<String> adminPermissionNames =
        List.of(
            PermissionEnum.CREATE_PERMISSION.toString(),
            PermissionEnum.READ_ALL_PERMISSIONS.toString(),
            PermissionEnum.DELETE_PERMISSION.toString(),
            PermissionEnum.READ_PERMISSION.toString(),
            PermissionEnum.UPDATE_PERMISSION.toString(),
            PermissionEnum.CREATE_ROLE.toString(),
            PermissionEnum.READ_ALL_ROLES.toString(),
            PermissionEnum.DELETE_ROLE.toString(),
            PermissionEnum.READ_ROLE.toString(),
            PermissionEnum.UPDATE_ROLE.toString(),
            PermissionEnum.SYNC_ROLE_WITH_PERMISSIONS.toString());

    Set<Permission> adminPermissions =
        new HashSet<>(permissionJpaRepository.findByNameIn(adminPermissionNames));

    adminRole.setPermissions(adminPermissions);

    roleJpaRepository.save(adminRole);
  }

  private void createAdminUser() {
    User user = new User();
    Role role = roleJpaRepository.findByName(RoleEnum.ADMIN.toString()).get();

    user.setName("test-name");
    user.setEmail(UserEmail.ADMIN());
    user.setPhoneNumber("1313131311");
    user.setPassword("{noop}test");
    user.setRole(role);
    userJpaRepository.save(user);
  }

  private void createTestUser() {
    User user = new User();
    Role role = roleJpaRepository.findByName("TEST").get();

    user.setName("test-name");
    user.setEmail(UserEmail.TEST());
    user.setPhoneNumber("1313131311");
    user.setPassword("{noop}test");
    user.setRole(role);
    userJpaRepository.save(user);
  }

  protected void generateAuthorizationToken()
      throws IllegalArgumentException, UnsupportedEncodingException {
    this.tokenFormatted = "Bearer " + this.authService.generateToken(this.userEmail);
  }

  private void buildAuthentication() {
    this.createPermissions();

    this.createAdminRole();

    this.createTestRole();

    this.syncAdminPermissions();

    this.createAdminUser();

    this.createTestUser();
  }

  @BeforeAll()
  public void setupAll() {
    this.buildAuthentication();
  }

  private void resetAuthentication() {
    this.userJpaRepository.deleteAll();
    this.roleJpaRepository.deleteAll();
    this.permissionJpaRepository.deleteAll();
  }

  @AfterAll()
  public void setdownAll() {
    this.resetAuthentication();
  }
}
