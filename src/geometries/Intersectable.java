package geometries;

import java.util.List;
import primitives.*;

/**
 * Interface for geometries that can be intersected with a Ray.
 */
public interface Intersectable {
    /**
     * Finds the intersection points of the geometry with a given ray.
     *
     * @param ray The ray to check for intersections.
     * @return A list of points where the ray intersects the geometry, or null if no intersection.
     */
    List<Point> findIntersections(Ray ray);
}
