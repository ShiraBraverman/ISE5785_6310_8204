package unittests.geometries;

import geometries.Tube;
import primitives.Point;
import primitives.Vector;
import static org.junit.Assert.*;
import org.junit.Test;

public class TubeTests {

    @Test
    public void testConstructor() {
        // Test for creating a valid Tube
        Vector axis = new Vector(0, 0, 1);
        Tube tube = new Tube(5, axis);

        assertNotNull("Tube should be created successfully", tube);
        assertEquals("Radius should be 5", 5, tube.getRadius(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        // Test for invalid Tube (zero radius or invalid axis)
        new Tube(0, new Vector(0, 0, 1)); // Radius cannot be zero or negative
    }

    @Test
    public void testGetNormal() {
        // Test for normal vector on the surface of the tube
        Vector axis = new Vector(0, 0, 1); // Axis along the Z-axis
        Tube tube = new Tube(5, axis);

        Point point = new Point(5, 0, 0); // A point on the surface of the tube

        // The normal should be perpendicular to the axis and point
        Vector normal = tube.getNormal(point);

        // Since this is a point on the X-axis and the tube is along the Z-axis,
        // the expected normal should be (1, 0, 0) or a vector in the XY plane
        Vector expectedNormal = new Vector(1, 0, 0);

        assertEquals("Normal vector is incorrect", expectedNormal, normal);
    }

    @Test
    public void testGetNormalFromAnotherPoint() {
        // Test for normal vector from another point on the tube
        Vector axis = new Vector(0, 0, 1); // Axis along the Z-axis
        Tube tube = new Tube(5, axis);

        Point point = new Point(0, 5, 0); // A point on the surface of the tube

        // The normal should again be perpendicular to the axis and point
        Vector normal = tube.getNormal(point);

        // Since this is a point on the Y-axis and the tube is along the Z-axis,
        // the expected normal should be (0, 1, 0) or a vector in the XY plane
        Vector expectedNormal = new Vector(0, 1, 0);

        assertEquals("Normal vector is incorrect", expectedNormal, normal);
    }
}
