package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.checkAuthentication;

public class CheckAuthenticationUseCaseInputDto {
  public String token;

  public CheckAuthenticationUseCaseInputDto(String token) {
    this.token = token;
  }
}
