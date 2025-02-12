package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.services.auth.getLoggedUserData.GetLoggedUserDataOutput;
import java.io.UnsupportedEncodingException;

public interface AuthServiceInterface {
  String generateToken(String email) throws IllegalArgumentException, UnsupportedEncodingException;

  boolean validateToken(String token);

  void invalidateToken();

  boolean validateCredentials(String email, String password);

  GetLoggedUserDataOutput getLoggedUserData();
}
