package application.mechanics.base.geometry;

@SuppressWarnings("PublicField")
public class Angles {
    // angle from oZ axes in XZ plane
    public double phi;
    // angle from oY axes in YZ (YX?) plane
    public double theta;

    // TODO check for needed
    public Angles() {
    }

    public Angles(double phi, double theta) {
        this.phi = phi;
        this.theta = theta;
    }

    @Override
    public String toString() {
        return "{phi:" + phi + ",theta:" + theta + '}';
    }
}
