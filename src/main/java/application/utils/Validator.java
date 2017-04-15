package application.utils;

import application.utils.requests.UserRequest;
import application.utils.requests.UsernameRequest;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final int LOGIN_MAX_SIZE = 50;
    private static final int EMAIL_MAX_SIZE = 50;
    private static final int PASSWORD_MAX_SIZE = 200;
    private static final int PASSWORD_MIN_SIZE = 8;

    public static final String LOGIN_MAX_LENGHT_ERROR = "Max lenght of login is " + LOGIN_MAX_SIZE;
    public static final String EMAIL_MAX_LENGHT_ERROR = "Max lenght of email is " + EMAIL_MAX_SIZE;
    public static final String USERNAME_MAX_LENGHT_ERROR = "Max lenght of username is " + LOGIN_MAX_SIZE;
    public static final String PASSWORD_MAX_LENGHT_ERROR = "Max lenght of password is " + EMAIL_MAX_SIZE;
    public static final String PASSWORD_MIN_LENGHT_ERROR = "Password must be at least " + PASSWORD_MIN_SIZE + " symbols";
    public static final String EMPTY_LOGIN = "Login is empty";
    public static final String EMPTY_USERNAME = "Username is empty";
    public static final String EMPTY_EMAIL = "Email is empty";
    public static final String EMPTY_PASSWORD = "Password is empty";
    public static final String WRONG_EMAIL_SCHEME = "Wrong email scheme";
    public static final String WRONG_LOGIN_SCHEME = "Login must contain only letters, digits, '_' and '-' and contain at least 3 symbols";

    public static @Nullable String checkEmail(@NotNull String email) {
        if (StringUtils.isEmpty(email)) {
            return EMPTY_EMAIL;
        } else if (email.length() > EMAIL_MAX_SIZE) {
            return EMAIL_MAX_LENGHT_ERROR;
        } else if (!doValidate(email, "^([a-zA-Z0-9_\\.-]+)@([\\da-zA-Z\\.-]+)\\.([a-z\\.]{2,6})$")) {
            return WRONG_EMAIL_SCHEME;
        }

        return null;
    }

    public static @Nullable String checkLogin(@NotNull String login) {
        if (StringUtils.isEmpty(login)) {
            return EMPTY_LOGIN;
        } else if (login.length() > LOGIN_MAX_SIZE) {
            return LOGIN_MAX_LENGHT_ERROR;
        } else if (!doValidate(login, "^[a-zA-Z0-9_-]{3,}$")) {
            return WRONG_LOGIN_SCHEME;
        }

        return null;
    }

    public static @Nullable String checkPassword(@NotNull String password) {
        if (StringUtils.isEmpty(password)) {
            return EMPTY_PASSWORD;
        } else if (password.length() > PASSWORD_MAX_SIZE) {
            return PASSWORD_MAX_LENGHT_ERROR;
        } else if (password.length() < 8) {
            return PASSWORD_MIN_LENGHT_ERROR;
        }

        return null;
    }

    public static @Nullable String checkUsername(@NotNull String username) {
        if (StringUtils.isEmpty(username)) {
            return EMPTY_USERNAME;
        } else if (username.length() > LOGIN_MAX_SIZE) {
            return USERNAME_MAX_LENGHT_ERROR;
        }

        return null;
    }

    public static @Nullable String getUserError(@NotNull UserRequest user) {
        String error;
        if ((error = checkLogin(user.getLogin())) != null) {
            return error;
        }

        if ((error = checkEmail(user.getEmail())) != null) {
            return error;
        }

        if ((error = checkPassword(user.getPassword())) != null) {
            return error;
        }

        return null;
    }

    public static @Nullable String getUserRequestError(@NotNull UsernameRequest user) {
        String error;
        if ((error = checkUsername(user.getUsername())) != null) {
            return error;
        }

        if ((error = checkPassword(user.getPassword())) != null) {
            return error;
        }

        return null;
    }

    private static boolean doValidate(String str, String pattern) {
        if (str == null) {
            return false;
        }
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(str);
        return m.matches();
    }
}
