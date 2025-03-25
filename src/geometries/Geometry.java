package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * This abstract class represents a generic geometry object in a 3D space.
 * Each geometry object must implement the method to return a normal vector at a given point.
 */
public abstract class Geometry {

    /**
     * This method returns the normal vector to the geometry at the given point.
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector to the surface of the geometry at the given point.
     */
    public abstract Vector getNormal(Point point);

}
