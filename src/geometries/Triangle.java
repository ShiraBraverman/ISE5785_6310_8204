package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * The Triangle class represents a triangle in 3D space.
 * It is a specific case of a polygon with exactly three vertices.
 * This class extends the Polygon class, which provides general polygon functionality.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle using three points.
     * Since a triangle is a polygon with three vertices, this constructor
     * simply calls the constructor of the Polygon class.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor with three points.
    }

    /**
     * This method returns the list of intersection points between a given ray and the triangle.
     *
     * @param ray The ray to check for intersections.
     * @return A list of points where the ray intersects the triangle, or null if there are no intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Step 1: Find the intersection of the ray with the plane of the triangle
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null || planeIntersections.isEmpty()) {
            return null; // No intersection with the plane, return null
        }

        // Step 2: Check if the intersection point is inside the triangle
        Point intersectionPoint = planeIntersections.get(0); // Only one intersection point with the plane
        if (isPointInPolygon(intersectionPoint)) {
            return List.of(intersectionPoint); // If the point is inside the triangle, return it
        }

        return null; // If the point is not inside the triangle, no intersection
    }
}