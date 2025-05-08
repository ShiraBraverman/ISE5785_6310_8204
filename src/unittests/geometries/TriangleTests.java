package unittests.geometries;

import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Triangle} class.
 * These tests verify the proper behavior of triangle creation under different scenarios.
 */
class TriangleTests {

    /**
     * Test for valid triangle creation.
     * Ensures that a triangle can be created successfully with three distinct non-collinear points.
     */
    @Test
    void testValidTriangle() {
        assertDoesNotThrow(() -> new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        ));
    }

    /**
     * Test for an invalid triangle with two identical points.
     * Verifies that an IllegalArgumentException is thrown when attempting to create
     * a triangle with two identical vertices.
     */
    @Test
    void testIdenticalPoints() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle(
                new Point(1, 1, 1),
                new Point(1, 1, 1),
                new Point(2, 2, 2)
        ));
    }

    /**
     * Test for an invalid triangle where all three points are identical.
     * Verifies that an IllegalArgumentException is thrown when attempting to create
     * a triangle with all three vertices being the same point.
     */
    @Test
    void testAllPointsIdentical() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle(
                new Point(1, 1, 1),
                new Point(1, 1, 1),
                new Point(1, 1, 1)
        ));
    }

    /**
     * Test for an invalid triangle where all points lie on the same line.
     * Verifies that an IllegalArgumentException is thrown when attempting to create
     * a triangle with three collinear points, as they cannot form a valid triangle.
     */
    @Test
    void testCollinearPoints() {
        assertThrows(IllegalArgumentException.class, () -> new Triangle(
                new Point(0, 0, 0),
                new Point(1, 1, 1),
                new Point(2, 2, 2)
        ));
    }
    @Test
    void testFindIntersections() {
        // ייצור משולש עם שלושה נקודות
        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(5, 0, 0),
                new Point(0, 5, 0)
        );

        // מקרה 1: קרן חותכת את המשולש בנקודה אחת
        Ray ray1 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1)); // קרן שזורמת מ(-1,1,1) לכיוון ציר Z
        List<Point> intersections1 = triangle.findIntersections(ray1);
        assertNotNull(intersections1, "The ray should intersect the triangle.");
        assertEquals(1, intersections1.size(), "The ray should intersect the triangle in exactly one point.");
        assertEquals(new Point(1, 1, 0), intersections1.get(0), "The intersection point is incorrect.");

        // מקרה 2: קרן לא חותכת את המשולש (היא מחוץ למשולש)
        Ray ray2 = new Ray(new Point(10, 10, 0), new Vector(0, 0, 1)); // קרן יוצאת מחוץ למשולש
        List<Point> intersections2 = triangle.findIntersections(ray2);
        assertNull(intersections2, "The ray should not intersect the triangle.");

        // מקרה 3: קרן מקבילה לפני המשולש (לא חותכת)
        Ray ray3 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0)); // קרן מקבילה לציר X, מעל המשולש
        List<Point> intersections3 = triangle.findIntersections(ray3);
        assertNull(intersections3, "The ray should not intersect the triangle.");
    }
}