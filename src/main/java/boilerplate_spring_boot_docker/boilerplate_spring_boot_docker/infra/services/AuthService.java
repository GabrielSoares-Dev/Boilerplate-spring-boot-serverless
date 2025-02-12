package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.services;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.findByEmail.FindUserByEmailRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.services.auth.getLoggedUserData.GetLoggedUserDataOutput;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.UserRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.AuthServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.EncryptionServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceInterface {
  @Autowired private UserRepositoryInterface userRepository;

  @Autowired private EncryptionServiceInterface encryptionService;

  @Value("${spring.jwt.secret}")
  private String SECRET_KEY;

  @Value("${spring.jwt.expiration}")
  private long EXPIRATION_TIME;

  public String generateToken(String email)
      throws IllegalArgumentException, UnsupportedEncodingException {
    FindUserByEmailRepositoryOutputDto userData = this.userRepository.findByEmail(email).get();
    Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);

    String[] permissions = userData.permissions;

    return JWT.create()
        .withSubject(email)
        .withArrayClaim("permissions", permissions)
        .withExpiresAt(new Date(System.currentTimeMillis() + this.EXPIRATION_TIME))
        .sign(algorithm);
  }

  public void invalidateToken() {}

  public boolean validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);
      JWT.require(algorithm).build().verify(token);
      return true;
    } catch (JWTVerificationException | UnsupportedEncodingException exception) {
      return false;
    }
  }

  public boolean validateCredentials(String email, String password) {
    Optional<FindUserByEmailRepositoryOutputDto> searchUser =
        this.userRepository.findByEmail(email);

    boolean userNotFound = searchUser.isEmpty();

    if (userNotFound) {
      return false;
    }

    String encryptedPassword = searchUser.get().password;
    boolean matchesPassword = this.encryptionService.matches(password, encryptedPassword);
    boolean isValid = matchesPassword;

    return isValid;
  }

  public GetLoggedUserDataOutput getLoggedUserData() {
    User userData = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return new GetLoggedUserDataOutput(userData.getId());
  }
}
