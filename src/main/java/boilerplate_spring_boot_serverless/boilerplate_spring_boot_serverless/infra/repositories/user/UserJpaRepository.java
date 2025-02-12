package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.repositories.user;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}
