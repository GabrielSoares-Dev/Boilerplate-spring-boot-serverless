package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.create.CreateRoleRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findAll.FindAllRolesRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findById.FindRoleByIdRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.findByName.FindRoleByNameRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.syncPermissions.SyncPermissionsRepositoryInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.role.update.UpdateRoleRepositoryInputDto;
import java.util.List;
import java.util.Optional;

public interface RoleRepositoryInterface {
  void create(CreateRoleRepositoryInputDto input);

  List<FindAllRolesRepositoryOutputDto> findAll();

  Optional<FindRoleByIdRepositoryOutputDto> findById(Integer id);

  Optional<FindRoleByNameRepositoryOutputDto> findByName(String name);

  void update(UpdateRoleRepositoryInputDto input);

  void delete(Integer id);

  void syncPermissions(SyncPermissionsRepositoryInputDto input);
}
