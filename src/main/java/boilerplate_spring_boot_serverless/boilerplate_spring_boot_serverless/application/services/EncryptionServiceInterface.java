package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services;

public interface EncryptionServiceInterface {
  String encrypt(String value);

  boolean matches(String rawValue, String encryptedValue);
}
