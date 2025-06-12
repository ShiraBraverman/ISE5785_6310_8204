package lighting;

import primitives.Color;

/**
 * Class representing ambient light in the scene.
 */
public class AmbientLight extends Light {
    /**
     * Static constant representing no ambient light (black).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for ambient light.
     *
     * @param intensity the ambient light intensity (I<sub>a</sub>)
     */
    public AmbientLight(Color intensity) {
        super(intensity); // Call the parent constructor in Light
    }
}
