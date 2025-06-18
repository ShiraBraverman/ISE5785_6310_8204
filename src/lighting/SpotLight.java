package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a spotlight (focused point light).
 */
public class SpotLight extends PointLight {
    /**
     * The direction vector of the spotlight.
     */
    private final Vector direction;

    /**
     * Beam narrowing factor (spotlight exponent).
     * 1 means no narrowing (standard spotlight), higher values create a narrower and more focused beam.
     */
    private double narrowBeam = 1;


    /**
     * Constructor for SpotLight.
     *
     * @param intensity the color/intensity of the light
     * @param position  the position of the spotlight
     * @param direction the direction of the spotlight
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the beam narrowing factor.
     * A higher value results in a more focused (narrower) spotlight beam.
     * For example: 1 = no narrowing, 10 = strong focus.
     *
     * @param narrowBeam the beam narrowing factor (must be ≥ 1)
     * @return this SpotLight instance (for chaining)
     */
    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * Calculates the spotlight's intensity at a given point.
     * The intensity is determined by both distance attenuation (from {@link PointLight})
     * and the angle between the light direction and the vector to the point.
     * A narrowing factor is applied using cosine falloff.
     *
     * @param p the point at which to calculate the intensity
     * @return the color intensity at point p
     */
    @Override
    public Color getIntensity(Point p) {
        Vector l = super.getL(p);
        double dirFactor = Math.max(0, direction.dotProduct(l.scale(-1))); // cosine of angle

        if (dirFactor == 0) return Color.BLACK;

        // Apply narrow beam effect
        dirFactor = Math.pow(dirFactor, narrowBeam);

        return super.getIntensity(p).scale(dirFactor); // attenuated * cos^narrowBeam
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant attenuation factor
     * @return this SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKC(double kC) {
        super.setKC(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL the linear attenuation factor
     * @return this SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKL(double kL) {
        super.setKL(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ the quadratic attenuation factor
     * @return this SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }
}
