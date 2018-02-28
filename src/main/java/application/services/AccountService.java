package application.services;

import application.db.UserDAO;
import application.models.User;
import application.utils.exceptions.GeneratedKeyException;
import application.utils.requests.UserRequest;
import application.utils.responses.FullUserResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService{
    private final UserDAO db;
    private final PasswordEncoder encoder;

    public AccountService(@NotNull UserDAO db, @NotNull PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    public @Nullable User getUser(@NotNull Long id) {
        return db.getUser(id);
    }

    public boolean checkUserAccount(@NotNull Long id, @NotNull String password) {
        final User user = getUser(id);
        return user != null && doCheckPassword(user, password);
    }

    public @NotNull Long addUser(@NotNull UserRequest user) throws GeneratedKeyException {
        final String encodedPassword = encoder.encode(user.getPassword());
        return db.add(user.getLogin(), user.getEmail(), encodedPassword, 0, 0);
    }

    public boolean changePassword(@NotNull User user, @NotNull String oldPassword, @NotNull String newPassword) {
        if (!doCheckPassword(user, oldPassword)) {
            return false;
        }
        final String encodedNewPassword = encoder.encode(newPassword);
        db.editUser(user.getId(), null, null, encodedNewPassword, null, null);
        return true;
    }

    public @Nullable Long getUserID(@NotNull String username) {
        return db.getUserID(username);
    }

    private boolean doCheckPassword(@NotNull User user, @NotNull String password) {
        return encoder.matches(password, user.getPassword());
    }

    public List<FullUserResponse> getBestUsers(int beg, int size) {
        if (size <= 0) {
            return Collections.emptyList();
        }
        return convertUser(db.getBestUsers(beg, size));
    }

    public List<FullUserResponse> getBestUsers() {
        return convertUser(db.getBestUsers());
    }

    private List<FullUserResponse> convertUser(List<User> users) {
        return users.stream().map(FullUserResponse::new)
                .collect(Collectors.toList());
    }

    public void clear() {
        db.clear();
    }

    public boolean addScore(@Nullable Long id, int sScore, int mScore) {
        if (sScore < 0) {
            sScore = 0;
        }
        if (mScore < 0) {
            mScore = 0;
        }
        return db.addScore(id, sScore, mScore);
    }
}