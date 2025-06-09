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
        super(radius); // Call the constructor of RadialGeometry to initialize the radius
        this.center = center;
    }

    /**
     * Returns the normal vector of the sphere at a given point.
     * The normal is the vector from the center of the sphere to the given point,
     * normalized to unit length.
     *
     * @param point The point on the surface of the sphere
     * @return The normal vector from the center of the sphere to the given point
     */
    @Override
    public Vector getNormal(Point point) {
        // The normal to the sphere is the vector from the center to the point, normalized
        return point.subtract(center).normalize();
    }

    /**
     * Finds the intersection points of the sphere with a given ray.
     *
     * @param ray The ray to check for intersections.
     * @return A list of points where the ray intersects the sphere, or an empty list if no intersection.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector v = ray.getDirection();
        Point p0 = ray.getOrigin();

        if (p0.equals(center)) {
            Point p = p0.add(v.scale(radius));
            return List.of(new Intersectable.Intersection(this, p));
        }
        Vector u = center.subtract(p0);
        double tm = alignZero(v.dotProduct(u));
        double d2 = alignZero(u.lengthSquared() - tm * tm);

        if (alignZero(d2 - radius * radius) > 0) {
            return null;
        }

        double th = alignZero(Math.sqrt(radius * radius - d2));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        List<Intersectable.Intersection> intersections = new ArrayList<>();

        if (t1 > 0) {
            intersections.add(new Intersectable.Intersection(this, ray.getPoint(t1)));
        }
        if (t2 > 0) {
            intersections.add(new Intersectable.Intersection(this, ray.getPoint(t2)));
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
