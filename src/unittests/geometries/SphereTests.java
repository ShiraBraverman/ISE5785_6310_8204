package unittests.geometries;

import geometries.Sphere;
import primitives.Point;
import primitives.Vector;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class SphereTests {

    @Test
    public void testConstructor() {
        // Test for a valid sphere
        Point center = new Point(0, 0, 0);
        double radius = 5.0;
        Sphere sphere = new Sphere(center, radius);

        assertEquals("Radius should be 5.0", 5.0, sphere.getRadius(), 0.0001);
        assertEquals("Center should be (0,0,0)", new Point(0, 0, 0), sphere.center);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidRadius() {
        // Test for invalid radius (<= 0)
        new Sphere(new Point(0, 0, 0), 0);
    }

    @Test
    public void testGetNormal() {
        // Test for normal vector
        Point center = new Point(0, 0, 0);
        double radius = 5.0;
        Sphere sphere = new Sphere(center, radius);

        // Test for a point on the surface of the sphere
        Point pointOnSurface = new Point(5, 0, 0);
        Vector normal = sphere.getNormal(pointOnSurface);

        // The normal should be the vector from the center to the point, normalized
        assertEquals("Normal vector is incorrect", new Vector(1, 0, 0), normal);
        assertEquals("Normal vector should have unit length", 1.0, normal.length(), 0.0001);
    }
}
