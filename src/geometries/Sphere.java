package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Sphere class represents a 3D sphere, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center of the sphere
     */
    private final Point center;

    /**
     * Constructor that initializes a sphere with a center point and a radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector of the sphere at a given point.
     *
     * @param point The point on the surface of the sphere
     * @return The normal vector from the center of the sphere to the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Finds the intersection points of the sphere with a given ray,
     * limited to points within the maxDistance.
     *
     * @param ray The ray to check for intersections.
     * @param maxDistance The maximum allowed distance for intersections.
     * @return A list of intersection points or null if none.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Vector v = ray.getDirection();
        Point p0 = ray.getOrigin();

        if (p0.equals(center)) {
            Point p = p0.add(v.scale(radius));
            if (alignZero(radius) <= maxDistance) {
                return List.of(new Intersectable.Intersection(this, p));
            }
            return null;
        }

        Vector u = center.subtract(p0);
        double tm = alignZero(v.dotProduct(u));
        double d2 = alignZero(u.lengthSquared() - tm * tm);

        double rSquared = radius * radius;
        if (alignZero(d2 - rSquared) > 0) {
            return null;
        }

        double th = alignZero(Math.sqrt(rSquared - d2));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        List<Intersectable.Intersection> intersections = new ArrayList<>();

        if (t1 > 0 && t1 <= maxDistance) {
            intersections.add(new Intersectable.Intersection(this, ray.getPoint(t1)));
        }
        if (t2 > 0 && t2 <= maxDistance) {
            intersections.add(new Intersectable.Intersection(this, ray.getPoint(t2)));
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
