package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;
import primitives.Point;
import java.util.List;

/**
 * Simple ray tracer implementation.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor initializing SimpleRayTracer with the given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces the given ray and returns the color.
     *
     * @param ray the ray to trace
     * @return the color computed for the ray
     */
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);

        if (intersections == null) {
            // No intersection points found, return the background color
            return scene.background;
        }

        // Find the closest intersection point to the ray origin
        Point closestPoint = ray.findClosestPoint(intersections);

        // Return the color at the closest intersection point calculated by calcColor
        return calcColor(closestPoint);
    }

    private Color calcColor(Point point) {
        // For now: return only the ambient light intensity color
        return scene.ambientLight.getIntensity();
    }
}
