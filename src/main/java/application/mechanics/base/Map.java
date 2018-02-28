package application.mechanics.base;

@SuppressWarnings({"ClassNamingConvention", "PublicField", "StaticVariableNamingConvention", "ConstantNamingConvention"})
public final class Map {
    public static final int[][] MAP = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 0, 0, 0, 0, 0, 1, 1, 1},
        {1, 1, 0, 0, 2, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 2, 0, 0, 0, 1},
        {1, 0, 0, 2, 0, 0, 2, 0, 0, 1},
        {1, 0, 0, 0, 2, 0, 0, 0, 1, 1},
        {1, 1, 1, 0, 0, 0, 0, 1, 1, 1},
        {1, 1, 1, 0, 0, 1, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 1, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    public static final int M = MAP.length;
    public static final int N = MAP[0].length;
}
