import org.junit.jupiter.api.Test;
import primitives.Point;
import java.util.List;
import java.util.Arrays;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class RayTests {
    // נניח שיש קרן לדוגמה עם נקודת התחלה ברירת מחדל
    private final Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

    /**
     * בדיקת findClosestPoint לפי תסריטי TC ו-EP/BVA:
     * 1. נקודה באמצע הרשימה היא הקרובה
     * 2. רשימה ריקה (null) מחזירה null
     * 3. הנקודה הראשונה היא הקרובה ביותר
     * 4. הנקודה האחרונה היא הקרובה ביותר
     */
    @Test
    public void testFindClosestPoint() {
        // Case 1: נקודה באמצע הרשימה היא הקרובה
        List<Point> points1 = Arrays.asList(
                new Point(5, 0, 0),
                new Point(1, 0, 0),  // הקרובה ביותר
                new Point(10, 0, 0)
        );
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points1), "נקודה באמצע הרשימה היא הקרובה");

        // Case 2: רשימה ריקה (null)
        assertNull(ray.findClosestPoint(null), "רשימה ריקה מחזירה null");

        // Case 3: הנקודה הראשונה היא הקרובה ביותר
        List<Point> points3 = Arrays.asList(
                new Point(0.5, 0, 0),  // הקרובה ביותר
                new Point(5, 0, 0),
                new Point(10, 0, 0)
        );
        assertEquals(new Point(0.5, 0, 0), ray.findClosestPoint(points3), "הנקודה הראשונה היא הקרובה");

        // Case 4: הנקודה האחרונה היא הקרובה ביותר
        List<Point> points4 = Arrays.asList(
                new Point(5, 0, 0),
                new Point(10, 0, 0),
                new Point(0.2, 0, 0)  // הקרובה ביותר
        );
        assertEquals(new Point(0.2, 0, 0), ray.findClosestPoint(points4), "הנקודה האחרונה היא הקרובה");
    }
}
