package renderer;

import lighting.LightSource;
import primitives.Color;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.Intersection;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A simple implementation of a ray tracer that calculates the color of a ray
 * by finding intersections with geometries in the scene.
 * If no intersections are found, the background color is returned.
 * Otherwise, the color is calculated based on the ambient light at the closest intersection point.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for SimpleRayTracer.
     *
     * @param scene  the scene to be rendered
     * @param simple a RayTracerType enum value (not used in this implementation)
     */
    public SimpleRayTracer(Scene scene, RayTracerType simple) {
        super(scene);
    }

    /**
     * Calculates the color at the intersection point.
     *
     * @param intersection the intersection object
     * @param ray          the ray that hit the object
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        // Preprocess data: set normal, ray direction, dot product
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK; // No local effects → return black

        // Start with ambient light + emission
        Color color = scene.ambientLight.getIntensity()
                .scale(intersection.material.kD)
                .add(calcColorLocalEffects(intersection));

        return color;
    }

    /**
     * Traces a ray through the scene and returns the color at the closest intersection point.
     * If no intersections are found, the background color is returned.
     *
     * @param ray the ray to trace
     * @return the color at the closest intersection point, or the background color if no intersections are found
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Intersection in = ray.findClosestIntersection(intersections);
        return calcColor(in,ray);
    }


    /**
     * Preprocesses the intersection data by setting the normal, ray direction, and dot product.
     *
     * @param intersection the intersection object
     * @param rayDir       the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir.scale(-1);
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.nv = alignZero(intersection.rayDir.dotProduct(intersection.normal));
        if (isZero(intersection.nv)) {
            return false;
        }
        return true;
    }

    /**
     * Sets the light direction vector for the intersection point.
     * The light direction is calculated as the normalized vector from the light source
     * to the intersection point.
     *
     * @param intersection the intersection object containing the point of intersection
     * @param lightSource  the light source affecting the intersection
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.l = lightSource.getL(intersection.point).normalize();
        intersection.nl = alignZero(intersection.l.dotProduct(intersection.normal));
        return intersection.nl * intersection.nv > 0;
    }

    /**
     * Calculates the local lighting effects (diffusive + specular) at the intersection point.
     *
     * @param intersection the intersection object
     * @return the local lighting color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection) {

        if (intersection == null) {
            return scene.background;
        }
        Material material = intersection.material;
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.getLights()) {
            {
                if(!setLightSource(intersection, lightSource)) {
                    continue;
                }
                // Compute light intensity at the intersection point
                Color iL = lightSource.getIntensity(intersection.point);
                // Add contribution from diffusive and specular effects
                color = color.add(iL.scale(calcDiffusive(intersection))).add(iL.scale(calcSpecular(intersection)));
            }
        }
        return color;
    }


    /**
     * Calculates the specular component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the specular component as Double3
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector n = intersection.normal.normalize();
        Vector l = intersection.l.normalize();
        Vector v = intersection.rayDir.normalize(); // inverse of ray direction

        // Calculate reflection vector R = L - 2 * (N·L) * N
        Vector r = l.subtract(n.scale(2 * intersection.nl)).normalize();

        // Calculate R·V (viewer direction)
        double rv = r.dotProduct(v);

        if (rv <= 0)
            return Double3.ZERO; // no specular if angle > 90 degrees

        // Calculate specular component: kS * (R·V)^nShininess
        return intersection.material.kS.scale(Math.pow(rv, intersection.material.nShininess));
    }

    /**
     * Calculates the diffusive component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the diffusive component as Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        // According to Phong model: kD * max(0, N·L)
        // The diffusive component should never be negative
        double nl = Math.abs(intersection.nl);
        return intersection.material.kD.scale(nl);
    }
}

