package primitives;

/**
 * This class represents a Ray in 3D space, defined by an origin point and a direction vector.
 */
public class Ray {

    private Point origin;  // The origin point of the ray
    private Vector direction;  // The direction vector of the ray

    /**
     * Constructor to initialize the ray with a point and a vector.
     * The vector will be normalized before storing.
     *
     * @param origin    The origin point of the ray
     * @param direction The direction vector of the ray
     */
    public Ray(Point origin, Vector direction) {
        if (origin == null || direction == null) {
            throw new IllegalArgumentException("Origin and direction cannot be null.");
        }
        this.origin = origin;
        this.direction = direction.normalize();  // Normalize the vector to ensure it is a unit vector
    }

    /**
     * Returns the origin of the ray.
     *
     * @return The origin point of the ray
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Returns the direction of the ray.
     *
     * @return The direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Ray[Origin: " + origin + ", Direction: " + direction + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other) {
            return origin.equals(other.origin) && direction.equals(other.direction);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return origin.hashCode() + 31 * direction.hashCode();
    }
}
