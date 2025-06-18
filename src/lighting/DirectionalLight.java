package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a directional light source (like sunlight).
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructor for DirectionalLight.
     *
     * @param intensity the light color/intensity
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize(); // Always normalize for consistency
    }

    @Override
    public Color getIntensity(Point p) {
        // For directional light, intensity is constant regardless of point
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        // The direction FROM the light TO the point is opposite of the direction field
        return direction.scale(-1); // Returning vector FROM light TO point
    }
}
