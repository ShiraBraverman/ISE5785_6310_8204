package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

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
    public List<Point> findIntersections(Ray ray) {
        // Define the vector from the tube's axis to the ray's origin
        Vector axisDirection = axisRay.getDirection();
        Point rayOrigin = ray.getOrigin();
        Vector rayDirection = ray.getDirection();
        Point tubeOrigin = axisRay.getOrigin();
        Vector axisToRay = rayOrigin.subtract(tubeOrigin);

        // Perpendicular vector between the ray and the tube axis
        Vector axisToRayPerpendicular = axisToRay.subtract(axisDirection.scale(axisToRay.dotProduct(axisDirection) / axisDirection.lengthSquared()));

        // Define the quadratic equation terms
        double a = rayDirection.subtract(axisDirection.scale(rayDirection.dotProduct(axisDirection) / axisDirection.lengthSquared())).lengthSquared();
        double b = 2 * rayDirection.subtract(axisDirection.scale(rayDirection.dotProduct(axisDirection) / axisDirection.lengthSquared())).dotProduct(axisToRayPerpendicular);
        double c = axisToRayPerpendicular.lengthSquared() - radius * radius;

        // Compute the discriminant
        double discriminant = b * b - 4 * a * c;

        // If the discriminant is negative, there are no intersections
        if (discriminant < 0) {
            return null;
        }

        // Calculate the intersection points using the quadratic formula
        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        List<Point> intersections = new ArrayList<>();

        // If t1 and t2 are valid (positive and within range), add the points
        if (t1 > 0) {
            intersections.add(ray.getPoint(t1));
        }
        if (t2 > 0) {
            intersections.add(ray.getPoint(t2));
        }

        return intersections;
    }
}
