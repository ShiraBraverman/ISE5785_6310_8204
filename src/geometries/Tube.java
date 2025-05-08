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
    public List<Point> findIntersections(Ray ray) {
        return null;
//        Vector vAxis = axisRay.getDirection(); // כיוון הציר
//        Point p0 = ray.getOrigin(); // נקודת ההתחלה של הקרן
//        Vector v = ray.getDirection(); // כיוון הקרן
//        Point pa = axisRay.getOrigin(); // נקודת ההתחלה של הצינור
//
//        Vector deltaP;
//        try {
//            deltaP = p0.subtract(pa); // חישוב וקטור ההפרש בין נקודת ההתחלה של הקרן ונקודת ההתחלה של הצינור
//        } catch (IllegalArgumentException e) {
//            deltaP = new Vector(0, 0, 0); // אם נקודת ההתחלה של הקרן שווה לזו של הצינור
//        }
//
//        double vVa = v.dotProduct(vAxis); // חישוב v * va
//        Vector vMinusVa = v;
//        if (!isZero(vVa)) {
//            vMinusVa = v.subtract(vAxis.scale(vVa)); // v - v*va*va
//        }
//
//        double deltaPVa = deltaP.dotProduct(vAxis); // חישוב deltaP * va
//        Vector deltaPMinusVa = deltaP;
//        if (!isZero(deltaPVa)) {
//            deltaPMinusVa = deltaP.subtract(vAxis.scale(deltaPVa)); // deltaP - deltaP*va*va
//        }
//
//        double A = vMinusVa.lengthSquared(); // A = (v - v*va*va)^2
//        double B = 2 * vMinusVa.dotProduct(deltaPMinusVa); // B = 2 * (v - v*va*va) * (deltaP - deltaP*va*va)
//        double C = deltaPMinusVa.lengthSquared() - radius * radius; // C = (deltaP - deltaP*va*va)^2 - R^2
//
//        double discriminant = B * B - 4 * A * C; // חישוב הדיסקרימיננטה
//        if (discriminant < 0) {
//            return null; // אם אין חיתוכים
//        }
//
//        double sqrtDiscriminant = Math.sqrt(discriminant); // חישוב שורש הדיסקרימיננטה
//        double t1 = (-B + sqrtDiscriminant) / (2 * A); // חישוב t1
//        double t2 = (-B - sqrtDiscriminant) / (2 * A); // חישוב t2
//
//        List<Point> intersections = new ArrayList<>();
//        if (t1 >= 0) intersections.add(ray.getPoint(t1)); // אם t1 חיובי, הוסף את הנקודה
//        if (t2 >= 0 && !isZero(t1 - t2)) intersections.add(ray.getPoint(t2)); // אם t2 חיובי ושונה מ-t1, הוסף את הנקודה
//
//        return intersections.isEmpty() ? null : intersections; // החזר אם יש חיתוכים, אחרת null
    }







}
