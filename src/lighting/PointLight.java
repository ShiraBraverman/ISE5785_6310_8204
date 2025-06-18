package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a point light source.
 */
public class PointLight extends Light implements LightSource {
    private final Point position;

    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    /**
     * Constructor for PointLight.
     *
     * @param intensity the color/intensity of the light
     * @param position  the position of the light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL the linear factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ the quadratic factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        double attenuation = kC + kL * d + kQ * d * d;
        return intensity.scale(1.0 / attenuation);
    }

    @Override
    public Vector getL(Point p) {
        return position.subtract(p).normalize(); // FROM light TO point
    }
}
