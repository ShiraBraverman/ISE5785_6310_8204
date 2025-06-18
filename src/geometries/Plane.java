package geometries;

import primitives.*;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Plane class represents a 3D plane defined either by three points or by a point and a normal vector.
 */
public class Plane extends Geometry {

    private final Point point;   // A point on the plane
    private final Vector normal; // The normalized normal vector to the plane

    /**
     * Constructs a plane from three non-collinear points.
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        Vector normalUnnormalized = v1.crossProduct(v2);
        if (normalUnnormalized.length() == 0) {
            throw new IllegalArgumentException("The three points are collinear and cannot define a plane.");
        }

        this.point = p1;
        this.normal = normalUnnormalized.normalize();
    }

    /**
     * Constructs a plane from a point and a normal vector.
     */
    public Plane(Point point, Vector normal) {
        if (normal.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Normal vector cannot be a zero vector.");
        }

        this.point = point;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    public Point getPoint() {
        return point;
    }

    /**
     * Finds intersection point(s) of the plane with a given ray and optional max distance.
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Vector v = ray.getDirection();
        Point p0 = ray.getOrigin();

        double nv = normal.dotProduct(v);

        if (isZero(nv)) {
            return null; // Ray is parallel to the plane
        }

        double t = normal.dotProduct(point.subtract(p0)) / nv;

        if (t <= 0 || t > maxDistance) {
            return null; // No valid intersection
        }

        Point p = ray.getPoint(t);
        return List.of(new Intersection(this, p));
    }


}
