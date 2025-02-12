package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.exceptions;

public class BusinessException extends Exception {
  public BusinessException(String message) {
    super(message);
  }
}
