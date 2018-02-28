package application.services;

import application.models.User;
import application.utils.exceptions.GeneratedKeyException;
import application.utils.requests.UserRequest;
import application.utils.responses.FullUserResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountServiceTest {
    private static final String PASSWORD = "qwerty123";
    private static final Long INVALID_ID = Long.MAX_VALUE;
    private static final int INVALID_PAGE = Integer.MAX_VALUE;

    @Autowired
    private AccountService accountService;

    @NotNull
    private UserRequest getDefaultUserRequest() {
        return new UserRequest("test", "test@mail.ru", PASSWORD);
    }

    private @Nullable Long addUser(@NotNull String login, @NotNull String email, @NotNull String password) {
        try {
            return accountService.addUser(new UserRequest(login, email, password));
        } catch (GeneratedKeyException e) {
            fail();
            return null;
        }
    }

    private @Nullable Long addDefaultUser() {
        try {
            return accountService.addUser(getDefaultUserRequest());
        } catch (GeneratedKeyException e) {
            fail();
            return null;
        }
    }

    @Test
    public void testUsersEmpty() {
        assertTrue(accountService.getBestUsers().isEmpty());
    }

    @Test
    public void testUsersNotEmpty() {
        addDefaultUser();
        assertFalse(accountService.getBestUsers().isEmpty());
    }

    @Test
    public void testConflict() {
        addDefaultUser();
        try {
            addDefaultUser();
            fail();
        } catch (DuplicateKeyException ignore) {
        }
    }

    @Test
    public void testAddUser() {
        final Long id = addDefaultUser();
        assertNotNull(id);
    }

    @Test
    public void testGetNonExistingUser() {
        assertNull(accountService.getUser(0L));
        assertNull(accountService.getUser(INVALID_ID));
    }

    @Test
    public void testAddedAndGettedUserMatches() {
        final String login = "test123";
        final String email = "test123@mail.ru";
        final Long id = addUser(login, email, PASSWORD);
        assertNotNull(id);
        final User user = accountService.getUser(id);
        assertNotNull(user);
        assertEquals(login, user.getLogin());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetNonExistingUsername() {
        assertNull(accountService.getUserID("not_exist"));
    }

    @Test
    public void testGetUserByUsername() {
        final Long id = addDefaultUser();
        assertEquals(id, accountService.getUserID(getDefaultUserRequest().getLogin()));
        assertEquals(id, accountService.getUserID(getDefaultUserRequest().getEmail()));
    }

    @Test
    public void testCheckUserAccount() {
        final String login = "ulogin";
        final String email = "uemail@mail.ru";
        final String rawPassword = "rawPassword";
        final Long id = addUser(login, email, rawPassword);
        assertNotNull(id);
        final User user = accountService.getUser(id);
        assertNotNull(user);
        assertFalse(accountService.checkUserAccount(id, user.getPassword()));
        assertTrue(accountService.checkUserAccount(id, rawPassword));

        assertFalse(accountService.checkUserAccount(INVALID_ID, user.getPassword()));
    }

    @Test
    public void testChangePassword() {
        final String login = "ulogin";
        final String email = "uemail@mail.ru";
        final String rawPassword = "rawPassword";
        final String newPassword = "newPassword";
        final Long id = addUser(login, email, rawPassword);
        assertNotNull(id);
        final User user = accountService.getUser(id);
        assertNotNull(user);

        assertTrue(accountService.changePassword(user, rawPassword, newPassword));

        assertTrue(accountService.checkUserAccount(id, newPassword));
    }

    private void addNUsers(int count) {
        for (int i = 0; i < count; i++) {
            addUser("login" + i, "email" + i + "@mail.ru", PASSWORD);
        }
    }

    @Test
    public void testGetUsers() {
        final int count = 5;
        addNUsers(count);
        final List<FullUserResponse> users = accountService.getBestUsers();
        assertEquals(count, users.size());
    }

    @Test
    public void testGetUsersByPage() {
        final int count = 10;
        addNUsers(count);
        assertEquals(accountService.getBestUsers().size(), accountService.getBestUsers(0, Integer.MAX_VALUE).size());
        assertEquals(count, accountService.getBestUsers(0, count).size());

        final int usersOnPage = 3;
        assertEquals(0, accountService.getBestUsers(count, usersOnPage).size());
        assertEquals(1, accountService.getBestUsers(count - 1, usersOnPage).size());
    }

    @Test
    public void testGetUsersByInvalidPage() {
        assertTrue(accountService.getBestUsers(0, -1).isEmpty());
        assertTrue(accountService.getBestUsers(INVALID_PAGE, 1).isEmpty());
    }

    @Test
    public void testAddScore() {
        final Long id = addDefaultUser();
        User user = accountService.getUser(id);
        assertEquals(0, user.getsScore());
        assertEquals(0, user.getmScore());

        accountService.addScore(id, 10, 0);
        user = accountService.getUser(id);
        assertEquals(10, user.getsScore());
        assertEquals(0, user.getmScore());

        accountService.addScore(id, -5, 5);
        user = accountService.getUser(id);
        assertEquals(10, user.getsScore());
        assertEquals(5, user.getmScore());

        accountService.addScore(id, 8, -1);
        user = accountService.getUser(id);
        assertEquals(18, user.getsScore());
        assertEquals(5, user.getmScore());

        accountService.addScore(id, 100, 200);
        user = accountService.getUser(id);
        assertEquals(118, user.getsScore());
        assertEquals(205, user.getmScore());
    }
}
