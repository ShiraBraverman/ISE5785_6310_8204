package unittests.primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import primitives.Point;



/**
 * Unit tests for the Point class.
 */
class PointTests {

    /**
     * Test constructor and ZERO constant.
     */
    @Test
    void testConstructor() {
        Point p1 = new Point(1, 2, 3);
        assertEquals(1, p1.coordinates.d1(), "Incorrect x coordinate");
        assertEquals(2, p1.coordinates.d2(), "Incorrect y coordinate");
        assertEquals(3, p1.coordinates.d3(), "Incorrect z coordinate");

        Point zero = Point.ZERO;
        assertEquals(new Point(0, 0, 0), zero, "ZERO constant is incorrect");
    }

    /**
     * Test subtracting two points to create a vector.
     */
    @Test
    void testSubtract() {
        Point p1 = new Point(3, 3, 3);
        Point p2 = new Point(1, 1, 1);
        Vector result = p1.subtract(p2);
        assertEquals(new Vector(2, 2, 2), result, "Point subtraction is incorrect");

        // Test subtraction resulting in a zero vector (should throw an exception)
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "Subtracting a point from itself should throw an exception");
    }

    /**
     * Test adding a vector to a point to get a new point.
     */
    @Test
    void testAdd() {
        Point p = new Point(1, 1, 1);
        Vector v = new Vector(2, 2, 2);
        Point result = p.add(v);
        assertEquals(new Point(3, 3, 3), result, "Point addition with vector is incorrect");
    }

    /**
     * Test calculating the squared distance between two points.
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 8);
        double expected = 3*3 + 4*4 + 5*5; // 9 + 16 + 25 = 50
        assertEquals(expected, p1.distanceSquared(p2), "Squared distance calculation is incorrect");
    }

    /**
     * Test calculating the distance between two points.
     */
    @Test
    void testDistance() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 8);
        double expected = Math.sqrt(50); // sqrt(9 + 16 + 25) = sqrt(50)
        assertEquals(expected, p1.distance(p2), "Distance calculation is incorrect");
    }
}
