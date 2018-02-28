package application.mechanics.base;

import application.mechanics.avatar.GameUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerSnap {
    @NotNull
    private final List<ServerPlayerSnap> players;
    private final long timeLeft;

    // TODO make this wasShot
    private boolean shot;
    private int hp;
    private int kills;
    private int deaths;

    public ServerSnap(@NotNull List<ServerPlayerSnap> players, long timeLeft) {
        this.players = players;
        this.timeLeft = timeLeft;
    }

    public void setPlayer(@NotNull GameUser player) {
        shot = player.getShot();
        hp = player.getHp();
        kills = player.getKills();
        deaths = player.getDeaths();
    }

    @NotNull
    public List<ServerPlayerSnap> getPlayers() {
        return players;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
}