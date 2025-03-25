package geometries;

import primitives.Point;

/**
 * The Triangle class represents a triangle in 3D space.
 * It is a specific case of a polygon with exactly three vertices.
 * This class extends the Polygon class, which provides general polygon functionality.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle using three points.
     * Since a triangle is a polygon with three vertices, this constructor
     * simply calls the constructor of the Polygon class.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor with three points.
    }
}
