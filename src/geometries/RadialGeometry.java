package geometries;

import primitives.*;

/**
 * This abstract class represents a radial geometry, i.e., a geometry that has a radius.
 * It implements the Geometry interface and includes a radius field.
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * The radius of the radial geometry.
     */
    protected final double radius;

    /**
     * Constructor that initializes the radius of the radial geometry.
     *
     * @param radius The radius of the geometry.
     * @throws IllegalArgumentException If the radius is less than or equal to zero.
     */
    public RadialGeometry(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive.");
        }
        this.radius = radius;
    }

    /**
     * Returns the radius of the geometry.
     *
     * @return The radius of the geometry.
     */
    public double getRadius() {
        return radius;
    }
}
