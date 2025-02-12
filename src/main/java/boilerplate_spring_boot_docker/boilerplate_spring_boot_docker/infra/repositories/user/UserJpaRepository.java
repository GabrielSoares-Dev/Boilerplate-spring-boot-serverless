package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.user;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}
