package geometries;

import primitives.*;

/**
 * Cylinder class represents a 3D cylinder, defined by a radius, height, and a direction vector (axis).
 * It extends the Tube class.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     * Defines the vertical dimension of the cylinder along its axis.
     */
    private final double height;

    /**
     * Constructor that initializes a cylinder with a radius, height, and axis.
     *
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     * @param axis   The direction vector of the cylinder's axis (must be normalized).
     * @throws IllegalArgumentException If the height is less than or equal to 0.
     */
    public Cylinder(double radius, double height, Vector axis) {
        super(radius, axis);  // Call the constructor of Tube to initialize radius and axis
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
        this.height = height;
    }

    /**
     * Returns the normal vector of the cylinder at a given point.
     * The normal vector is calculated by projecting the vector from the point on the cylinder
     * to the axis of the cylinder, and then subtracting the projected point from the given point.
     * If the point is on the sides of the cylinder, the normal vector is perpendicular to the surface.
     * If the point is on the top or bottom base, the normal vector is along the cylinder's axis.
     *
     * @param point The point on the surface of the cylinder.
     * @return The normal vector to the cylinder at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        Vector centerToPoint = point.subtract(new Point(0, 0, 0));  // Vector from the center to the point on the surface
        double projectionLength = centerToPoint.dotProduct(axis);  // Projection of the vector onto the axis

        // Handle the base case (point is on the bottom base of the cylinder)
        if (projectionLength <= 0) {
            return axis.scale(-1.0);  // Normal points downward from the base
        }

        // Handle the top base case (point is on the top base of the cylinder)
        if (projectionLength >= height) {
            return axis;  // Normal points upward from the top base
        }

        // Handle the side of the cylinder (point is on the lateral surface)
        Point projectionPoint = new Point(0, 0, 0).add(axis.scale(projectionLength));  // Calculate the projected point on the axis
        return point.subtract(projectionPoint).normalize();  // Return the normalized vector from the projected point to the given point
    }
}
