package application.mechanics.utils;

import application.mechanics.Config;
import application.mechanics.base.Map;
import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;

public class MapHelper {
    public static final double X_OFFSET = (Map.M / 2) * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;
    public static final double Z_OFFSET = (Map.N / 2) * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;

    @NotNull
    public static Index getIndex(@NotNull Coordinates point) {
        final Index index = new Index();
        index.i = (int) ((point.x + MapHelper.X_OFFSET) / Config.BLOCK_SIZE);
        index.j = (int) ((point.z + MapHelper.Z_OFFSET) / Config.BLOCK_SIZE);
        ensureMapBounds(index);
        return index;
    }

    private static void ensureMapBounds(@NotNull Index index) {
        index.i = getIndexInBounds(index.i, Map.M);
        index.j = getIndexInBounds(index.j, Map.N);
    }

    private static int getIndexInBounds(int possibleIndex, int size) {
        if (possibleIndex < 0) {
            return 0;
        } else if (possibleIndex >= size) {
            return size - 1;
        }
        return possibleIndex;
    }
}
