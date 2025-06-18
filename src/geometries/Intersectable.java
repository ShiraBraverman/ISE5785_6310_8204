package geometries;

import primitives.*;
import lighting.LightSource;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing geometric objects that can be intersected by a Ray.
 */
public abstract class Intersectable {

    /**
     * Passive Data Structure (PDS) representing an intersection between a Ray and a Geometry.
     * <p>
     * This inner class holds useful cached data related to the intersection point,
     * which helps in improving rendering efficiency and clarity.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;
        public final Material material;

        public Vector rayDir;
        public Vector normal;
        public Double nv;
        public LightSource lightSource;
        public Vector l;
        public Double nl;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? null : geometry.getMaterial();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return geometry == other.geometry && Objects.equals(point, other.point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * Template method following the Non-Virtual Interface (NVI) design pattern.
     * Calls the abstract helper method implemented by specific geometries.
     *
     * @param ray The ray to check for intersections
     * @return List of intersection objects, or null if none found
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        // Default maxDistance is positive infinity
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * New overloaded method to calculate intersections with a max distance limit.
     *
     * @param ray The ray to check for intersections
     * @param maxDistance The maximal distance to consider intersections
     * @return List of intersection objects, or null if none found
     */
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Abstract helper method to be implemented by each geometry type.
     *
     * @param ray The ray to test for intersection
     * @param maxDistance The maximal distance to consider intersections
     * @return List of intersection objects, or null if none found
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Convenience method to return just the points of intersection without geometry info.
     *
     * @param ray The ray to check
     * @return List of intersection points, or null if none found
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
}
