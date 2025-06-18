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
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final double MIN_CALC_COLOR_K = 0.001;

    public SimpleRayTracer(Scene scene, RayTracerType simple) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Intersection in = ray.findClosestIntersection(intersections);
        return calcColor(in, ray);
    }

    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        Color color = scene.ambientLight.getIntensity()
                .scale(intersection.material.kD)
                .add(calcColorLocalEffects(intersection, ray.getDirection()));

        return color;
    }

    public boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir.scale(-1);
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.nv = alignZero(intersection.rayDir.dotProduct(intersection.normal));
        return !isZero(intersection.nv);
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.l = lightSource.getL(intersection.point).normalize();
        intersection.nl = alignZero(intersection.l.dotProduct(intersection.normal));
        return intersection.nl * intersection.nv > 0;
    }

    /**
     * Computes how much light passes from the light source to the point (transparency).
     */
    private Double3 transparency(LightSource light, Vector l, Vector n, Intersection geoPoint) {
        // קרן אור מהנקודה לכיוון האור עם הזזה קטנה למניעת self-shadowing
        Ray lightRay = new Ray(geoPoint.point.add(n.scale(geoPoint.nv > 0 ? DELTA : -DELTA)), l.scale(-1));

        // חיפוש חיתוכים עד למרחק של מקור האור
        List<Intersection> intersections = scene.geometries.calculateIntersections(lightRay, light.getDistance(geoPoint.point));
        if (intersections == null) return Double3.ONE;

        Double3 ktr = Double3.ONE;

        // מכפיל שקיפות לאורך כל החפיפות
        for (Intersection inter : intersections) {
            ktr = ktr.product(inter.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO; // כמעט שקוף - עצור כאן
        }

        return ktr;
    }

    private Color calcColorLocalEffects(Intersection intersection, Vector v) {
        Material material = intersection.material;
        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.getLights()) {
            if (!setLightSource(intersection, lightSource))
                continue;

            // משתמשים בשקיפות במקום unshaded
            Double3 ktr = transparency(lightSource, intersection.l, intersection.normal, intersection);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                continue; // חסום כמעט לגמרי - לא מוסיפים אור

            Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
            color = color.add(iL.scale(calcDiffusive(intersection)))
                    .add(iL.scale(calcSpecular(intersection, v)));
        }
        return color;
    }

    private Double3 calcSpecular(Intersection intersection, Vector v) {
        Vector n = intersection.normal.normalize();
        Vector l = intersection.l.normalize();
        v = v.normalize();

        Vector r = l.subtract(n.scale(2 * intersection.nl)).normalize();
        double rv = r.dotProduct(v);

        if (rv <= 0)
            return Double3.ZERO;

        return intersection.material.kS.scale(Math.pow(rv, intersection.material.nShininess));
    }

    private Double3 calcDiffusive(Intersection intersection) {
        double nl = Math.abs(intersection.nl);
        return intersection.material.kD.scale(nl);
    }
}
