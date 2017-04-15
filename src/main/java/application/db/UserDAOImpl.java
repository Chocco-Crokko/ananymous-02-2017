package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class UserDAOImpl implements UserDAO{
    private static final String TABLE_NAME = "users";
    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Autowired
    private JdbcTemplate template;

    @Override
    public @Nullable Long add(@NotNull String login, @NotNull String email, @NotNull String password) {
        try {
            final KeyHolder idHolder = new GeneratedKeyHolder();
            final String query = "INSERT INTO users (login, email, password) VALUES (?, ?, ?) RETURNING id";
            template.update(con -> {
                final PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, login);
                pst.setString(2, email);
                pst.setString(3, password);
                return pst;
            }, idHolder);
            return idHolder.getKey().longValue();
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    @Override
    public @Nullable User getUser(@NotNull Long id) {
        final String query = "SELECT * FROM users WHERE id = ?";
        return template.queryForObject(query, USER_ROW_MAPPER, id);
    }

    @Override
    public boolean hasUser(@NotNull Long id) {
        return getUser(id) != null;
    }

    @Override
    public void editUserPassword(@NotNull User user, @NotNull String password) {
        final String query = "UPDATE users SET password = ? WHERE id = ?";
        template.update(query, password, user.getId());
    }

    @Override
    public @Nullable Long getUserID(@NotNull String username) {
        final String query = "SELECT id FROM users WHERE LOWER(login) = LOWER(?) OR LOWER(email) = LOWER(?)";
        return template.queryForObject(query, Long.class, username, username);
    }

    @Override
    public @NotNull List<User> getUsers(int beg, int size) {
        final String query = "SELECT * FROM users LIMIT ? OFFSET ?";
        return template.query(query, USER_ROW_MAPPER, size, beg);
    }

    @Override
    public @NotNull List<User> getUsers() {
        final String query = "SELECT * FROM users";
        return template.query(query, USER_ROW_MAPPER);
    }

    @Override
    public void clear() {
        template.execute("TRUNCATE TABLE users");
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet, rowNum) -> {
        Long id = resultSet.getLong("id");
        String login = resultSet.getString("login");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        return new User(id, login, email, password);
    };
}
