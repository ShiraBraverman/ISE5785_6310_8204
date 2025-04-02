package unittests.geometries;

import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
}