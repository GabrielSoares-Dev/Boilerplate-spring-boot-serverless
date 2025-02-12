package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.permission;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.create.CreatePermissionRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findAll.FindAllPermissionsRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findById.FindPermissionByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.findByName.FindPermissionByNameRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.permission.update.UpdatePermissionRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.PermissionRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Permission;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionRepository implements PermissionRepositoryInterface {
  @Autowired private PermissionJpaRepository permissionJpaRepository;

  @Override
  public void create(CreatePermissionRepositoryInputDto input) {
    Permission model = new Permission();
    model.setName(input.name);
    model.setDescription(input.description);
    permissionJpaRepository.save(model);
  }

  @Override
  public List<FindAllPermissionsRepositoryOutputDto> findAll() {
    return permissionJpaRepository.findAll().stream()
        .map(
            permission ->
                new FindAllPermissionsRepositoryOutputDto(
                    permission.getId(),
                    permission.getName(),
                    permission.getDescription(),
                    permission.getCreatedAt()))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<FindPermissionByIdRepositoryOutputDto> findById(Integer id) {
    return permissionJpaRepository
        .findById(id)
        .map(
            permission ->
                new FindPermissionByIdRepositoryOutputDto(
                    permission.getId(),
                    permission.getName(),
                    permission.getDescription(),
                    permission.getCreatedAt()));
  }

  @Override
  public Optional<FindPermissionByNameRepositoryOutputDto> findByName(String name) {
    return permissionJpaRepository
        .findByName(name)
        .map(
            permission ->
                new FindPermissionByNameRepositoryOutputDto(
                    permission.getId(), permission.getName(), permission.getDescription()));
  }

  @Override
  public void update(UpdatePermissionRepositoryInputDto input) {
    Optional<Permission> optionalPermission = permissionJpaRepository.findById(input.id);

    Permission model = optionalPermission.get();
    model.setName(input.name);
    model.setDescription(input.description);
    permissionJpaRepository.save(model);
  }

  @Override
  public void delete(Integer id) {
    permissionJpaRepository.deleteById(id);
  }
}
