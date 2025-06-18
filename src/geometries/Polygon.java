package geometries;

import static java.lang.Double.*;
import java.util.List;
import static primitives.Util.*;
import primitives.*;

import java.util.ArrayList;

/**
 * The Polygon class represents a two-dimensional polygon in a 3D Cartesian coordinate system.
 * The polygon is defined by a list of ordered vertices and must be convex.
 */
public class Polygon extends Geometry {
   protected final List<Point> vertices;
   protected final Plane plane;
   private final int size;

   public Polygon(Point... vertices) {
      if (vertices.length < 3)
         throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
      this.vertices = List.of(vertices);
      size = vertices.length;

      plane = new Plane(vertices[0], vertices[1], vertices[2]);
      if (size == 3) return;

      Vector n = plane.getNormal(vertices[0]);
      Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
      Vector edge2 = vertices[0].subtract(vertices[size - 1]);

      boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
      for (var i = 1; i < size; ++i) {
         if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
            throw new IllegalArgumentException("All vertices of a polygon must lie in the same plane");

         edge1 = edge2;
         edge2 = vertices[i].subtract(vertices[i - 1]);
         if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
            throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
      }
   }

   @Override
   public Vector getNormal(Point point) {
      return plane.getNormal(point);
   }

   @Override
   protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
      List<Intersectable.Intersection> planeIntersections = plane.calculateIntersections(ray, maxDistance);
      if (planeIntersections == null) return null;

      List<Intersectable.Intersection> intersections = new ArrayList<>();
      for (Intersectable.Intersection inter : planeIntersections) {
         Point p = inter.point;
         if (isPointInPolygon(p)) {
            intersections.add(new Intersectable.Intersection(this, p));
         }
      }

      return intersections.isEmpty() ? null : intersections;
   }

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
            double dotProduct = v1.dotProduct(v2);
            return dotProduct < 0;
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

   private boolean doIntersect(Ray ray, Point p1, Point p2) {
      Vector edgeVector = p2.subtract(p1);
      Vector rayToEdge = p1.subtract(ray.getOrigin());
      Vector rayDirection = ray.getDirection();
      Vector crossProduct = rayDirection.crossProduct(edgeVector);

      if (isZero(crossProduct.length())) {
         return false;
      }

      double t = rayToEdge.crossProduct(edgeVector).length() / crossProduct.length();
      double u = rayToEdge.crossProduct(rayDirection).length() / crossProduct.length();

      return t >= 0 && u >= 0 && u <= 1;
   }
}
