package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class UsernameRequest {
    @NotNull
    private final String username;
    @NotNull
    private final String password;

    @JsonCreator
    public UsernameRequest(@JsonProperty("username") @NotNull String username,
                           @JsonProperty("password") @NotNull String password) {
        this.username = username;
        this.password = password;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }
}
