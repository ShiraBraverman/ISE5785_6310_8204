package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * The Triangle class represents a triangle in 3D space.
 * It is a specific case of a polygon with exactly three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle using three points.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * Returns the list of intersection points between a given ray and the triangle,
     * limited by the given maxDistance.
     *
     * @param ray The ray to check for intersections.
     * @param maxDistance The maximum allowed distance from the ray origin.
     * @return A list of intersections, or null if there are none within maxDistance.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersectable.Intersection> planeIntersections = plane.calculateIntersections(ray, maxDistance);
        if (planeIntersections == null) {
            return null;
        }

        Point p = planeIntersections.get(0).point;

        if (!isPointInPolygon(p)) {
            return null;
        }

        return List.of(new Intersectable.Intersection(this, p));
    }
}
