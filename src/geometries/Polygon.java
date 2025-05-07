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
   /** List of polygon's vertices */
   protected final List<Point> vertices;
   /** Associated plane in which the polygon lies */
   protected final Plane plane;
   /** The number of vertices in the polygon */
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

}
