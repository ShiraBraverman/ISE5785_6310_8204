package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;
import primitives.Point;
import java.util.List;
import geometries.Intersectable.Intersection;

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
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);

        if (intersections == null) {
            // No intersection points found, return the background color
            return scene.background;
        }

        // Find the closest intersection point to the ray origin
        Intersection closestIntersection = ray.findClosestIntersection(intersections);

        // Return the color at the closest intersection point calculated by calcColor
        return calcColor(closestIntersection);
    }

    private Color calcColor(Intersection intersection) {
        if (intersection == null) {
            return scene.background;
        }

        Color ambient = scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kD);

        return ambient.add(intersection.geometry.getEmission());
    }
}
