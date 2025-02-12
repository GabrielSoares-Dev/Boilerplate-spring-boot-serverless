package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.useCases.user;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.repositories.user.create.CreateUserRepositoryInputDto;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.user.create.CreateUserUseCaseInputDto;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions.BusinessException;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.repositories.UserRepositoryInterface;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services.EncryptionServiceInterface;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.domain.entities.User;
import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.domain.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private EncryptionServiceInterface encryptionService;

  @Autowired private UserRepositoryInterface repository;

  private String logContext = "CreateUserUseCase";

  private void validate(CreateUserUseCaseInputDto input) throws BusinessException {
    User entity = new User(input.name, input.email, input.phoneNumber, input.password);
    entity.create();
  }

  private boolean foundUserBySameEmail(String email) {
    return this.repository.findByEmail(email).isPresent();
  }

  private String encryptPassword(String password) {
    return this.encryptionService.encrypt(password);
  }

  private void saveIntoDatabase(CreateUserUseCaseInputDto input) {
    String password = this.encryptPassword(input.password);

    this.repository.create(
        new CreateUserRepositoryInputDto(
            input.name, input.email, input.phoneNumber, password, RoleEnum.ADMIN));
  }

  public void run(CreateUserUseCaseInputDto input) throws BusinessException {
    this.loggerService.debug(String.format("Start %s with input: ", this.logContext), input);

    this.validate(input);

    boolean foundUserBySameEmail = this.foundUserBySameEmail(input.email);
    this.loggerService.debug("found user by same email: ", foundUserBySameEmail);

    if (foundUserBySameEmail) {
      throw new BusinessException("User already exists");
    }

    this.saveIntoDatabase(input);

    this.loggerService.debug(String.format("Finish %s ", this.logContext));
  }
}
