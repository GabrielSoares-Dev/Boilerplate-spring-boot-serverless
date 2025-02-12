package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.services;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services.EncryptionServiceInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService implements EncryptionServiceInterface {
  private PasswordEncoder passwordEncoder;

  public EncryptionService() {
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @Override
  public String encrypt(String value) {
    return this.passwordEncoder.encode(value);
  }

  @Override
  public boolean matches(String rawValue, String encryptedValue) {
    return this.passwordEncoder.matches(rawValue, encryptedValue);
  }
}
