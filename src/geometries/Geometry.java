package geometries;

import primitives.Vector;
import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing a generic geometric object in 3D space.
 *
 * This class defines the common interface and behavior for all concrete geometry subclasses,
 * such as Sphere, Plane, Triangle, etc. Its main responsibility is to provide
 * a method for obtaining the normal vector at any given point on the geometry's surface.
 */
public abstract class Geometry implements Intersectable {

    /**
     * Default constructor for the Geometry class.
     */
    public Geometry() {
        // No specific initialization needed here
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

    /**
     * Finds the intersection points between this geometry and a given ray.
     * <p>
     * This method is part of the Intersectable interface and is intended to be overridden
     * by each specific geometry subclass. In this abstract class, it returns null by default,
     * indicating no intersections.
     *
     * @param ray The ray to test for intersections.
     * @return A list of intersection points, or null if none are found.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}