package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.user;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.create.CreateUserRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.findByEmail.FindUserByEmailRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.UserRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Role;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.User;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.role.RoleJpaRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserRepositoryInterface {
  @Autowired private UserJpaRepository userJpaRepository;

  @Autowired private RoleJpaRepository roleJpaRepository;

  @Override
  public void create(CreateUserRepositoryInputDto input) {
    Role role = this.roleJpaRepository.findByName(input.role.toString()).get();
    User model = new User();
    model.setName(input.name);
    model.setEmail(input.email);
    model.setPhoneNumber(input.phoneNumber);
    model.setPassword(input.password);
    model.setRole(role);

    this.userJpaRepository.save(model);
  }

  @Override
  public Optional<FindUserByEmailRepositoryOutputDto> findByEmail(String email) {
    Optional<User> result = this.userJpaRepository.findByEmail(email);

    boolean notFound = result.isEmpty();
    if (notFound) {
      return Optional.empty();
    }

    User userData = result.get();

    FindUserByEmailRepositoryOutputDto output =
        new FindUserByEmailRepositoryOutputDto(
            userData.getId(),
            userData.getName(),
            userData.getEmail(),
            userData.getPassword(),
            userData.getPermissions(),
            userData.getRole().getId());

    return Optional.of(output);
  }
}
