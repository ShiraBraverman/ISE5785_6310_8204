package geometries;

import primitives.Double3;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static primitives.Util.isZero;

/**
 * This class represents a plane in 3D space, defined by a point and a normal vector.
 * It can be initialized using either three points or a point and a normal vector.
 */
public class Plane extends Geometry {

    private final Point point;
    private final Vector normal;

    /**
     * Constructor that initializes the plane based on three points.
     * This constructor calculates the normal vector by taking the cross product of two vectors
     * defined by the three points, and stores one of the points as the reference point for the plane.
     *
     * @param p1 The first point defining the plane.
     * @param p2 The second point defining the plane.
     * @param p3 The third point defining the plane.
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        System.out.println("Creating Plane from 3 points:");
        System.out.println("v1: " + v1 + ", v2: " + v2);

        Vector normalUnnormalized = v1.crossProduct(v2);

        System.out.println("crossProduct (unnormalized normal): " + normalUnnormalized);

        if (normalUnnormalized.length() == 0) {
            throw new IllegalArgumentException("The three points are collinear and cannot define a plane.");
        }

        this.point = p1;
        this.normal = normalUnnormalized.normalize();
    }

    /**
     * Constructor that initializes the plane with a point and a normal vector.
     * The normal vector must be normalized.
     *
     * @param point  A point on the plane.
     * @param normal The normal vector to the plane (not necessarily normalized).
     */
    public Plane(Point point, Vector normal) {
        System.out.println("Creating Plane with point: " + point + ", and normal: " + normal);

        if (normal.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Normal vector cannot be a zero vector");
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

    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector normal = this.normal;
        Point rayOrigin = ray.getOrigin();
        Vector rayDirection = ray.getDirection();

        double denom = normal.dotProduct(rayDirection);
        System.out.println("denom = " + denom + " (isZero = " + isZero(denom) + ")");

        // אם הקרן מקבילה למישור – אין חיתוך
        if (isZero(denom)) {
            System.out.println("Ray is parallel to the plane.");
            return null;
        }

        double num = normal.dotProduct(this.point.subtract(rayOrigin));
        System.out.println("num = " + num);

        double t = num / denom;
        System.out.println("t = " + t);

        // חיתוך "קדימה" בלבד (קרן מכוונת לעבר המישור)
        // בדיקה אם הקרן מכוונת לעבר המישור (denom*num > 0) וגם נקודת החיתוך קדימה (t > 0)
        if (t > 0 && denom * num > 0) {
            return List.of(ray.getPoint(t));
        }

        // חיתוך מאחורי התחלת הקרן או בכיוון הלא נכון
        System.out.println("Intersection is behind the ray's origin or in wrong direction.");
        return null;
    }
}