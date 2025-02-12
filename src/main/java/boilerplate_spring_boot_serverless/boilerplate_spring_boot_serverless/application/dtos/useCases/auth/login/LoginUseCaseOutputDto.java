package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.auth.login;

public class LoginUseCaseOutputDto {
  public String token;

  public LoginUseCaseOutputDto(String token) {
    this.token = token;
  }
}
