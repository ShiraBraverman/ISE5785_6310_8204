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
      Vector n = plane.getNormal(vertices.get(0));

      Vector v1 = vertices.get(vertices.size() - 1).subtract(point);
      Vector v2 = vertices.get(0).subtract(point);
      Vector cross = v1.crossProduct(v2);
      double sign = alignZero(n.dotProduct(cross));

      if (isZero(sign)) {
         return false;
      }

      boolean positive = sign > 0;

      for (int i = 1; i < vertices.size(); ++i) {
         v1 = v2;
         v2 = vertices.get(i).subtract(point);
         if (v1.length() == 0 || v2.length() == 0) {
            return true;
         }
         try {
            cross = v1.crossProduct(v2);
         } catch (IllegalArgumentException e) {
            return false;
         }

         if (isZero(cross.length())) {
            // Check if the point is actually on the edge segment
            double dotProduct = v1.dotProduct(v2);
            if (dotProduct < 0) {
               return true; // point is between the two vertices
            } else {
               return false; // point is on the extension beyond the edge
            }
         }

         sign = alignZero(n.dotProduct(cross));

         if (isZero(sign)) {
            return false;
         }

         if ((sign > 0) != positive) {
            return false;
         }
      }

      return true;
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
      // Vector representing the edge of the polygon
      Vector edgeVector = p2.subtract(p1);

      // Vector from the ray's origin to the first point of the edge
      Vector rayToEdge = p1.subtract(ray.getOrigin());

      // Calculate the normal of the plane formed by the ray and the edge
      Vector rayDirection = ray.getDirection();
      Vector crossProduct = rayDirection.crossProduct(edgeVector);

      // Check if the vectors are parallel (cross product = 0 means parallel)
      if (isZero(crossProduct.length())) {
         return false; // If the vectors are parallel, no intersection
      }

      // Find the intersection point using the determinant method
      double t = rayToEdge.crossProduct(edgeVector).length() / crossProduct.length();
      double u = rayToEdge.crossProduct(rayDirection).length() / crossProduct.length();

      // Check if the intersection point lies within the bounds of the edge
      if (t >= 0 && u >= 0 && u <= 1) {
         return true; // The intersection point is within the bounds of the edge
      }

      return false; // No intersection
   }
}