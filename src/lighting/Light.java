package lighting;

import primitives.Color;

/**
 * Abstract class representing a light source.
 */
abstract class Light {
    /**
     * Intensity (color) of the light.
     */
    protected final Color intensity;

    /**
     * Protected constructor to initialize light intensity.
     *
     * @param intensity the intensity (color) of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity of the light.
     *
     * @return the intensity (color) of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
