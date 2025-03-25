package geometries;

import primitives.*;

/**
 * Tube class represents a 3D tube, defined by a radius and a direction vector (axis).
 */
public class Tube extends Geometry {

    /**
     * The radius of the tube
     */
    private final double radius;
    /**
     * The direction vector of the tube's axis
     */
    private final Vector axis;

    /**
     * Constructor that initializes a tube with a radius and a direction vector.
     *
     * @param radius The radius of the tube.
     * @param axis   The direction vector of the tube's axis.
     */
    public Tube(double radius, Vector axis) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive.");
        }
        this.radius = radius;
        this.axis = axis.normalize(); // Normalize the axis vector
    }

    /**
     * Returns the normal vector of the tube at a given point.
     * The normal is calculated by projecting the vector from the center of the tube
     * to the point onto the plane perpendicular to the axis of the tube.
     *
     * @param point The point on the surface of the tube
     * @return The normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        Vector axisToPoint = point.subtract(new Point(0, 0, 0));

        double scale = axisToPoint.dotProduct(axis) / axis.lengthSquared();

        Vector projection = axis.scale(scale);

        return axisToPoint.subtract(projection).normalize();
    }
}
