package unittests.geometries;

import geometries.*;
import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for geometries.Geometries class
 */
public class GeometriesTests {

    // מקרה BVA: אוסף ריק (null צפוי)
    @Test
    public void testEmptyCollection() {
        Geometries geometries = new Geometries();
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        assertNull(geometries.findIntersections(ray), "Expected null for empty geometries collection");
    }

    // מקרה BVA: אף צורה לא נחתכת (null צפוי)
    @Test
    public void testNoIntersection() {
        Geometries geometries = new Geometries(
                new Sphere(1, new Point(0, 0, 5)),
                new Plane(new Point(0, 0, 5), new Vector(0, 0, 1))
        );
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        assertNull(geometries.findIntersections(ray), "Expected null when ray does not intersect any geometry");
    }

    // מקרה BVA: צורה אחת בלבד נחתכת (מצפים ל-1 או 2 נקודות, תלוי בגוף)
    @Test
    public void testOneIntersection() {
        Geometries geometries = new Geometries(
                new Sphere(1, new Point(0, 0, 3)), // נחתכת
                new Plane(new Point(0, 0, 5), new Vector(0, 0, 1)) // לא נחתכת
        );
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result = geometries.findIntersections(ray);
        assertNotNull(result, "Expected one intersection");
        assertTrue(result.size() == 2, "Expected 2 points of intersection with sphere");
    }

    // מקרה EP: כמה צורות נחתכות, אך לא כולן
    @Test
    public void testSomeIntersected() {
        Geometries geometries = new Geometries(
                new Sphere(1, new Point(0, 0, 3)), // נחתכת
                new Plane(new Point(0, 0, 4), new Vector(0, 0, 1)), // נחתכת
                new Plane(new Point(0, 0, -5), new Vector(0, -1, 0)) // לא נחתכת
        );
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result = geometries.findIntersections(ray);
        assertNotNull(result, "Expected intersections with some geometries");
        assertEquals(3, result.size(), "Expected 3 intersection points");
    }

    // מקרה BVA: כל הצורות נחתכות
    @Test
    public void testAllIntersected() {
        Geometries geometries = new Geometries(
                new Sphere(1, new Point(0, 0, 3)),     // 2 נקודות
                new Plane(new Point(0, 0, 2), new Vector(0, 0, 1)), // 1 נקודה
                new Triangle(new Point(-1, 1, 4), new Point(1, 1, 4), new Point(0, -1, 4)) // 1 נקודה
        );
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result = geometries.findIntersections(ray);
        assertNotNull(result, "Expected intersections with all geometries");
        assertEquals(4, result.size(), "Expected 4 intersection points total");
    }
}
