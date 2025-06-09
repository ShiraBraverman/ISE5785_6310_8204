package geometries;

import primitives.Vector;
import primitives.Point;
import primitives.Ray;
import primitives.Color;

import java.util.List;

/**
 * Abstract class representing a generic geometric object in 3D space.
 *
 * This class defines the common interface and behavior for all concrete geometry subclasses,
 * such as Sphere, Plane, Triangle, etc. Its main responsibility is to provide
 * a method for obtaining the normal vector at any given point on the geometry's surface.
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry (default is black).
     */
    protected Color emission = Color.BLACK;

    /**
     * Default constructor for the Geometry class.
     */
    public Geometry() {
        // No specific initialization needed here
    }

    /**
     * Getter for the emission color.
     *
     * @return the emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Setter for the emission color using builder pattern.
     *
     * @param emission the color to set
     * @return this geometry instance (for method chaining)
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Abstract method to get the normal vector to the surface at a given point.
     * <p>
     * The normal vector is perpendicular to the surface at the specified point.
     * This vector is crucial for lighting calculations, shading, and rendering.
     * Each concrete geometry class must provide its own implementation of this method.
     *
     * @param point The point on the geometry's surface where the normal is requested.
     * @return The normal vector at the specified point (usually normalized).
     */
    public abstract Vector getNormal(Point point);

    @Override
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
}
