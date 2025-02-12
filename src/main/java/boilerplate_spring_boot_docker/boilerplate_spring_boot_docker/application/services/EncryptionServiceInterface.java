package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services;

public interface EncryptionServiceInterface {
  String encrypt(String value);

  boolean matches(String rawValue, String encryptedValue);
}
