package application.mechanics.internal;

import application.mechanics.Config;
import application.mechanics.GameSession;
import application.mechanics.avatar.GameUser;
import application.mechanics.base.UserSnap;
import application.mechanics.base.VictimModel;
import application.mechanics.base.geometry.Angles;
import application.mechanics.base.geometry.Coordinates;
import application.mechanics.base.geometry.Vec3;
import application.services.AccountService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientSnapService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnapService.class.getSimpleName());

    @NotNull
    private final Map<Long, List<UserSnap>> userToSnaps = new HashMap<>();

    private double damageCoeff;
    @NotNull
    private AccountService accountService;
    @NotNull
    private BlockService blockService;

    public ClientSnapService(@NotNull AccountService accountService,
                             @NotNull BlockService blockService) {
        this.accountService = accountService;
        this.blockService = blockService;
    }

    public synchronized void pushClientSnap(long user, @NotNull UserSnap snap) {
        final List<UserSnap> userSnaps = userToSnaps.computeIfAbsent(user, u -> new ArrayList<>());
        userSnaps.add(snap);
    }

    @Nullable
    public synchronized List<UserSnap> getSnapsForUser(long user) {
        return userToSnaps.get(user);
    }

    public void processSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        for (GameUser player : players) {
            final List<UserSnap> playerSnaps = getSnapsForUser(player.getId());
            if (playerSnaps == null || playerSnaps.isEmpty()) {
                continue;
            }
            final UserSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
            player.setPosition(lastSnap.getPosition());

            for (UserSnap snap: playerSnaps) {
                if (!snap.isShooting()) {
                    continue;
                }
                final GameUser victim = processShooting(snap, players);
                if (victim != null) {
                    accountService.addScore(player.getId(), 0, Config.SCORES_FOR_SHOT);
                    victim.markShot(damageCoeff);
                    if (!victim.isAlive()) {
                        final VictimModel model =
                                new VictimModel(victim.getId(), victim.getUser().getLogin());
                        player.addVictim(model);
                        accountService.addScore(player.getId(), 0, Config.SCORES_FOR_KILL);
                    }
                }
            }
        }
    }

    @Nullable
    private GameUser processShooting(UserSnap snap, Iterable<GameUser> players) {
        final Coordinates userPosition = snap.getPosition();
        final Angles camera = snap.getCamera();
        final Vec3 normalizedDirection = Vec3.makeNormalized(camera);
        
        for (GameUser enemy: players) {
            if (enemy.getId() == snap.getId()) {
                continue;
            }
            final Coordinates enemyPosition = enemy.getPosition();
            if (enemyPosition == null) {
                continue;
            }

            final Vec3 userToEnemy = new Vec3(userPosition, enemyPosition);

            final double distance = userToEnemy.lenght();
            final double cosThreshold = distance / Math.hypot(distance, Config.RADIUS);

//            final double horizontalCos = normalizedDirection.horizontalCosBetween(userToEnemy);
//            final double verticalCos = normalizedDirection.verticalCosBetween(userToEnemy);
            final double cos = normalizedDirection.cosBetween(userToEnemy);

//            if (horizontalCos >= cosThreshold && verticalCos >= cosThreshold) {
            if (cos >= cosThreshold) {
                if (isWallsOnDistance(userPosition, normalizedDirection, distance)) {
                    continue;
                }
                final double hypotenuse = distance / cos;
                final double distanceFromEnemyCenter =
                        Math.sqrt(hypotenuse * hypotenuse - distance * distance);

                damageCoeff = (Config.RADIUS - distanceFromEnemyCenter) / Config.RADIUS;
                if (damageCoeff < Config.DAMAGE_COEFF_MIN) {
                    damageCoeff = Config.DAMAGE_COEFF_MIN;
                }
                return enemy;
            }
        }
        return null;
    }

    private boolean isWallsOnDistance(@NotNull Coordinates from, @NotNull Vec3 normalizedDirection, double distance) {
        final Coordinates to = new Coordinates(
                from.x + distance * normalizedDirection.getX(),
                from.y + distance * normalizedDirection.getY(),
                from.z + distance * normalizedDirection.getZ()
        );
        LOGGER.info("to {}", to.toString());
        return blockService.isWallsBetween(from, to);
    }

    public void clear() {
        userToSnaps.clear();
    }
}

