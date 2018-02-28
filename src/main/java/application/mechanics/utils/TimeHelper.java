package application.mechanics.utils;

public final class TimeHelper {
    private TimeHelper() {
    }

    public static void sleep(long millis) {
        if (millis <= 0) {
            return;
        }
        try{
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }
}
