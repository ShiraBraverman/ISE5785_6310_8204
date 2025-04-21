package geometries;

import primitives.*;

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
}
