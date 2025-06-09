package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class for geometries that can be intersected with a Ray.
 */
public abstract class Intersectable {

    /**
     * Passive Data Structure (PDS) representing an intersection between a ray and a geometry.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
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
     * Template method according to the Non-Virtual Interface (NVI) design pattern.
     * This method is final and calls the abstract helper.
     *
     * @param ray The ray to check for intersections.
     * @return List of intersections with geometry, or null if none found.
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Abstract helper method to be implemented by concrete geometries.
     * This method performs the actual intersection logic.
     *
     * @param ray The ray to check.
     * @return List of Intersection objects, or null if no intersection.
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * Returns only the list of intersection points (without the geometry).
     *
     * @param ray The ray to intersect.
     * @return List of Points, or null if no intersection.
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
}
