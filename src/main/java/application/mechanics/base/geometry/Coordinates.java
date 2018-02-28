package application.mechanics.base.geometry;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"InstanceVariableNamingConvention", "PublicField"})
public class Coordinates {
    public double x;
    public double y;
    public double z;

    // TODO check for needed
    public Coordinates() {
    }

    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public Coordinates add(@NotNull Coordinates addition) {
        return new Coordinates(x + addition.x, y + addition.y, z + addition.z);
    }

    @NotNull
    public Coordinates subtract(@NotNull Coordinates subtractor) {
        return new Coordinates(x - subtractor.x, y - subtractor.y, z - subtractor.z);
    }

    @Override
    public String toString() {
        return "{x:" + x + ",y:" + y + ",z:" + z + '}';
    }
}
