package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Tube class represents a 3D tube, defined by a radius and a central axis (Ray).
 */
public class Tube extends RadialGeometry {

    /**
     * The axis ray of the tube (defines the direction and position).
     */
    protected final Ray axisRay;

    /**
     * Constructor that initializes a tube with a radius and an axis ray.
     *
     * @param radius  The radius of the tube.
     * @param axisRay The axis ray (point and direction) of the tube.
     */
    public Tube(double radius, Ray axisRay) {
        super(radius);
        this.axisRay = axisRay;
    }

    /**
     * Returns the normal vector of the tube at a given point.
     * The normal is calculated by projecting the point onto the axis,
     * finding the closest point on the axis, and then calculating the vector
     * from that point to the given point.
     *
     * @param point The point on the surface of the tube
     * @return The normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        Point p0 = axisRay.getOrigin();
        Vector v = axisRay.getDirection();

        Vector p0ToPoint = point.subtract(p0);
        double t = p0ToPoint.dotProduct(v);

        Point o = p0.add(v.scale(t)); // Closest point on the axis

        Vector normal = point.subtract(o);
        if (normal.length() == 0) {
            return v;
        }

        return normal.normalize();
    }

    /**
     * Returns the axis ray of the tube.
     *
     * @return The axis ray.
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    /**
     * Finds the intersections between the tube and a given ray.
     * The intersections are calculated by solving the quadratic equation
     * that arises from the ray's parametric equation and the tube's equation.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersectable.Intersection> intersections = new ArrayList<>();

        Vector vAxis = axisRay.getDirection();
        Point p0 = ray.getOrigin();
        Vector v = ray.getDirection();
        Point pa = axisRay.getOrigin();

        if (p0.equals(pa)) {
            return null;
        }

        Vector deltaP;
        try {
            deltaP = p0.subtract(pa);
        } catch (IllegalArgumentException e) {
            return null;
        }

        double vVa = v.dotProduct(vAxis);

        Vector vMinusVa;
        try {
            vMinusVa = v.subtract(vAxis.scale(vVa));
        } catch (IllegalArgumentException e) {
            return null;
        }

        double deltaPVa = deltaP.dotProduct(vAxis);

        Vector deltaPMinusVa;
        try {
            deltaPMinusVa = deltaP.subtract(vAxis.scale(deltaPVa));
        } catch (IllegalArgumentException e) {
            return null;
        }

        double A = vMinusVa.lengthSquared();

        if (isZero(A)) {
            return null;
        }

        double B = 2 * vMinusVa.dotProduct(deltaPMinusVa);
        double C = deltaPMinusVa.lengthSquared() - radius * radius;

        double discriminant = B * B - 4 * A * C;

        if (discriminant < 0) {
            return null;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-B + sqrtDiscriminant) / (2 * A);
        double t2 = (-B - sqrtDiscriminant) / (2 * A);

        if (t1 > 0) {
            Point p1 = ray.getPoint(t1);
            intersections.add(new Intersectable.Intersection(this, p1));
        }
        if (t2 > 0 && !isZero(t1 - t2)) {
            Point p2 = ray.getPoint(t2);
            intersections.add(new Intersectable.Intersection(this, p2));
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
