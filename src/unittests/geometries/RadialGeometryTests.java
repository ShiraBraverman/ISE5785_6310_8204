package unittests.geometries;

import geometries.RadialGeometry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RadialGeometry class.
 */
class RadialGeometryTests {
    /**
     * Test constructor with valid radius
     */
    @Test
    void testConstructorValidRadius() {
        // Create a RadialGeometry with a valid radius
        RadialGeometry radialGeometry = new RadialGeometry(5) {};  // Using anonymous subclass
        assertNotNull(radialGeometry, "RadialGeometry object should be created.");
        assertEquals(5, radialGeometry.getRadius(), "The radius should be 5.");
    }

    /**
     * Test constructor with invalid radius (zero)
     */
    @Test
    void testConstructorInvalidRadiusZero() {
        // Try creating a RadialGeometry with zero radius
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new RadialGeometry(0) {};  // Using anonymous subclass
        });
        assertEquals("Radius must be positive.", thrown.getMessage(), "Exception message should be 'Radius must be positive.'");
    }

    /**
     * Test constructor with invalid radius (negative value)
     */
    @Test
    void testConstructorInvalidRadiusNegative() {
        // Try creating a RadialGeometry with a negative radius
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new RadialGeometry(-1) {};  // Using anonymous subclass
        });
        assertEquals("Radius must be positive.", thrown.getMessage(), "Exception message should be 'Radius must be positive.'");
    }
}
