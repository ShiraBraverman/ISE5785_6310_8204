package unittests.geometries;

import geometries.Plane;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Plane} class.
 * These tests verify the proper behavior of plane creation and functionality under different scenarios.
 */
class PlaneTests {

    /**
     * Test for constructor with three points.
     * Ensures the normal vector is calculated correctly, is perpendicular to the plane,
     * and has length 1 (is normalized).
     */
    @Test
    void testConstructorWithThreePoints() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);

        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);

        // Check if the normal is perpendicular to both vectors
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        assertEquals(0, normal.dotProduct(v1), 1e-10, "Normal is not perpendicular to v1");
        assertEquals(0, normal.dotProduct(v2), 1e-10, "Normal is not perpendicular to v2");

        // Check if the normal length is 1
        assertEquals(1, normal.length(), 1e-10, "Normal vector is not normalized");
    }

    /**
     * Test for constructor with three collinear points.
     * Verifies that an IllegalArgumentException is thrown when attempting to create
     * a plane with three points that lie on the same line, as they cannot define a plane.
     */
    @Test
    void testConstructorCollinearPoints() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 1, 1);
        Point p3 = new Point(2, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p3),
                "Constructor should throw an exception for collinear points");
    }

    /**
     * Test for constructor with duplicate points.
     * Verifies that an IllegalArgumentException is thrown when attempting to create
     * a plane with duplicate points, as three distinct points are required to define a plane.
     */
    @Test
    void testConstructorDuplicatePoints() {
        Point p1 = new Point(1, 1, 1);

        // Identical first and second points
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, new Point(2, 2, 2)),
                "Constructor should throw an exception for duplicate first and second points");

        // Identical first and third points
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, new Point(2, 2, 2), p1),
                "Constructor should throw an exception for duplicate first and third points");

        // Identical second and third points
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(2, 2, 2), p1, p1),
                "Constructor should throw an exception for duplicate second and third points");

        // All three points are identical
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1),
                "Constructor should throw an exception for all points being identical");
    }

    /**
     * Test for getNormal method.
     * Verifies that the normal vector to the plane is calculated correctly,
     * is a unit vector, and is oriented in the expected direction.
     */
    @Test
    void testGetNormal() {
        // Create a plane using three points
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);

        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);

        // Check that the normal is a unit vector
        assertEquals(1, normal.length(), 1e-10, "Normal vector is not normalized");

        // Check if the normal is oriented correctly
        assertEquals(new Vector(0, 0, 1), normal, "Normal vector is incorrect");
    }
}