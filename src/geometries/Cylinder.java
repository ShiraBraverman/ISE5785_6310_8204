package geometries;

import primitives.*;

/**
 * Cylinder class represents a 3D cylinder, defined by a radius, height, center, and axis.
 */
public class Cylinder extends Geometry {

    /**
     * The center of the cylinder (base of the cylinder)
     */
    private final Point center;

    /**
     * The height of the cylinder
     */
    private final double height;

    /**
     * The axis of the cylinder, represented as a normalized vector.
     */
    private final Vector axis;

    /**
     * Constructor that initializes a cylinder with a center, height, and axis.
     *
     * @param center The center of the cylinder (base point).
     * @param height The height of the cylinder.
     * @param axis   The direction vector of the cylinder's axis.
     */
    public Cylinder(Point center, double height, Vector axis) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
        this.center = center;
        this.height = height;
        this.axis = axis.normalize();
    }

    /**
     * Returns the normal vector of the cylinder at a given point.
     * The normal is calculated by projecting the vector from the point on the cylinder
     * to the axis of the cylinder, and then subtracting it from the point-to-axis vector.
     *
     * @param point The point on the surface of the cylinder
     * @return The normal vector to the cylinder at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        Vector centerToPoint = point.subtract(center);
        double projectionLength = centerToPoint.dotProduct(axis);
        if (projectionLength <= 0) {
            return axis.scale(-1.0);
        }
        if (projectionLength >= height) {
            return axis;
        }
        Point projectionPoint = center.add(axis.scale(projectionLength));
        return point.subtract(projectionPoint).normalize();
    }
}
