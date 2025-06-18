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
        // The geometry that was intersected
        public final Geometry geometry;

        // The intersection point on the geometry
        public final Point point;

        // The material of the geometry at the point of intersection (final - initialized in constructor)
        public final Material material;

        // --- Cached fields for lighting calculations ---

        // The ray that caused this intersection
        public Vector rayDir;

        // The normal vector at the intersection point on the geometry
        public Vector normal;

        // Dot product of ray direction and the normal vector
        public Double nv;

        // The light source currently being processed for this intersection
        public LightSource lightSource;

        // Vector from the light source to the intersection point
        public Vector l;

        // Dot product of light direction and normal vector
        public Double nl;

        /**
         * Constructor for creating an Intersection object with the associated geometry and point.
         * Initializes the material if geometry is not null.
         *
         * @param geometry the intersected geometry
         * @param point    the intersection point on the geometry
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? null : geometry.getMaterial();
        }

        /**
         * Checks whether this intersection is equal to another object.
         * Equality is based on both the geometry and the intersection point.
         *
         * @param obj the object to compare with
         * @return true if the object is an Intersection with the same geometry and point; false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return geometry == other.geometry && Objects.equals(point, other.point);
        }

        /**
         * Returns a string representation of the Intersection object.
         * Includes the geometry and the intersection point.
         *
         * @return a string describing this intersection
         */
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
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Abstract helper method to be implemented by each geometry type.
     *
     * @param ray The ray to test for intersection
     * @return List of intersection objects, or null if none found
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

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
