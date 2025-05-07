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
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = new ArrayList<>();

        // Vector from the ray origin to the axis origin
        Vector p0ToRayOrigin = ray.getOrigin().subtract(axisRay.getOrigin());
        Vector axisDirection = axisRay.getDirection();

        // Project the ray direction onto the axis direction (this is needed for the cylinder's axis)
        double a = ray.getDirection().dotProduct(ray.getDirection()) - Math.pow(ray.getDirection().dotProduct(axisDirection), 2);
        double b = 2 * (ray.getDirection().dotProduct(p0ToRayOrigin) - (ray.getDirection().dotProduct(axisDirection) * p0ToRayOrigin.dotProduct(axisDirection)));
        double c = p0ToRayOrigin.dotProduct(p0ToRayOrigin) - Math.pow(p0ToRayOrigin.dotProduct(axisDirection), 2) - Math.pow(radius, 2);

        // Solve the quadratic equation for intersection points
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return intersections;  // No intersection
        }

        // Calculate the two solutions (the points where the ray intersects the infinite cylinder)
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);

        // Calculate the intersection points for t1 and t2
        Point p1 = ray.getOrigin().add(ray.getDirection().scale(t1));
        Point p2 = ray.getOrigin().add(ray.getDirection().scale(t2));

        // Check if the intersection points are within the height of the cylinder
        Vector p1ToAxis = p1.subtract(axisRay.getOrigin());
        Vector p2ToAxis = p2.subtract(axisRay.getOrigin());
        double projection1 = p1ToAxis.dotProduct(axisDirection);
        double projection2 = p2ToAxis.dotProduct(axisDirection);

        // Check if the points are within the valid height range of the cylinder
        if (projection1 >= 0 && projection1 <= height) {
            intersections.add(p1);
        }
        if (projection2 >= 0 && projection2 <= height) {
            intersections.add(p2);
        }

        return intersections;
    }
}
