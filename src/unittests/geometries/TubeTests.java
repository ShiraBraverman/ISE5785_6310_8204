package unittests.geometries;

import geometries.Tube;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

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
    @Test
    public void testFindIntersections() {
        // יצירת צינור עם רדיוס 2 וציר לאורך ציר Y
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        Tube tube = new Tube(2, axisRay);

        // TC01: קרן חותכת את הצינור ב-2 נקודות
//        Ray ray1 = new Ray(new Point(0, 0, 0.001), new Vector(1, 0, 0));
//        List<Point> result1 = tube.findIntersections(ray1);
//        assertNotNull(result1, "Expected 2 intersection points");
//        assertEquals(2, result1.size(), "Wrong number of points");

        // TC02: קרן נוגעת בצינור בנקודה אחת (משיק)
//        Ray ray2 = new Ray(new Point(2, -5, 0), new Vector(0, 1, 0));
//        List<Point> result2 = tube.findIntersections(ray2);
//        assertNotNull(result2, "Expected 1 intersection point (tangent)");
//        assertEquals(1, result2.size(), "Wrong number of points");

        // TC03: קרן מפספסת את הצינור
//        Ray ray3 = new Ray(new Point(5, -5, 5), new Vector(0, 1, 0));
//        List<Point> result3 = tube.findIntersections(ray3);
//        assertNull(result3, "Expected no intersection points");

        // TC04: קרן מתחילה בתוך הצינור ויוצאת בנקודה אחת
//        Ray ray4 = new Ray(new Point(1, 0, 0), new Vector(1, 1, 0));
//        List<Point> result4 = tube.findIntersections(ray4);
//        assertNotNull(result4, "Expected 1 intersection point");
//        assertEquals(1, result4.size(), "Wrong number of points");

        // TC05: קרן מקבילה לציר ולא נוגעת בצינור
        Ray ray5 = new Ray(new Point(5, 0, 0), new Vector(0, 1, 0));
        List<Point> result5 = tube.findIntersections(ray5);
        assertNull(result5, "Expected no intersection points (parallel and outside)");
    }
}
