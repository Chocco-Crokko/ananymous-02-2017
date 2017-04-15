package application.utils.responses;

import javax.validation.constraints.NotNull;

public class FullUserResponse {
    private final long id;
    @NotNull
    private final String login;
    @NotNull
    private final String email;

    public FullUserResponse(long id, @NotNull String login, @NotNull String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getEmail() {
        return email;
    }
}
