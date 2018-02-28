package application.mechanics.avatar;

import application.mechanics.Config;
import application.mechanics.base.ServerPlayerSnap;
import application.mechanics.base.VictimModel;
import application.mechanics.base.geometry.Coordinates;
import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class GameUser {
    @NotNull
    private final User user;
    @NotNull
    private final Set<VictimModel> victims = new HashSet<>();

    @Nullable
    private Coordinates position;
    private int hp = 100;
    private boolean wasShot;

    private int kills = 0;
    private int deaths = 0;

    public GameUser(@NotNull User user) {
        this.user = user;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    public void setPosition(@NotNull Coordinates coords) {
        this.position = coords;
    }

    @Nullable
    public Coordinates getPosition() {
        return position;
    }

    public long getId() {
        return user.getId();
    }

    public void resetForNextSnap() {
        wasShot = false;
        victims.clear();
        if (hp == 0) {
            setFullHealth();
        }
    }

    @NotNull
    public ServerPlayerSnap generateSnap() {
        final ServerPlayerSnap result = new ServerPlayerSnap(
                getId(),
                position,
                victims,
                hp,
                kills,
                deaths,
                user.getLogin()
        );
        victims.clear();
        return result;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void markShot(double coefficient) {
        wasShot = true;
        hp -= Config.SHOT_REDUCING * coefficient;
        if (hp < 0) {
            hp = 0;
            deaths++;
        }
    }

    public void addVictim(@NotNull VictimModel victim) {
        victims.add(victim);
        kills++;
    }

    public boolean getShot() {
        return wasShot;
    }

    public int getHp() {
        return hp;
    }

    private void setFullHealth() {
        hp = 100;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameUser other = (GameUser) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        final long id = user.getId();
        return (int) (id ^ (id >>> 32));
    }
}

