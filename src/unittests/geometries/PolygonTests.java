package unittests.geometries;

import geometries.Polygon;
import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import primitives.Ray;



/**
 * Unit tests for {@link geometries.Polygon} class.
 * These tests verify the proper behavior of polygon creation and functionality under different scenarios.
 */
public class PolygonTests {

    /**
     * Test method for Polygon constructor.
     * Verifies that polygons are created correctly with valid parameters and that
     * appropriate exceptions are thrown for invalid parameters.
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
     * Test method for getNormal().
     * Verifies that the normal vector to the polygon is calculated correctly
     * and is a unit vector perpendicular to the polygon's plane.
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
    @Test
    public void testFindIntersections() {
        Polygon triangle = new Polygon(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        // TC01: Ray intersects inside the triangle
        Point expectedPoint = new Point(0.25, 0.25, 0);
        assertEquals(
                List.of(expectedPoint),
                triangle.findIntersections(
                        new Ray(new Point(0.25, 0.25, 1), new Vector(0, 0, -1))
                ),
                "Ray should intersect inside the triangle"
        );

        // TC02: Ray misses the triangle
        assertNull(
                triangle.findIntersections(
                        new Ray(new Point(2, 2, 1), new Vector(0, 0, -1))
                ),
                "Ray should miss the triangle"
        );

        // TC03: Ray lies in the plane but outside the triangle
        assertNull(
                triangle.findIntersections(
                        new Ray(new Point(1.5, 0.5, 0), new Vector(1, 0, 0))
                ),
                "Ray lies in the plane but does not intersect the triangle"
        );

        // TC04: Ray is parallel and above the triangle
        assertNull(
                triangle.findIntersections(
                        new Ray(new Point(0.5, 0.5, 1), new Vector(1, 0, 0))
                ),
                "Ray is parallel to the triangle and should not intersect"
        );

        // TC05: Ray intersects the plane outside the triangle
        assertNull(
                triangle.findIntersections(
                        new Ray(new Point(1.5, 0.5, 1), new Vector(0, 0, -1))
                ),
                "Ray intersects plane but outside triangle"
        );
    }

}