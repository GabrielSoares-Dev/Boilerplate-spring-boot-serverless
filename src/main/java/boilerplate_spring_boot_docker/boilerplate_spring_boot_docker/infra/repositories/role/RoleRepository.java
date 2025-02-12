package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.role;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.create.CreateRoleRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findAll.FindAllRolesRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findById.FindRoleByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findByName.FindRoleByNameRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.syncPermissions.SyncPermissionsRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.update.UpdateRoleRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.RoleRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Permission;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Role;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.permission.PermissionJpaRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository implements RoleRepositoryInterface {
  @Autowired private RoleJpaRepository roleJpaRepository;

  @Autowired private PermissionJpaRepository permissionJpaRepository;

  @Override
  public void create(CreateRoleRepositoryInputDto input) {
    Role model = new Role();
    model.setName(input.name);
    model.setDescription(input.description);
    roleJpaRepository.save(model);
  }

  @Override
  public List<FindAllRolesRepositoryOutputDto> findAll() {
    return roleJpaRepository.findAll().stream()
        .map(
            role ->
                new FindAllRolesRepositoryOutputDto(
                    role.getId(), role.getName(), role.getDescription(), role.getCreatedAt()))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<FindRoleByIdRepositoryOutputDto> findById(Integer id) {
    return roleJpaRepository
        .findById(id)
        .map(
            role ->
                new FindRoleByIdRepositoryOutputDto(
                    role.getId(), role.getName(), role.getDescription(), role.getCreatedAt()));
  }

  @Override
  public Optional<FindRoleByNameRepositoryOutputDto> findByName(String name) {
    return roleJpaRepository
        .findByName(name)
        .map(
            role ->
                new FindRoleByNameRepositoryOutputDto(
                    role.getId(), role.getName(), role.getDescription()));
  }

  @Override
  public void update(UpdateRoleRepositoryInputDto input) {
    Optional<Role> optionalRole = roleJpaRepository.findById(input.id);

    Role model = optionalRole.get();
    model.setName(input.name);
    model.setDescription(input.description);
    roleJpaRepository.save(model);
  }

  @Override
  public void delete(Integer id) {
    roleJpaRepository.deleteById(id);
  }

  @Transactional
  @Override
  public void syncPermissions(SyncPermissionsRepositoryInputDto input) {
    Optional<Role> role = this.roleJpaRepository.findByName(input.role);

    Set<Permission> permissions =
        new HashSet<>(this.permissionJpaRepository.findByNameIn(input.permissions));

    role.get().setPermissions(permissions);
    this.roleJpaRepository.save(role.get());
  }
}
