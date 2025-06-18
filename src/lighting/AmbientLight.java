package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Class representing ambient light in the scene.
 */
public class AmbientLight extends Light {
    /**
     * Static constant representing no ambient light (black).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     * Constructor for ambient light with default attenuation factor (1.0)
     *
     * @param iA the base ambient light color
     */
    public AmbientLight(Color iA) {
        this(iA, Double3.ONE);
    }

    /**
     * Constructor for ambient light.
     *
     * @param iA the base ambient light color
     * @param kA the attenuation factor
     */
    public AmbientLight(Color iA, Double3 kA) {
        super(iA.scale(kA)); // I = Ia * Ka
    }

    /**
     * Convenience constructor for ambient light with scalar factor
     *
     * @param iA base color
     * @param kA scalar factor
     */
    public AmbientLight(Color iA, double kA) {
        this(iA, new Double3(kA));
    }
}
