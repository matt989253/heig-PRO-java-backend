package ch.heigvd.pro.b04.login;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.endpoints.LoginController;
import ch.heigvd.pro.b04.login.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

  @InjectMocks
  LoginController loginController;

  @Mock
  ModeratorRepository moderatorRepository;

  @Test
  public void testRegistrationWorks() {

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    assertDoesNotThrow(() -> loginController.register(credentials));
  }

  @Test
  public void testLoginDoesNotWorkWithExistingAccount() {

    UserCredentials credentials = UserCredentials.builder()
        .username("unknown")
        .password("password")
        .build();

    assertThrows(UnknownUserCredentialsException.class, () -> loginController.login(credentials));
  }

  @Test
  public void testLoginWorksWithExistingAccountAndGoodPassword() {

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    when(moderatorRepository.findById("sample"))
        .thenReturn(Optional.of(new Moderator("sample", "password")));

    assertDoesNotThrow(() -> loginController.login(credentials));
  }

  @Test
  public void testLoginDoesNotWorkWithExistingUsernameAndBadPassword() {

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    when(moderatorRepository.findById("sample"))
        .thenReturn(Optional.of(new Moderator("sample", "another password")));

    assertThrows(UnknownUserCredentialsException.class, () -> loginController.login(credentials));
  }
}
