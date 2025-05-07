package geometries;

import static java.lang.Double.*;
import java.util.List;
import static primitives.Util.*;
import primitives.*;

/**
 * The Polygon class represents a two-dimensional polygon in a 3D Cartesian coordinate system.
 * The polygon is defined by a list of ordered vertices and must be convex.
 */
public class Polygon extends Geometry {
   /**
    * List of polygon's vertices
    */
   protected final List<Point> vertices;
   /**
    * Associated plane in which the polygon lies
    */
   protected final Plane plane;
   /**
    * The number of vertices in the polygon
    */
   private final int size;

   /**
    * Constructs a polygon using an ordered list of vertices.
    * The polygon must be convex and all vertices must lie in the same plane.
    *
    * @param vertices List of vertices in order, forming the polygon.
    * @throws IllegalArgumentException If:
    *                                  <ul>
    *                                  <li>Less than 3 vertices are provided</li>
    *                                  <li>Two consecutive vertices are identical</li>
    *                                  <li>The vertices do not lie in the same plane</li>
    *                                  <li>The polygon is concave</li>
    *                                  </ul>
    */
   public Polygon(Point... vertices) {
      if (vertices.length < 3)
         throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
      this.vertices = List.of(vertices);
      size = vertices.length;

      // Create the plane based on the first three vertices
      plane = new Plane(vertices[0], vertices[1], vertices[2]);

      // If the polygon is a triangle, no further checks are required
      if (size == 3) return;

      // Retrieve the normal vector of the plane
      Vector n = plane.getNormal(vertices[0]);

      // Initialize edge vectors
      Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
      Vector edge2 = vertices[0].subtract(vertices[size - 1]);

      // Determine convexity using the sign of the cross product with the normal
      boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
      for (var i = 1; i < size; ++i) {
         // Ensure all vertices lie in the same plane
         if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
            throw new IllegalArgumentException("All vertices of a polygon must lie in the same plane");

         // Check if the polygon is convex
         edge1 = edge2;
         edge2 = vertices[i].subtract(vertices[i - 1]);
         if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
            throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
      }
   }

   /**
    * Returns the normal vector to the polygon at a given point.
    *
    * @param point A point on the polygon.
    * @return The normal vector to the polygon.
    */
   @Override
   public Vector getNormal(Point point) {
      return plane.getNormal(point);
   }

   /**
    * This method returns the list of intersection points between a given ray and the polygon.
    *
    * @param ray The ray to check for intersections.
    * @return A list of points where the ray intersects the polygon, or null if there are no intersections.
    */
   @Override
   public List<Point> findIntersections(Ray ray) {
      // Step 1: Check intersection with the plane of the polygon
      List<Point> intersections = plane.findIntersections(ray);
      if (intersections == null || intersections.isEmpty()) {
         return null; // If no intersection with the plane, return null (no intersection with the polygon)
      }

      // Step 2: Check if the intersection point is inside the polygon
      Point intersectionPoint = intersections.get(0); // We know there's only one point on the plane
      if (isPointInPolygon(intersectionPoint)) {
         return List.of(intersectionPoint); // If the point is inside the polygon, return it
      }

      return null; // If the point is not inside the polygon, no intersection
   }

   /**
    * This helper method checks if a point is inside the polygon.
    * The method uses the ray-casting algorithm to check if the point is inside the polygon.
    *
    * @param point The point to check.
    * @return true if the point is inside the polygon, false otherwise.
    */
   protected boolean isPointInPolygon(Point point) {
      int intersectionsCount = 0;
      Ray ray = new Ray(point, new Vector(1, 0, 0)); // A ray in any direction (here along the x-axis)

      // Loop through each edge of the polygon to check for intersections
      for (int i = 0; i < size; i++) {
         Point p1 = vertices.get(i);
         Point p2 = vertices.get((i + 1) % size);

         // Check if the ray intersects with the edge of the polygon
         if (doIntersect(ray, p1, p2)) {
            intersectionsCount++;
         }
      }

      // If the number of intersections is odd, the point is inside the polygon
      return intersectionsCount % 2 != 0;
   }

   /**
    * This method checks if the ray intersects with the edge of the polygon.
    *
    * @param ray The ray to check for intersection.
    * @param p1  The first point of the edge.
    * @param p2  The second point of the edge.
    * @return true if the ray intersects with the edge, false otherwise.
    */
   private boolean doIntersect(Ray ray, Point p1, Point p2) {
      // Here you need to implement the logic for intersection between the ray and the edge of the polygon.
      // This is just a placeholder, and you would need to implement the actual intersection logic.
      return false; // Placeholder, implement the actual intersection check
   }
}