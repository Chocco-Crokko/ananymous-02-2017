package application.mechanics.base;

import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnnecessaryLocalVariable", "OverlyComplexBooleanExpression"})
public class Block {
    @NotNull
    private Coordinates corner;
    private double xLength;
    private double yLength;
    private double zLength;

    public Block(@NotNull Coordinates corner, double xLength, double yLength, double zLength) {
        this.corner = corner;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }

    private double getClosestPlane(double first, double second, double point) {
        if (Math.abs(point - first) < Math.abs(point - second)) {
            return first;
        } else {
            return second;
        }
    }

    private double getYPlane() {
        return corner.y + yLength;
    }

    @Nullable
    private Coordinates getIntersectionPointWithX(@NotNull Coordinates from, @NotNull Coordinates to) {
        final double planeX = getClosestPlane(corner.x + xLength, corner.x, from.x);

        final double dx = (to.x - from.x);
        final double dy = (to.y - from.y);
        final double dz = (to.z - from.z);

        final double t = (planeX - from.x) / dx;

        final double x = planeX;
        final double y = dy * t + from.y;
        final double z = dz * t + from.z;

        if (dx >= 0 && x < from.x || dx < 0 && x > from.x) {
            return null;
        }

        return new Coordinates(x, y, z);
    }

    @NotNull
    private Coordinates getIntersectionPointWithY(@NotNull Coordinates from, @NotNull Coordinates to) {
        final double planeY = getYPlane();

        final double dx = (to.x - from.x);
        final double dy = (to.y - from.y);
        final double dz = (to.z - from.z);

        final double t = (planeY - from.y) / dy;

        final double y = planeY;
        final double x = dx * t + from.x;
        final double z = dz * t + from.z;
        return new Coordinates(x, y, z);
    }

    @Nullable
    private Coordinates getIntersectionPointWithZ(@NotNull Coordinates from, @NotNull Coordinates to) {
        final double planeZ = getClosestPlane(corner.z + zLength, corner.z, from.z);

        final double dx = (to.x - from.x);
        final double dy = (to.y - from.y);
        final double dz = (to.z - from.z);

        final double t = (planeZ - from.z) / dz;

        final double z = planeZ;
        final double y = dy * t + from.y;
        final double x = dx * t + from.x;

        if (dz >= 0 && z < from.z || dz < 0 && z > from.z) {
            return null;
        }

        return new Coordinates(x, y, z);
    }

    private boolean isBlockContains(@Nullable Coordinates point) {
        if (point == null) {
            return false;
        }
        final boolean isXInside = point.x >= corner.x && point.x <= corner.x + xLength;
        final boolean isYInside = point.y >= corner.y && point.y <= corner.y + yLength;
        final boolean isZInside = point.z >= corner.z && point.z <= corner.z + zLength;
        return isXInside && isYInside && isZInside;
    }

    public boolean isBetween(@NotNull Coordinates from, @NotNull Coordinates to) {
        final Coordinates intersectionX = getIntersectionPointWithX(from, to);
        final Coordinates intersectionY = getIntersectionPointWithY(from, to);
        final Coordinates intersectionZ = getIntersectionPointWithZ(from, to);
        final boolean isContainX = isBlockContains(intersectionX);
        final boolean isContainY = isBlockContains(intersectionY);
        final boolean isContainZ = isBlockContains(intersectionZ);

        return isContainX || isContainY || isContainZ;
    }
}
