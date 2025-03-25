package geometries;

import primitives.Point;

/**
 * Triangle class represents a triangle in 3D space, defined by three points.
 * It extends the Polygon class, which is already implemented to handle the general polygon behavior.
 */
public class Triangle extends Polygon {

    /**
     * Constructor that initializes a triangle using three points.
     * This constructor calls the Polygon constructor and ensures the shape is a triangle.
     *
     * @param p1 The first point of the triangle.
     * @param p2 The second point of the triangle.
     * @param p3 The third point of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor with 3 points.
    }
}
