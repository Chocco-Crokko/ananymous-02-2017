package application.mechanics;

import application.mechanics.avatar.GameUser;
import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    @NotNull
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private final Long sessionId;
    @NotNull
    private final Set<GameUser> players = new HashSet<>();

    private final long startTime;

    public GameSession() {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        startTime = System.nanoTime();
    }

    public boolean isFull() {
        return players.size() >= Config.MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void addPlayer(@NotNull User player) {
        if (players.size() >= Config.MAX_PLAYERS) {
            throw new RuntimeException("No more players for this session!");
        }
        players.add(new GameUser(player));
    }

    public void removePlayer(long userId) {
        for (GameUser player: players) {
            if (player.getId() == userId) {
                players.remove(player);
                break;
            }
        }
    }

    public long getRemainingSeconds(long nanos) {
        long diff = Config.SESSION_TIME_NANOS - (nanos - startTime);
        if (diff < 0) {
            diff = 0;
        }
        return TimeUnit.NANOSECONDS.toSeconds(diff);
    }

    @NotNull
    public Set<GameUser> getPlayers() {
        return players;
    }

    public long getId() {
        return sessionId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession other = (GameSession) o;

        return sessionId.equals(other.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}
