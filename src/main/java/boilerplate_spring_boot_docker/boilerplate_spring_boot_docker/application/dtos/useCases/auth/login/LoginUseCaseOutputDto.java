package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login;

public class LoginUseCaseOutputDto {
  public String token;

  public LoginUseCaseOutputDto(String token) {
    this.token = token;
  }
}
