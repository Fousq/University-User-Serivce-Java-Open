package kz.zhanbolat.user;

import kz.zhanbolat.user.entity.User;
import kz.zhanbolat.user.exception.AuthenticationException;
import kz.zhanbolat.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestConfiguration.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void givenExistedUserId_whenGetUser_thenReturnUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("test_user");
        expectedUser.setPassword("test_password");
        User user = userService.getUser(1L);

        assertEquals(expectedUser, user);
    }

    @Test
    public void givenNotExistedUserId_whenGetUser_thenThrowException() {
        assertThrows(IllegalStateException.class, () -> userService.getUser(100L));
    }

    @Test
    public void givenNull_whenGetUser_thenThrowException() {
        assertThrows(Exception.class, () -> userService.getUser(null));
    }

    @Test
    public void givenNotExitedUser_whenCreateUser_thenReturnUser() {
        User newUser = new User();
        newUser.setUsername("new-user");
        newUser.setPassword("password");
        User user = userService.createUser(newUser);

        assertNotNull(user.getId());
        assertEquals(newUser.getUsername(), user.getUsername());
        assertEquals(newUser.getPassword(), user.getPassword());
    }

    @Test
    public void givenExistedUser_whenCreateUser_thenThrowException() {
        User existedUser = new User();
        existedUser.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(existedUser));
    }

    @Test
    public void givenUserWithExistedUsername_whenCreateUser_thenThrowException() {
        User user = new User();
        user.setUsername("test_user");

        assertThrows(Exception.class, () -> userService.createUser(user));
    }

    @Test
    public void givenNull_whenCreateUser_thenThrowException() {
        assertThrows(Exception.class, () -> userService.createUser(null));
    }

    @Test
    public void givenEmptyUser_whenCreateUser_thenThrowException() {
        User user = new User();
        user.setPassword("");
        user.setUsername("");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(new User()));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    public void givenExistedUser_whenUpdateUser_thenReturnUser() {
        User exitedUser = userService.getUser(2L);
        // clone of existed user, because it's in persistence context
        User clone = new User();
        clone.setId(exitedUser.getId());
        clone.setUsername(exitedUser.getUsername());
        clone.setPassword(exitedUser.getPassword());

        User user = new User();
        user.setId(2L);
        user.setUsername("test");
        user.setPassword("test");

        User updatedUser = userService.updateUser(user);

        assertEquals(clone.getId(), updatedUser.getId());
        assertNotEquals(clone.getUsername(), updatedUser.getUsername());
        assertNotEquals(clone.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void givenNull_whenUpdateUser_thenThrowException() {
        assertThrows(Exception.class, () -> userService.updateUser(null));
    }

    @Test
    public void givenNotExistedUser_whenUpdateUser_thenThrowException() {
        User user = new User();
        user.setPassword("test");
        user.setUsername("test");

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user));
    }

    @Test
    public void givenEmptyUser_whenUpdateUser_thenThrowException() {
        User emptyUsername = new User();
        emptyUsername.setId(2L);
        emptyUsername.setUsername("");
        emptyUsername.setPassword("test");

        User nullUsername = new User();
        nullUsername.setId(2L);
        nullUsername.setPassword("test");

        User nullId = new User();
        nullId.setUsername("test");
        nullId.setPassword("test");

        User emptyPassword = new User();
        emptyPassword.setId(2L);
        emptyPassword.setUsername("test");
        emptyPassword.setPassword("");

        User nullPassword = new User();
        nullPassword.setId(2L);
        nullPassword.setUsername("test");

        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(new User()));
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(emptyUsername));
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(nullId));
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(nullUsername));
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(emptyPassword));
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(nullPassword));
        });
    }

    @Test
    public void givenExistedUsernameAndPassword_whenAuthenticateUser_thenReturnUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("test_user");
        expectedUser.setPassword("test_password");

        User user = userService.authenticateUser("test_user", "test_password");

        assertEquals(expectedUser, user);
    }

    @Test
    public void givenNullOrEmpty_whenAuthenticateUser_thenThrowException() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser(null, null));
            assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser("", null));
            assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser(null, ""));
            assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser("", ""));
        });
    }

    @Test
    public void givenNotExistedUsernameOrPassword_whenAuthenticateUser_thenThrowException() {
        assertAll(() -> {
            assertThrows(AuthenticationException.class, () -> userService.authenticateUser("not_existing", "not_existing"));
            assertThrows(AuthenticationException.class, () -> userService.authenticateUser("test_user", "not_existing"));
            assertThrows(AuthenticationException.class, () -> userService.authenticateUser("not_existing", "test_password"));
        });
    }
}
