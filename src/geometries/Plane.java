package geometries;

import primitives.Double3;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Plane class represents a 3D plane defined either by three points or by a point and a normal vector.
 * The plane is characterized by a point lying on it and a perpendicular normal vector.
 */
public class Plane extends Geometry {

    /**
     * A point that lies on the plane.
     */
    private final Point point;

    /**
     * The normalized normal vector perpendicular to the plane surface.
     */
    private final Vector normal;

    /**
     * Constructor that creates a plane from three non-collinear points.
     * It computes the normal vector as the normalized cross product of two vectors
     * formed by the points.
     *
     * @param p1 The first point defining the plane.
     * @param p2 The second point defining the plane.
     * @param p3 The third point defining the plane.
     * @throws IllegalArgumentException If the three points are collinear (do not define a plane).
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        Vector normalUnnormalized = v1.crossProduct(v2);

        if (normalUnnormalized.length() == 0) {
            throw new IllegalArgumentException("The three points are collinear and cannot define a plane.");
        }

        this.point = p1;
        this.normal = normalUnnormalized.normalize();
    }

    /**
     * Constructor that creates a plane from a point and a normal vector.
     * The normal vector will be normalized internally.
     *
     * @param point  A point lying on the plane.
     * @param normal A vector perpendicular to the plane (not necessarily normalized).
     * @throws IllegalArgumentException If the normal vector is a zero vector.
     */
    public Plane(Point point, Vector normal) {
        if (normal.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Normal vector cannot be a zero vector.");
        }

        this.point = point;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normalized normal vector to the plane at any given point.
     * Since the plane is flat, the normal vector is constant everywhere.
     *
     * @param point The point on the plane (ignored because normal is constant).
     * @return The normalized normal vector of the plane.
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * Returns a point on the plane.
     *
     * @return The reference point of the plane.
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Finds the intersection point(s) of the plane with a given ray.
     * <p>
     * The method calculates the parameter t for the ray equation and returns
     * the intersection point if it lies in front of the ray origin.
     * If the ray is parallel to the plane or the intersection is behind the ray origin, it returns null.
     *
     * @param ray The ray to test for intersection with the plane.
     * @return A list containing the intersection point if exists, or null if no intersection.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector normal = this.normal;
        Point rayOrigin = ray.getOrigin();
        Vector rayDirection = ray.getDirection();

        double denom = normal.dotProduct(rayDirection);
        if (isZero(denom)) {
            return null; // Ray is parallel to the plane - no intersections
        }

        double num = normal.dotProduct(this.point.subtract(rayOrigin));
        double t = num / denom;

        if (t > 0) {
            Point p = ray.getPoint(t);
            return List.of(new Intersectable.Intersection(this, p));
        }

        return null; // Intersection is behind the ray origin
    }
}