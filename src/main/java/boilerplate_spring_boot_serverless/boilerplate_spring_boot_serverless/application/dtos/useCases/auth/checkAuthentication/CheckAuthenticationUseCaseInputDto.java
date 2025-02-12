package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.useCases.auth.checkAuthentication;

public class CheckAuthenticationUseCaseInputDto {
  public String token;

  public CheckAuthenticationUseCaseInputDto(String token) {
    this.token = token;
  }
}
