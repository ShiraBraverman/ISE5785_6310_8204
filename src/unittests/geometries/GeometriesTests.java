package unittests;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GeometriesTests {

    @Test
    public void testEmptyGeometries() {
        Geometries geometries = new Geometries();
        Ray ray = new Ray(new Point(1.0, 2.0, 3.0), new Vector(1.0, 1.0, 1.0));
        assertNull(geometries.findIntersections(ray), "Expected null for empty geometries collection");
    }

    @Test
    public void testNoIntersection() {
        Geometries geometries = new Geometries(
                new Sphere(new Point(10, 10, 10), 2),
                new Plane(new Point(-5, -5, -5), new Vector(1, -1, 2))
        );

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 2, 1)); // פונה בכיוון הפוך מהמישור
        assertNull(geometries.findIntersections(ray), "Expected null when ray does not intersect any geometry");
    }

    @Test
    void testSingleIntersection() {
        Geometries geometries = new Geometries(
                new Sphere(new Point(5, 0, 0), 2),
                new Plane(new Point(0, 5, 0), new Vector(0, -1, 1))
        );

        // הקרן נעה על ציר X מימין לשמאל, תעבור דרך הספירה ולא תחתוך את המישור
        Ray ray = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));

        List<Point> intersections = geometries.findIntersections(ray);

        assertNotNull(intersections, "Expected intersection point");
        assertEquals(2, intersections.size(), "Expected two intersection points with the sphere only");
    }


    @Test
    public void testSomeIntersections() {
        Geometries geometries = new Geometries(
                new Sphere(new Point(3, 3, 3), 2),
                new Plane(new Point(1, 2, 0), new Vector(2, 3, 1)),
                new Triangle(
                        new Point(2, 0, 2),
                        new Point(4, 2, 1),
                        new Point(3, 1, 4)
                )
        );

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(3, 3, 3));
        assertEquals(3, geometries.findIntersections(ray).size(), "Expected three intersection points");
    }

    @Test
    public void testAllIntersected() {
        Geometries geometries = new Geometries(
                new Sphere(new Point(0, 0, 5), 1),
                new Plane(new Point(0, 0, 4), new Vector(1, 2, 3)),
                new Triangle(
                        new Point(-1, 1, 4),
                        new Point(1, 1, 4),
                        new Point(0, -1, 4)
                )
        );

        Ray ray = new Ray(new Point(0, 0, 0.999), new Vector(0, 0, 1));
        assertEquals(4, geometries.findIntersections(ray).size(), "Expected four intersection points");
    }
}
