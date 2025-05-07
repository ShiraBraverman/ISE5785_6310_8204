package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Sphere class represents a 3D sphere, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center of the sphere
     */
    private final Point center;

    /**
     * Constructor that initializes a sphere with a center point and a radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius); // Call the constructor of RadialGeometry to initialize the radius
        this.center = center;
    }

    /**
     * Returns the normal vector of the sphere at a given point.
     * The normal is the vector from the center of the sphere to the given point,
     * normalized to unit length.
     *
     * @param point The point on the surface of the sphere
     * @return The normal vector from the center of the sphere to the given point
     */
    @Override
    public Vector getNormal(Point point) {
        // The normal to the sphere is the vector from the center to the point, normalized
        return point.subtract(center).normalize();
    }

    /**
     * Finds the intersection points of the sphere with a given ray.
     *
     * @param ray The ray to check for intersections.
     * @return A list of points where the ray intersects the sphere, or an empty list if no intersection.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = new ArrayList<>();

        // The vector from the ray's origin to the sphere's center
        Vector L = center.subtract(ray.getOrigin());

        // The projection of L onto the ray's direction
        double tca = L.dotProduct(ray.getDirection());

        // Calculate the distance from the center to the ray's path
        double d2 = L.lengthSquared() - tca * tca;

        // If the ray does not intersect the sphere, return an empty list
        if (d2 > radius * radius) {
            return intersections;
        }

        // Calculate the distance from the projection to the intersection point(s)
        double thc = Math.sqrt(radius * radius - d2);

        // Calculate the two intersection points
        double t0 = tca - thc;
        double t1 = tca + thc;

        // If t0 or t1 is positive, the ray intersects the sphere
        if (t0 > 0) {
            intersections.add(ray.getPoint(t0));
        }
        if (t1 > 0) {
            intersections.add(ray.getPoint(t1));
        }

        return intersections;
    }
}
