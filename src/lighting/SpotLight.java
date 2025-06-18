package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a spotlight (focused point light).
 */
public class SpotLight extends PointLight {
    private final Vector direction;
    private double narrowBeam = 1; // 1 = no narrowing, higher values = narrower beam

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
     * Set the beam narrowing factor (higher = narrower beam)
     *
     * @param narrowBeam beam narrowing factor (e.g., 10)
     * @return this SpotLight instance for chaining
     */
    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        Vector l = super.getL(p);
        double dirFactor = Math.max(0, direction.dotProduct(l.scale(-1))); // cosine of angle

        if (dirFactor == 0) return Color.BLACK;

        // Apply narrow beam effect
        dirFactor = Math.pow(dirFactor, narrowBeam);

        return super.getIntensity(p).scale(dirFactor); // attenuated * cos^narrowBeam
    }

    @Override
    public SpotLight setKC(double kC) {
        super.setKC(kC);
        return this;
    }

    @Override
    public SpotLight setKL(double kL) {
        super.setKL(kL);
        return this;
    }

    @Override
    public SpotLight setKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }
}
