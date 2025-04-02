package unittests.geometries;

import geometries.RadialGeometry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.RadialGeometry} class.
 * These tests verify the proper behavior of radial geometry creation with different radius values.
 */
class RadialGeometryTests {
    /**
     * Test constructor with valid radius.
     * Verifies that a RadialGeometry object can be created successfully with a positive radius
     * and that the radius is stored correctly.
     */
    @Test
    void testConstructorValidRadius() {
        // Create a RadialGeometry with a valid radius
        RadialGeometry radialGeometry = new RadialGeometry(5) {};  // Using anonymous subclass
        assertNotNull(radialGeometry, "RadialGeometry object should be created.");
        assertEquals(5, radialGeometry.getRadius(), "The radius should be 5.");
    }

    /**
     * Test constructor with invalid radius (zero).
     * Verifies that an IllegalArgumentException with the appropriate message is thrown
     * when attempting to create a RadialGeometry with a zero radius.
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
     * Test constructor with invalid radius (negative value).
     * Verifies that an IllegalArgumentException with the appropriate message is thrown
     * when attempting to create a RadialGeometry with a negative radius.
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