package unittests.geometries;

import geometries.Tube;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TubeTests {

    @Test
    public void testConstructor() {
        // Test for creating a valid Tube
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(5, axis);

        assertNotNull(tube, "Tube should be created successfully");
        assertEquals(5, tube.getRadius(), 0.001, "Radius should be 5");
    }

    @Test
    public void testInvalidConstructor() {
        // Test for invalid Tube (zero radius)
        assertThrows(IllegalArgumentException.class, () ->
                        new Tube(0, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))),
                "Radius cannot be zero or negative");
    }

    @Test
    public void testGetNormal() {
        // Axis along Z-axis from origin
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(5, axis);

        Point point = new Point(5, 0, 7); // A point on the surface of the tube
        Vector normal = tube.getNormal(point);

        Vector expectedNormal = new Vector(1, 0, 0); // Should be in X-direction

        assertEquals(expectedNormal, normal, "Normal vector is incorrect");
    }

    @Test
    public void testGetNormalFromAnotherPoint() {
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(5, axis);

        Point point = new Point(0, -5, 10); // A point on the surface
        Vector normal = tube.getNormal(point);

        Vector expectedNormal = new Vector(0, -1, 0); // Should be in -Y direction

        assertEquals(expectedNormal, normal, "Normal vector is incorrect");
    }
}
