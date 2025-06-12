package primitives;

/**
 * This class represents the material properties of a surface.
 * Currently contains only the diffuse reflection coefficient (kD).
 */
public class Material {
    /**
     * Diffuse reflection coefficient (kD)
     */
    public Double3 kD = Double3.ONE;

    /**
     * Default constructor (explicit, for javadoc generation)
     */
    public Material() {
    }

    /**
     * Setter for kD with a Double3 value
     *
     * @param kD diffuse reflection coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for kD with a single double value
     *
     * @param kD diffuse reflection coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
}
