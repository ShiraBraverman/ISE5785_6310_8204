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
    private static final double DELTA = 0.1;
    private static final Double3 MIN_CALC_COLOR_K = new Double3(0.001);
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final Double3 INITIAL_K = Double3.ONE;

    public SimpleRayTracer(Scene scene, RayTracerType simple) {
        super(scene);
    }

    private Color calcColor(Intersection intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K);
    }

    private Color calcColor(Intersection intersection, Ray ray, int level, Double3 k) {
        if (intersection == null || level == 0 || k.lowerThan(MIN_CALC_COLOR_K))
            return scene.background;

        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        Color localColor = scene.ambientLight.getIntensity()
                .scale(intersection.material.kD)
                .add(intersection.geometry.getEmission())
                .add(calcColorLocalEffects(intersection));

        Color globalColor = calcGlobalEffects(intersection, level, k);

        Double3 kT = intersection.material.kT;
        Double3 kO = Double3.ONE.subtract(kT);

        return localColor.scale(kO).add(globalColor);

    }

    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.rayDir; // כיוון הגעה של הקרן
        Vector n = intersection.normal;
        double nv = alignZero(v.dotProduct(n));
        Vector r = v.subtract(n.scale(2 * nv)).normalize();

        Point point = intersection.point.add(n.scale(nv > 0 ? DELTA : -DELTA));
        return new Ray(point, r);
    }

    private Ray constructRefractedRay(Intersection intersection) {
        Vector v = intersection.rayDir;
        Vector n = intersection.normal;
        Point point = intersection.point.add(n.scale(alignZero(v.dotProduct(n)) > 0 ? DELTA : -DELTA));
        return new Ray(point, v);
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        if (level == 0 || k.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null)
            return scene.background;

        Color color = calcColor(intersection, ray, level - 1, k.product(kx));
        return color.scale(kx);
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Color color = Color.BLACK;

        Double3 kR = intersection.material.kR;
        if (!kR.lowerThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflectedRay(intersection);
            color = color.add(calcGlobalEffect(reflectedRay, level, k, kR));
        }

        Double3 kT = intersection.material.kT;
        if (!kT.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefractedRay(intersection);
            color = color.add(calcGlobalEffect(refractedRay, level, k, kT));
        }

        return color;
    }

    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty())
            return null;

        return ray.findClosestIntersection(intersections);
    }

    private boolean unshaded(Intersection intersection, LightSource lightSource) {
        Vector lightDirection = intersection.l.normalize();
        Vector epsVector = intersection.normal.scale(intersection.nl < 0 ? DELTA : -DELTA);
        Point newPoint = intersection.point.add(epsVector);
        Ray shadowRay = new Ray(newPoint, lightDirection);

        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(shadowRay);
        double lightDistance = lightSource.getDistance(intersection.point);

        if (intersections == null) {
            return true;
        }

        for (Intersection inter : intersections) {
            if (inter.geometry == intersection.geometry) continue;

            double interDist = inter.point.distance(intersection.point);
            Double3 kT = inter.material != null ? inter.material.kT : Double3.ZERO;

            if (interDist <= lightDistance && kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Color traceRay(Ray ray) {
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null)
            return scene.background;

        return calcColor(intersection, ray);
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

    private Color calcColorLocalEffects(Intersection intersection) {
        Color localColor = intersection.geometry.getEmission();

        Vector n = intersection.normal;
        Vector v = intersection.rayDir;
        double nv = alignZero(n.dotProduct(v));

        if (isZero(nv)) {
            return localColor;
        }

        Material material = intersection.material;

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) continue;
            if (!unshaded(intersection, lightSource)) continue;

            Color intensity = lightSource.getIntensity(intersection.point);
            Color diffusive = calcDiffusive(material.kD, intersection.l, intersection.normal, intensity);
            Color specular = calcSpecular(material.kS, intersection.l, intersection.normal,
                    intersection.rayDir, material.nShininess, intensity);
            localColor = localColor.add(diffusive).add(specular);
        }

        return localColor;
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, double nShininess, Color iL) {
        Vector r = l.subtract(n.scale(l.dotProduct(n)).scale(2)).normalize();
        double max = Math.max(0, -v.dotProduct(r));
        double maxNs = Math.pow(max, nShininess);
        Double3 ksMaxNs = ks.scale(maxNs);
        return iL.scale(ksMaxNs);
    }

    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color iL) {
        double lN = l.normalize().dotProduct(n.normalize());
        return iL.scale(kd.scale(Math.abs(lN)));
    }
}