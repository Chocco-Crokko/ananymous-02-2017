package application.models;

public class User {
    private final long id;
    private final String login;
    private final String email;
    private final String password;
    private final int sScore;
    private final int mScore;

    public User(long id, String login, String email, String password, int sScore, int mScore) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.sScore = sScore;
        this.mScore = mScore;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getsScore() {
        return sScore;
    }

    public int getmScore() {
        return mScore;
    }
}
