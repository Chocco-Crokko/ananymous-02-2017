package application.utils;

import application.utils.requests.UserRequest;
import application.utils.requests.UsernameRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidatorTest {
    @Test
    public void testWrongEmail() {
        Assert.assertEquals(Validator.checkEmail(""), Validator.EMPTY_EMAIL);
        Assert.assertEquals(Validator.checkEmail("123456789012345678901234567890123456789012345678901234567890@mail.ru"), Validator.EMAIL_MAX_LENGHT_ERROR);
        Assert.assertEquals(Validator.checkEmail("user@mail.rurururu"), Validator.WRONG_EMAIL_SCHEME);
        Assert.assertEquals(Validator.checkEmail("usermail.ru"), Validator.WRONG_EMAIL_SCHEME);
    }

    @Test
    public void testWrongLogin() {
        Assert.assertEquals(Validator.checkLogin(""), Validator.EMPTY_LOGIN);
        Assert.assertEquals(Validator.checkLogin("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"),
                Validator.LOGIN_MAX_LENGHT_ERROR);
        Assert.assertEquals(Validator.checkLogin("qw"), Validator.WRONG_LOGIN_SCHEME);
        Assert.assertEquals(Validator.checkLogin("sdh©"), Validator.WRONG_LOGIN_SCHEME);
    }

    @Test
    public void testWrongPassword() {
        Assert.assertEquals(Validator.checkPassword(""), Validator.EMPTY_PASSWORD);
        Assert.assertEquals(Validator.checkPassword("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789000"),
                Validator.PASSWORD_MAX_LENGHT_ERROR);
        Assert.assertEquals(Validator.checkPassword("qwerty"), Validator.PASSWORD_MIN_LENGHT_ERROR);
    }

    @Test
    public void testWrongUsername() {
        Assert.assertEquals(Validator.checkUsername(""), Validator.EMPTY_USERNAME);
        Assert.assertEquals(Validator.checkUsername("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"), Validator.USERNAME_MAX_LENGHT_ERROR);
    }

    @Test
    public void testCorrectUserRequest() {
        Assert.assertNull(Validator.getUserError(new UserRequest("user", "user@mail.ru", "qwerty1234")));
    }

    @Test
    public void testCorrectUsernameRequest() {
        Assert.assertNull(Validator.getUserRequestError(new UsernameRequest("user", "qwerty1234")));
        Assert.assertNull(Validator.getUserRequestError(new UsernameRequest("user@mail.ru", "qwerty1234")));
    }
}
