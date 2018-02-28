package application.mechanics.base;

import application.mechanics.base.geometry.Coordinates;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ServerPlayerSnap {
    @NotNull
    private final Set<VictimModel> victims = new HashSet<>();

    @JsonProperty("id")
    private long userId;
    @NotNull
    private String login;
    @Nullable
    private Coordinates position;
    private int hp;
    private int kills;
    private int deaths;

    public ServerPlayerSnap(long id,
                            @Nullable Coordinates position,
                            @NotNull Collection<VictimModel> victims,
                            int hp,
                            int kills,
                            int deaths,
                            @NotNull String login) {
        this.userId = id;
        this.position = position;
        this.hp = hp;
        this.victims.addAll(victims);
        this.kills = kills;
        this.deaths = deaths;
        this.login = login;
    }

    @Nullable
    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(@NotNull Coordinates position) {
        this.position = position;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @NotNull
    public Set<VictimModel> getVictims() {
        return victims;
    }

    public void setVictims(@NotNull Collection<VictimModel> victims) {
        this.victims.addAll(victims);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }
}
