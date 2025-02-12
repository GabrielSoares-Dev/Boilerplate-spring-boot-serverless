package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionConfig {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("statusCode", HttpStatus.UNPROCESSABLE_ENTITY.value());
    response.put("message", "Validation failed for one or more fields");
    response.put("errors", errors);
    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("message", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("statusCode", HttpStatus.FORBIDDEN.value());
    response.put("message", "Access to this resource was denied");

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }
}
