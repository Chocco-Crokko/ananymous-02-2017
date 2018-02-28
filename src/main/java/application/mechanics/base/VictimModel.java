package application.mechanics.base;

import org.jetbrains.annotations.NotNull;

public class VictimModel {
    private long id;
    @NotNull
    private String login;

    // TODO check
    public VictimModel() {
    }

    public VictimModel(long id, @NotNull String login) {
        this.id = id;
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }
}
