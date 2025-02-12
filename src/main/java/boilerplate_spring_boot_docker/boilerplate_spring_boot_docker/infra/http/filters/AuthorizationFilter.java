package boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.http.filters;

import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.repositories.user.findByEmail.FindUserByEmailRepositoryOutputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.dtos.useCases.auth.checkAuthentication.CheckAuthenticationUseCaseInputDto;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.repositories.UserRepositoryInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.services.LoggerServiceInterface;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.application.useCases.auth.CheckAuthenticationUseCase;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.helpers.BaseResponse;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.Role;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.models.User;
import boilerplate_spring_boot_docker.boilerplate_spring_boot_docker.infra.repositories.role.RoleJpaRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
  @Autowired private LoggerServiceInterface loggerService;

  @Autowired private CheckAuthenticationUseCase checkAuthenticationUseCase;

  @Autowired private UserRepositoryInterface userRepository;

  @Autowired private RoleJpaRepository roleJpaRepository;

  private String[] WHITELIST = {"/v1/user", "/v1/auth/login"};

  private boolean isWhitelisted(String requestURI) {
    return Arrays.stream(this.WHITELIST).anyMatch(requestURI::startsWith);
  }

  private String extractTokenFromHeader(String authorizationHeader) throws Exception {
    boolean noHaveAuthorizationHeader = authorizationHeader == null;

    if (noHaveAuthorizationHeader) {
      throw new Exception("No has token");
    }
    ;
    String authorizationWithoutBearer = authorizationHeader.replace("Bearer ", "");
    return authorizationWithoutBearer;
  }

  private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
    ResponseEntity<Map<String, Object>> errorResponse =
        BaseResponse.error("Unauthorized", HttpStatus.UNAUTHORIZED);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(errorResponse.getBody());
    response.getWriter().write(jsonResponse);
  }

  private void setAuthenticationContext(String token, HttpServletRequest request) {
    DecodedJWT tokenData = JWT.decode(token);
    String email = tokenData.getSubject();
    FindUserByEmailRepositoryOutputDto userData = this.userRepository.findByEmail(email).get();

    Role role = this.roleJpaRepository.findById(userData.roleId).get();

    User userDetails = new User();
    userDetails.setId(userData.id);
    userDetails.setName(userData.name);
    userDetails.setEmail(userData.email);
    userDetails.setPassword(userData.password);
    userDetails.setRole(role);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (this.isWhitelisted(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String authorizationHeader = request.getHeader("Authorization");

      String token = this.extractTokenFromHeader(authorizationHeader);

      this.checkAuthenticationUseCase.run(new CheckAuthenticationUseCaseInputDto(token));

      this.setAuthenticationContext(token, request);

    } catch (Exception exception) {
      String errorMessage = exception.getMessage();
      this.loggerService.error("error with authentication: ", errorMessage);
      this.sendUnauthorizedResponse(response);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
