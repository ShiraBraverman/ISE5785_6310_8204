package unittests.geometries;

import geometries.Sphere;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere} class.
 * These tests verify the proper behavior of sphere creation and functionality.
 */
public class SphereTests {

    /**
     * Test for the sphere constructor with valid parameters.
     * Verifies that a sphere is created correctly with the given center point and radius.
     */
    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new Sphere(new Point(0, 0, 0), 0),
                "Expected exception for radius <= 0"
        );
    }

    /**
     * Test for the sphere constructor with invalid radius.
     * Verifies that an IllegalArgumentException is thrown when attempting to create
     * a sphere with a radius less than or equal to zero.
     */
    @Test
    public void testConstructorInvalidRadius() {
        assertThrows(IllegalArgumentException.class,
                () -> new Sphere(new Point(0, 0, 0), 0),
                "Expected exception for radius <= 0");
    }

    /**
     * Test for the getNormal method of the sphere.
     * Verifies that the normal vector at a point on the sphere's surface
     * is calculated correctly as the normalized vector from the center to the point.
     */
    @Test
    public void testGetNormal() {
        Point center = new Point(0, 0, 0);
        double radius = 5.0;
        Sphere sphere = new Sphere(center, radius);

        Point pointOnSurface = new Point(5, 0, 0);
        Vector normal = sphere.getNormal(pointOnSurface);

        assertEquals(new Vector(1, 0, 0), normal, "Normal vector is incorrect");
        assertEquals(1.0, normal.length(), 0.0001, "Normal vector should have unit length");
    }
    @Test
    void testFindIntersections() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1);

        Ray ray = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));

        List<Point> result = sphere.findIntersections(ray);

        // בודקים שהתוצאה לא null
        assertNotNull(result, "The ray should intersect the sphere");

        // בודקים שיש בדיוק שתי נקודות חיתוך
        assertEquals(2, result.size(), "Wrong number of intersection points");

        // בודקים את הנקודות עצמן
        Point p1 = new Point(-1, 0, 0);
        Point p2 = new Point(1, 0, 0);

        // ודאו שהן נמצאות בתוך התוצאה (בלי תלות בסדר)
        assertTrue(result.contains(p1), "Missing expected intersection point: " + p1);
        assertTrue(result.contains(p2), "Missing expected intersection point: " + p2);
    }

}
