package primitives;
import geometries.Intersectable;
import primitives.Vector;
import java.util.List;
import geometries.Intersectable.Intersection;

/**
 * This class represents a Ray in 3D space, which is defined by an origin point and a direction vector.
 * The Ray is used to represent an infinite line starting from a point in a specified direction.
 */
public class Ray {
    private static final double DELTA = 0.1;


    /**
     * The origin point of the ray (where the ray starts).
     */
    private final Point origin;

    /**
     * The direction vector of the ray (specifies the direction of the ray).
     */
    private final Vector direction;

    /**
     * Constructor to initialize a ray with a specified origin and direction.
     * The direction vector will be normalized to ensure it is a unit vector before storing it.
     *
     * @param origin    The origin point of the ray (the starting point).
     * @param direction The direction vector of the ray (the direction in which the ray travels).
     * @throws IllegalArgumentException If either the origin or direction is null.
     * @throws ArithmeticException If the direction vector has a zero length, which cannot be normalized.
     */
    public Ray(Point origin, Vector direction) {
        if (origin == null || direction == null) {
            throw new IllegalArgumentException("Origin and direction cannot be null.");
        }
        this.origin = origin;
        this.direction = direction.normalize();  // Normalize the vector to ensure it is a unit vector
    }
    public Ray(Point point, Vector direction, Vector normal) {
        // הזזה קטנה לאורך הנורמל כדי למנוע חיתוך עצמי
        double sign = direction.dotProduct(normal) > 0 ? DELTA : -DELTA;
        Vector delta = normal.scale(sign);
        this.origin = point.add(delta);
        this.direction = direction.normalize();
    }


    /**
     * Returns the origin of the ray.
     *
     * @return The origin point of the ray.
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Returns the direction of the ray.
     *
     * @return The direction vector of the ray.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns a string representation of the ray, including the origin and direction.
     *
     * @return A string describing the ray's origin and direction.
     */
    @Override
    public String toString() {
        return "Ray[Origin: " + origin + ", Direction: " + direction + "]";
    }

    /**
     * Checks whether two rays are equal. Two rays are considered equal if they have the same origin and direction.
     *
     * @param obj The object to compare to this ray.
     * @return true if the two rays are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Same object reference, return true.
        if (obj instanceof Ray other) {
            return origin.equals(other.origin) && direction.equals(other.direction);  // Compare both origin and direction.
        }
        return false;
    }

    /**
     * Computes a hash code for the ray, which is based on the hash codes of its origin and direction.
     *
     * @return A hash code value for the ray.
     */
    @Override
    public int hashCode() {
        return origin.hashCode() + 31 * direction.hashCode();  // Standard way of combining hash codes.
    }

    /**
     * Returns a point on the ray at a given parameter t.
     *
     * @param t The parameter along the ray (where t = 0 is the origin, and positive t values are along the direction).
     * @return The point on the ray corresponding to the parameter t.
     */
    public Point getPoint(double t) {
        if (t == 0 || Math.abs(t) < 1e-6 || Double.isNaN(t) || Double.isInfinite(t)) {
            Vector scaled = direction.scale(t);
            return origin.add(scaled);
        }
        return origin.add(direction.scale(t));
    }
    public Point getHead() {
        return origin;
    }

    /**
     * Finds the closest intersection from the origin of the ray among a list of intersections.
     *
     * @param intersections list of intersections to check.
     * @return the closest intersection or null if the list is null or empty.
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        Intersection closest = intersections.get(0);
        double minDistance = origin.distance(closest.point);

        for (Intersection inter : intersections) {
            double dist = origin.distance(inter.point);
            if (dist < minDistance) {
                closest = inter;
                minDistance = dist;
            }
        }
        return closest;
    }

    /**
     * Finds the closest point to the ray's origin from a list of points.
     *
     * This method converts each point into an {@link geometries.Intersectable.Intersection} with a null geometry,
     * and uses {@link #findClosestIntersection(List)} to find the closest one.
     *
     * @param points The list of points to check.
     * @return The point closest to the ray's origin, or null if the list is null or empty.
     */
    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty()) return null;
        Intersection closestIntersection = findClosestIntersection(
                points.stream().map(p -> new Intersection(null, p)).toList());
        return closestIntersection == null ? null : closestIntersection.point;
    }
}
