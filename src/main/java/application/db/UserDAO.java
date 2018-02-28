package application.db;

import application.models.User;
import application.utils.exceptions.GeneratedKeyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserDAO {
    @NotNull
    Long add(@NotNull String login, @NotNull String email, @NotNull String password, int sScore, int mScore) throws GeneratedKeyException;

    @Nullable
    User getUser(@NotNull Long id);

    void editUser(@NotNull Long id, @Nullable String login, @Nullable String email, @Nullable String password,
                  @Nullable Integer sScore, @Nullable Integer mScore);

    boolean addScore(@Nullable Long id, int sScore, int mScore);

    @Nullable
    Long getUserID(@NotNull String username);

    @NotNull
    List<User> getBestUsers(int beg, int size);

    @NotNull
    List<User> getBestUsers();

    void clear();
}
