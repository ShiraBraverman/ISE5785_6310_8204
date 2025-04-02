package unittests.geometries;

import geometries.Cylinder;
import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Cylinder} class.
 * These tests verify the proper behavior of cylinder creation and functionality under different scenarios.
 */
class CylinderTests {

    /**
     * Test constructor for Cylinder.
     * Verifies that cylinders are created correctly with valid parameters and that
     * appropriate exceptions are thrown for invalid parameters such as zero or negative height.
     */
    @Test
    void testConstructor() {
        // Valid cylinder
        assertDoesNotThrow(() -> new Cylinder(2.0, 5.0, new Vector(0, 0, 1)), "Failed constructing a correct cylinder");

        // Invalid cylinder with zero height
        assertThrows(IllegalArgumentException.class, () -> new Cylinder(2.0, 0.0, new Vector(0, 0, 1)), "Expected exception for zero height");

        // Invalid cylinder with negative height
        assertThrows(IllegalArgumentException.class, () -> new Cylinder(2.0, -3.0, new Vector(0, 0, 1)), "Expected exception for negative height");
    }

    /**
     * Test getNormal(Point) method for Cylinder.
     * Verifies that the normal vectors are calculated correctly for different points on the cylinder,
     * including points on the curved surface, bottom base, top base, and special boundary cases.
     */
    @Test
    void testGetNormal() {
        Cylinder cylinder = new Cylinder(2.0, 5.0, new Vector(0, 0, 1));

        // EP1: Point on the curved surface
        Point p1 = new Point(2, 0, 2);
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(p1), "Wrong normal for point on curved surface");

        // EP2: Point on the bottom base
        Point p2 = new Point(1, 1, 0);
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(p2), "Wrong normal for point on bottom base");

        // EP3: Point on the top base
        Point p3 = new Point(-1, -1, 5);
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(p3), "Wrong normal for point on top base");

        // BVA1: Center of bottom base
        Point p4 = new Point(0, 0, 0);
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(p4), "Wrong normal for center of bottom base");

        // BVA2: Center of top base
        Point p5 = new Point(0, 0, 5);
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(p5), "Wrong normal for center of top base");

        // BVA3: Edge between bottom base and lateral surface
        Point p6 = new Point(2, 0, 0);
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(p6), "Wrong normal for edge between bottom base and lateral surface");

        // BVA4: Edge between top base and lateral surface
        Point p7 = new Point(2, 0, 5);
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(p7), "Wrong normal for edge between top base and lateral surface");
    }
}