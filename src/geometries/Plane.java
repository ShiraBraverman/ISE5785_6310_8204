package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static primitives.Util.isZero;

/**
 * This class represents a plane in 3D space, defined by a point and a normal vector.
 * It can be initialized using either three points or a point and a normal vector.
 */
public class Plane extends Geometry {

    /**
     * The point on the plane
     */
    private final Point point;
    /**
     * The normal vector to the plane
     */
    private final Vector normal;

    /**
     * Constructor that initializes the plane based on three points.
     * This constructor calculates the normal vector by taking the cross product of two vectors
     * defined by the three points, and stores one of the points as the reference point for the plane.
     *
     * @param p1 The first point defining the plane.
     * @param p2 The second point defining the plane.
     * @param p3 The third point defining the plane.
     */
    public Plane(Point p1, Point p2, Point p3) {
        // Calculate two vectors from the points by subtracting the points
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        // The normal vector is the cross product of the two vectors
        Vector normalUnnormalized = v1.crossProduct(v2);

        if (normalUnnormalized.length() == 0) {
            throw new IllegalArgumentException("The three points are collinear and cannot define a plane.");
        }

        // Store the point as the reference point for the plane
        this.point = p1;

        // The normal is not normalized yet, so we leave it as is
        this.normal = normalUnnormalized.normalize(); // Store the calculated normal
    }

    /**
     * Constructor that initializes the plane with a point and a normal vector.
     * The normal vector must be normalized.
     *
     * @param point  A point on the plane.
     * @param normal The normal vector to the plane (not necessarily normalized).
     */
    public Plane(Point point, Vector normal) {
        if (normal.equals(new Vector(0, 0, 0))) {
            throw new IllegalArgumentException("Normal vector cannot be a zero vector");
        }

        this.point = point;
        // Normalize the normal vector before storing it
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector to the plane.
     * If the plane was initialized with three points, the normal is calculated.
     *
     * @return The normal vector to the plane.
     */
    @Override
    public Vector getNormal(Point point) {
        if (normal == null) {
            throw new UnsupportedOperationException("Normal is not calculated yet.");
        }
        return normal;
    }

    /**
     * Returns the reference point on the plane.
     *
     * @return The reference point on the plane.
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Finds the intersection points of the plane with a given ray.
     *
     * @param ray The ray to check for intersections.
     * @return A list of points where the ray intersects the plane, or null if no intersection.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector normal = this.normal;
        Point rayOrigin = ray.getOrigin();
        Vector rayDirection = ray.getDirection();

        double denom = normal.dotProduct(rayDirection);
        System.out.println("denom = " + denom + " (isZero = " + isZero(denom) + ")");

        if (isZero(denom)) {
            System.out.println("Ray is parallel to the plane.");
            return null;
        }

        double num = normal.dotProduct(this.point.subtract(rayOrigin));
        System.out.println("num = " + num);

        double t = num / denom;
        System.out.println("t = " + t);

        if (t >= 0 && num * denom > 0) {
            Point intersection = ray.getPoint(t);
            return List.of(intersection);  // Return the intersection point as a list
        }

        System.out.println("Intersection is behind the ray's origin.");
        return null;
    }

}
