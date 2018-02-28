package application.mechanics.base.geometry;

import org.jetbrains.annotations.NotNull;

public class Vec3 {
    @NotNull
    private Coordinates coordinates;

    public Vec3(@NotNull Coordinates from, @NotNull Coordinates to) {
        this.coordinates = to.subtract(from);
    }

    public Vec3(double x, double y, double z) {
        coordinates = new Coordinates(x, y, z);
    }

    @NotNull
    public static Vec3 makeNormalized(@NotNull Angles angles) {
        final double cosTheta = Math.cos(angles.theta);
        final double x = -cosTheta * Math.sin(angles.phi);
        final double z = -cosTheta * Math.cos(angles.phi);
        final double y = Math.sin(angles.theta);
        return new Vec3(x, y, z);
    }

    public double cosBetween(@NotNull Vec3 other) {
        final double scalarMult = coordinates.x * other.coordinates.x
                + coordinates.y * other.coordinates.y
                + coordinates.z * other.coordinates.z;
        return scalarMult / (lenght() * other.lenght());
    }

    public double lenght() {
        return Math.sqrt(coordinates.x * coordinates.x + coordinates.y * coordinates.y + coordinates.z * coordinates.z);
    }

    public double horizontalCosBetween(@NotNull Vec3 other) {
        return getXZVec().cosBetween(other.getXZVec());
    }

    public double verticalCosBetween(@NotNull Vec3 other) {
        return getYZVec().cosBetween(other.getYZVec());
    }

    @NotNull
    private Vec3 getXZVec() {
        return new Vec3(coordinates.x, 0, coordinates.z);
    }

    @NotNull
    private Vec3 getYZVec() {
        return new Vec3(0, coordinates.y, coordinates.z);
    }

    public double getX() {
        return coordinates.x;
    }
    public double getY() {
        return coordinates.y;
    }
    public double getZ() {
        return coordinates.z;
    }
}
