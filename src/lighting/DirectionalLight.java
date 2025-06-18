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

    /**
     * Returns the intensity of the light at a given point.
     * For directional light, the intensity is constant and independent of the point.
     *
     * @param p The point to get the light intensity at (ignored).
     * @return The light's intensity.
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Returns the direction vector from the light source to the given point.
     * For directional light, this is simply the inverse of the light's direction.
     *
     * @param p The point to which the light direction is needed.
     * @return The direction vector from the light to the point.
     */
    @Override
    public Vector getL(Point p) {
        return direction.scale(-1);
    }
}
