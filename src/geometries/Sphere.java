package geometries;

import primitives.*;

/**
 * Sphere class represents a 3D sphere, defined by a center point and a radius.
 */
public class Sphere extends Geometry {

    /**
     * The center of the sphere
     */
    private final Point center;
    /**
     * The radius of the sphere
     */
    private final double radius;

    /**
     * Constructor that initializes a sphere with a center point and a radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive.");
        }
        this.center = center;
        this.radius = radius;
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
}
