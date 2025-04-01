package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import primitives.Vector;


/**
 * Unit tests for {@link Vector} class.
 */
class VectorTests {

    /**
     * Test method for {@link Vector#add(Vector)}.
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-1, -2, -3);
        Vector v3 = new Vector(2, 3, 4);

        assertEquals(new Vector(3, 5, 7), v1.add(v3), "Add function failed");
        assertThrows(IllegalArgumentException.class, () -> v1.add(v2), "Adding inverse vectors should throw exception");
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    void testScale() {
        Vector v = new Vector(1, -2, 3);

        assertEquals(new Vector(2, -4, 6), v.scale(2), "Scale function failed");
        assertThrows(IllegalArgumentException.class, () -> v.scale(0), "Scaling by zero should throw exception");
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        assertEquals(-28, v1.dotProduct(v2), "Dot product calculation is incorrect");
        assertEquals(0, v1.dotProduct(v3), "Dot product should be zero for orthogonal vectors");
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(0, 3, -2);

        Vector cross = v1.crossProduct(v2);
        assertEquals(0, cross.dotProduct(v1), "Cross product should be orthogonal to the first operand");
        assertEquals(0, cross.dotProduct(v2), "Cross product should be orthogonal to the second operand");
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1.scale(2)), "Parallel vectors should throw exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        Vector v = new Vector(1, 2, 2);
        assertEquals(9, v.lengthSquared(), "Length squared calculation is incorrect");
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    void testLength() {
        Vector v = new Vector(0, 3, 4);
        assertEquals(5, v.length(), "Length calculation is incorrect");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector normalized = v.normalize();
        assertEquals(1, normalized.length(), 1e-10, "Normalized vector should have length 1");
        assertThrows(ArithmeticException.class, () -> new Vector(0, 0, 0).normalize(), "Normalizing zero vector should throw exception");
    }
}
