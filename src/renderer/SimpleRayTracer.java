package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.Intersection;

import java.util.List;

import static primitives.Util.*;

public class SimpleRayTracer extends RayTracerBase {
    private static final Double3 MIN_CALC_COLOR_K = new Double3(0.001);
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final Double3 INITIAL_K = Double3.ONE;
    private static final double DELTA = 0.1;

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null)
            return scene.background;
        return calcColor(intersection, ray);
    }

    private Color calcColor(Intersection intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K);
    }

    private Color calcColor(Intersection intersection, Ray ray, int level, Double3 k) {
        if (intersection == null || level == 0 || k.lowerThan(MIN_CALC_COLOR_K))
            return scene.background;

        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        Color localColor = scene.ambientLight.getIntensity().scale(intersection.material.kD)
                .add(intersection.geometry.getEmission())
                .add(calcColorLocalEffects(intersection));

        Color globalColor = calcGlobalEffects(intersection, level, k);

        return localColor.add(globalColor);
    }

    private boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir; // היה scale(-1)
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(rayDir));
        if (nv > 0) {
            n = n.scale(-1);
            nv = -nv;
        }
        intersection.normal = n.normalize();
        intersection.vNormal = nv;
        return !isZero(nv);
    }


    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.l = lightSource.getL(intersection.point).normalize();
        intersection.nl = alignZero(intersection.l.dotProduct(intersection.normal));
        return intersection.nl > 0;
    }

    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        Material material = intersection.material;

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) continue;
            if (!unshaded(intersection, lightSource)) continue;

            Color intensity = lightSource.getIntensity(intersection.point);

            Color diffusive = calcDiffusive(material.kD, intersection.l, intersection.normal, intensity);
            Color specular = calcSpecular(material.kS, intersection.l, intersection.normal,
                    intersection.rayDir, material.nShininess, intensity);

            color = color.add(diffusive).add(specular);
        }

        return color;
    }

    private boolean unshaded(Intersection intersection, LightSource lightSource) {
        Vector lightDirection = intersection.l;
        Vector epsVector = intersection.normal.scale(intersection.nl < 0 ? DELTA : -DELTA);
        Point newPoint = intersection.point.add(epsVector);
        Ray shadowRay = new Ray(newPoint, lightDirection);
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(shadowRay);
        double lightDistance = lightSource.getDistance(intersection.point);

        if (intersections == null) return true;

        for (Intersection inter : intersections) {
            if (inter.geometry == intersection.geometry) continue;
            double dist = inter.point.distance(intersection.point);
            if (alignZero(dist - lightDistance) <= 0 && inter.material.kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }

        return true;
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, double nShininess, Color iL) {
        Vector r = l.subtract(n.scale(2 * l.dotProduct(n))).normalize();
        double max = Math.max(0, r.dotProduct(v)); // נקודת highlight תלויה בזה!
        return iL.scale(ks.scale(Math.pow(max, nShininess)));
    }

    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color iL) {
        return iL.scale(kd.scale(Math.abs(l.normalize().dotProduct(n.normalize()))));
    }

    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.rayDir;
        Vector n = intersection.normal;
        double nv = alignZero(v.dotProduct(n));
        Vector r = v.subtract(n.scale(2 * nv)).normalize();
        Vector delta = n.scale(intersection.vNormal < 0 ? DELTA : -DELTA);
        Point newPoint = intersection.point.add(delta);
        return new Ray(newPoint, r, n);
    }

    private Ray constructRefractedRay(Intersection intersection) {
        Vector delta = intersection.normal.scale(intersection.vNormal < 0 ? DELTA : -DELTA);
        Point newPoint = intersection.point.add(delta);
        return new Ray(newPoint, intersection.rayDir, intersection.normal);
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Double3 kR = intersection.material.kR;
        Double3 kT = intersection.material.kT;

        Color reflectedColor = Color.BLACK;
        if (!kR.lowerThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflectedRay(intersection);
            reflectedColor = calcGlobalEffect(reflectedRay, level, k, kR);
        }

        Color refractedColor = Color.BLACK;
        if (!kT.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefractedRay(intersection);
            refractedColor = calcGlobalEffect(refractedRay, level, k, kT);
        }

        return reflectedColor.add(refractedColor);
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        if (level == 0 || k.product(kx).lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null)
            return scene.background;

        return calcColor(intersection, ray, level - 1, k.product(kx)).scale(kx);
    }

    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty()) return null;
        return ray.findClosestIntersection(intersections);
    }
}
