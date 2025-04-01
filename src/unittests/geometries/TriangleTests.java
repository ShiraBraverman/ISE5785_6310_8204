package unittests.geometries;

import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Triangle} class.
 */
class TriangleTests {

    /**
     * Test for valid triangle creation.
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
