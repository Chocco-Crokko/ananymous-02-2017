package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class UserRequest {
    @NotNull
    private final String login;
    @NotNull
    private final String email;
    @NotNull
    private final String password;

    @JsonCreator
    public UserRequest(@JsonProperty("login") @NotNull String login,
                       @JsonProperty("email") @NotNull String email,
                       @JsonProperty("password") @NotNull String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }
}
