package geometries;

import primitives.*;
import static primitives.Util.isZero;

import java.util.ArrayList;
import java.util.List;

/**
 * Cylinder class represents a 3D cylinder, defined by a radius, height, and a direction vector (axis).
 * It extends the Tube class.
 */
public class Cylinder extends Tube {
    private final double height;

    public Cylinder(double radius, double height, Ray axis) {
        super(radius, axis);
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        Vector axisDir = axisRay.getDirection();
        Point axisOrigin = axisRay.getOrigin();
        Vector centerToPoint = point.subtract(axisOrigin);
        double projectionLength = centerToPoint.dotProduct(axisDir);

        if (projectionLength <= 0) {
            return axisDir.scale(-1.0);
        }

        if (projectionLength >= height) {
            return axisDir;
        }

        Point projectionPoint = axisOrigin.add(axisDir.scale(projectionLength));
        return point.subtract(projectionPoint).normalize();
    }

    /**
     * Finds the intersection points of a ray with the finite cylinder,
     * filtering out points beyond maxDistance.
     *
     * @param ray         The ray to intersect with the cylinder.
     * @param maxDistance The maximum distance from the ray's origin to consider.
     * @return List of intersection points within bounds, or null if none.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersectable.Intersection> intersections = new ArrayList<>();

        Vector axisDirection = axisRay.getDirection();
        Point rayOrigin = ray.getOrigin();
        Vector rayDirection = ray.getDirection();
        Vector deltaP = rayOrigin.subtract(axisRay.getOrigin());

        double vDotVa = rayDirection.dotProduct(axisDirection);
        double deltaPDotVa = deltaP.dotProduct(axisDirection);

        Vector vMinusVa = rayDirection.subtract(axisDirection.scale(vDotVa));
        Vector deltaPMinusVa = deltaP.subtract(axisDirection.scale(deltaPDotVa));

        double a = vMinusVa.lengthSquared();
        double b = 2 * vMinusVa.dotProduct(deltaPMinusVa);
        double c = deltaPMinusVa.lengthSquared() - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0 || isZero(a)) {
            return null;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDiscriminant) / (2 * a);
        double t2 = (-b + sqrtDiscriminant) / (2 * a);

        // בדיקת t1
        if (t1 > 0) {
            Point p1 = ray.getPoint(t1);
            double projection1 = p1.subtract(axisRay.getOrigin()).dotProduct(axisDirection);
            if (projection1 >= 0 && projection1 <= height && rayOrigin.distance(p1) <= maxDistance) {
                intersections.add(new Intersectable.Intersection(this, p1));
            }
        }

        // בדיקת t2 (ולא זהה ל־t1)
        if (t2 > 0 && !isZero(t1 - t2)) {
            Point p2 = ray.getPoint(t2);
            double projection2 = p2.subtract(axisRay.getOrigin()).dotProduct(axisDirection);
            if (projection2 >= 0 && projection2 <= height && rayOrigin.distance(p2) <= maxDistance) {
                intersections.add(new Intersectable.Intersection(this, p2));
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
