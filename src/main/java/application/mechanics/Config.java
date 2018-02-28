package application.mechanics;

import java.util.concurrent.TimeUnit;

public class Config {
    public static final long STEP_TIME = 30;
    public static final int THREADS_NUM = Runtime.getRuntime().availableProcessors();

    public static final int SHOT_REDUCING = 20;

    public static final int BLOCK_HEIGHT = 250 / 3 + 40;
    public static final int BLOCK_SIZE = 250;

    public static final int MAX_PLAYERS = 5;

    public static final int PLAYER_SIZE = 40;
    public static final int RADIUS = PLAYER_SIZE / 2;
    public static final int SCORES_FOR_SHOT = 2;
    public static final int SCORES_FOR_KILL = 10;
    public static final double DAMAGE_COEFF_MIN = 0.5;

    public static final long SESSION_TIME_NANOS = TimeUnit.MINUTES.toNanos(10);
}
