package application.utils.responses;

import application.models.User;
import org.jetbrains.annotations.NotNull;

public class FullUserResponse {
    private final long id;
    @NotNull
    private final String login;
    @NotNull
    private final String email;
    private final int sScore;
    private final int mScore;

    public FullUserResponse(@NotNull User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.sScore = user.getsScore();
        this.mScore = user.getmScore();
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

    public int getsScore() {
        return sScore;
    }

    public int getmScore() {
        return mScore;
    }
}
