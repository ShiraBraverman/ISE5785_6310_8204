package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in the scene.
 */
public interface LightSource {
    /**
     * Returns the intensity of the light at a specific point.
     *
     * @param p the point to calculate intensity at
     * @return the light intensity (color) at the given point
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to the given point.
     *
     * @param p the point
     * @return the direction vector (normalized)
     */
    Vector getL(Point p);

    /**
     * Returns the distance from the light source to the given point.
     *
     * @param p the point
     * @return the distance between the light source and the point
     */
    double getDistance(Point p); // ← הוספת מתודה זו פותרת את השגיאה
}
