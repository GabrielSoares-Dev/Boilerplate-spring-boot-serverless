package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.services;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.application.dtos.services.auth.getLoggedUserData.GetLoggedUserDataOutput;
import java.io.UnsupportedEncodingException;

public interface AuthServiceInterface {
  String generateToken(String email) throws IllegalArgumentException, UnsupportedEncodingException;

  boolean validateToken(String token);

  void invalidateToken();

  boolean validateCredentials(String email, String password);

  GetLoggedUserDataOutput getLoggedUserData();
}
