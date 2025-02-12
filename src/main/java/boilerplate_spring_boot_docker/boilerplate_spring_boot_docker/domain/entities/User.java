package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.domain.entities;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.exceptions.BusinessException;
import java.util.regex.Pattern;

public class User {
  private String name;
  private String email;
  private String phoneNumber;
  private String password;

  public User(String name, String email, String phoneNumber, String password) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
  }

  private boolean isInvalidName() {
    return this.name == null || this.name.isEmpty();
  }

  private boolean isInvalidEmail() {
    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    return !Pattern.matches(emailRegex, this.email);
  }

  private boolean isInvalidPhoneNumber() {
    return this.phoneNumber == null || this.phoneNumber.length() != 11;
  }

  private boolean isInvalidPassword() {
    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
    return !Pattern.matches(passwordRegex, this.password);
  }

  public void create() throws BusinessException {
    if (this.isInvalidName()) {
      throw new BusinessException("Invalid name");
    }

    if (this.isInvalidEmail()) {
      throw new BusinessException("Invalid email");
    }

    if (this.isInvalidPhoneNumber()) {
      throw new BusinessException("Invalid phone number");
    }

    if (this.isInvalidPassword()) {
      throw new BusinessException("Invalid password");
    }
  }
}
