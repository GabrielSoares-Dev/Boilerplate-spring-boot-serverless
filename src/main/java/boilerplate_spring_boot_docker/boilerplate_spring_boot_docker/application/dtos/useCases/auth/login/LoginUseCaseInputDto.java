package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.login;

public class LoginUseCaseInputDto {
  public String email;
  public String password;

  public LoginUseCaseInputDto(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
