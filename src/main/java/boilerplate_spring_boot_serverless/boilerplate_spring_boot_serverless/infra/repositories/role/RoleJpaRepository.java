package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.repositories.role;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.models.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(String name);
}
