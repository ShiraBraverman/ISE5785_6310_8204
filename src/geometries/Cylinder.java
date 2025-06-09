package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cylinder class represents a 3D cylinder, defined by a radius, height, and a direction vector (axis).
 * It extends the Tube class.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     * Defines the vertical dimension of the cylinder along its axis.
     */
    private final double height;

    /**
     * Constructor that initializes a cylinder with a radius, height, and axis.
     *
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     * @param axis   The direction vector of the cylinder's axis (must be normalized).
     * @throws IllegalArgumentException If the height is less than or equal to 0.
     */
    public Cylinder(double radius, double height, Ray axis) {
        super(radius, axis);  // Call the constructor of Tube to initialize radius and axis
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
        this.height = height;
    }

    /**
     * Returns the normal vector of the cylinder at a given point.
     * The normal vector is calculated by projecting the vector from the point on the cylinder
     * to the axis of the cylinder, and then subtracting the projected point from the given point.
     * If the point is on the sides of the cylinder, the normal vector is perpendicular to the surface.
     * If the point is on the top or bottom base, the normal vector is along the cylinder's axis.
     *
     * @param point The point on the surface of the cylinder.
     * @return The normal vector to the cylinder at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        Vector axisDir = axisRay.getDirection();
        Point axisOrigin = axisRay.getOrigin();
        Vector centerToPoint = point.subtract(axisOrigin);
        double projectionLength = centerToPoint.dotProduct(axisDir);

        // Handle the base case (point is on the bottom base of the cylinder)
        if (projectionLength <= 0) {
            // אם הנקודה היא בדיוק במרכז הבסיס, נחזיר את הנורמל בכיוון הפוך
            if (point.equals(axisOrigin)) {
                return axisDir.scale(-1.0);  // Normal points downward from the base
            }
            return axisDir.scale(-1.0);  // Normal points downward from the base
        }

        // Handle the top base case (point is on the top base)
        if (projectionLength >= height) {
            // אם הנקודה היא במרכז הבסיס העליון, נחזיר את הנורמל
            Point topBaseCenter = axisOrigin.add(axisDir.scale(height));
            if (point.equals(topBaseCenter)) {
                return axisDir;  // Normal points upward from the top base
            }
            return axisDir;  // Normal points upward from the top base
        }

        // Handle the side of the cylinder (point is on the lateral surface)
        Point projectionPoint = axisOrigin.add(axisDir.scale(projectionLength));  // Calculate the projected point on the axis
        return point.subtract(projectionPoint).normalize();  // Return the normalized vector from the projected point to the given point
    }

    /**
     * Finds the intersection points of a ray with the cylinder.
     * The cylinder is considered infinite, and the function checks if the intersection points
     * are within the height of the cylinder. If they are, they are added to the list of intersection points.
     * <p>
     * The method solves the quadratic equation representing the intersection of the ray with the
     * infinite cylinder and checks if the points are within the valid height range of the cylinder.
     *
     * @param ray The ray to check for intersections with the cylinder.
     * @return A list of intersection points between the ray and the cylinder. If no intersections are found,
     * an empty list is returned.
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersectable.Intersection> intersections = new ArrayList<>();

        Vector p0ToRayOrigin = ray.getOrigin().subtract(axisRay.getOrigin());
        Vector axisDirection = axisRay.getDirection();

        double a = ray.getDirection().dotProduct(ray.getDirection()) - Math.pow(ray.getDirection().dotProduct(axisDirection), 2);
        double b = 2 * (ray.getDirection().dotProduct(p0ToRayOrigin) - (ray.getDirection().dotProduct(axisDirection) * p0ToRayOrigin.dotProduct(axisDirection)));
        double c = p0ToRayOrigin.dotProduct(p0ToRayOrigin) - Math.pow(p0ToRayOrigin.dotProduct(axisDirection), 2) - Math.pow(radius, 2);

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return intersections;  // No intersections
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDiscriminant) / (2 * a);
        double t2 = (-b + sqrtDiscriminant) / (2 * a);

        Point p1 = ray.getOrigin().add(ray.getDirection().scale(t1));
        Point p2 = ray.getOrigin().add(ray.getDirection().scale(t2));

        double projection1 = p1.subtract(axisRay.getOrigin()).dotProduct(axisDirection);
        double projection2 = p2.subtract(axisRay.getOrigin()).dotProduct(axisDirection);

        if (projection1 >= 0 && projection1 <= height) {
            intersections.add(new Intersectable.Intersection(this, p1));
        }
        if (projection2 >= 0 && projection2 <= height) {
            intersections.add(new Intersectable.Intersection(this, p2));
        }

        return intersections;
    }
}
