package geometries;

import primitives.Vector;
import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * This abstract class represents a generic geometry object in a 3D space.
 * Geometry objects, such as spheres, planes, and others, are subclasses of this class.
 * The purpose of this class is to define a common interface for all geometry objects
 * in 3D space, focusing on the computation of the normal vector at a given point on the surface.
 * Each concrete geometry object must implement the method to return the normal vector
 * at a specified point on its surface.
 */
public abstract class Geometry implements Intersectable {

    /**
     * Default constructor for Geometry.
     */
    public Geometry() {
    }

    /**
     * This method returns the normal vector to the geometry at the given point.
     * A normal vector is a vector that is perpendicular to the surface of the geometry
     * at the point of interest. It is typically used in lighting calculations, ray tracing,
     * and surface interactions in 3D graphics.
     *
     * @param point The point on the surface of the geometry where the normal vector is to be calculated.
     * @return The normal vector to the surface of the geometry at the given point.
     * The vector is typically normalized (unit vector) but this depends on the specific geometry.
     */
    public abstract Vector getNormal(Point point);

    /**
     * This method is part of the Intersectable interface.
     * For now, it will be implemented as an empty method,
     * and it will be overridden by subclasses such as Sphere, Plane, etc.
     *
     * @param ray The ray to check for intersections.
     * @return null (to be implemented by subclasses)
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}