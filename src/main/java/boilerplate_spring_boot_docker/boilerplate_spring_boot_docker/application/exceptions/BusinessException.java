package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions;

public class BusinessException extends Exception {
  public BusinessException(String message) {
    super(message);
  }
}
