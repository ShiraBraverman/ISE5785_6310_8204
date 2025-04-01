package unittests.geometries;

import geometries.Polygon;
import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Polygon} class.
 */
public class PolygonTests {

    /**
     * Test method for Polygon constructor.
     */
    @Test
    public void testConstructor() {
        // ============ Valid Polygon ==============
        assertDoesNotThrow(() -> new Polygon(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(1, 1, 0),
                new Point(0, 1, 0)
        ));

        // ============ Invalid Polygons ==============
        // Less than 3 points
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                new Point(0, 0, 0),
                new Point(1, 0, 0)
        ));

        // Duplicate points
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        ));

        // Points not in the same plane
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(1, 1, 1),
                new Point(0, 1, 0)
        ));

        // Concave polygon
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                new Point(0, 0, 0),
                new Point(2, 0, 0),
                new Point(1, 1, 0),
                new Point(2, 2, 0),
                new Point(0, 2, 0)
        ));
    }

    /**
     * Test method for getNormal()
     */
    @Test
    public void testGetNormal() {
        Polygon polygon = new Polygon(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(1, 1, 0),
                new Point(0, 1, 0)
        );

        Vector normal = polygon.getNormal(new Point(0.5, 0.5, 0));

        // Ensure normal is unit length
        assertEquals(1, normal.length(), "Polygon normal is not a unit vector");

        // Check that the normal is correct (should be (0,0,1) or (0,0,-1))
        assertTrue(normal.equals(new Vector(0, 0, 1)) || normal.equals(new Vector(0, 0, -1)),
                "Polygon normal is incorrect");
    }
}
