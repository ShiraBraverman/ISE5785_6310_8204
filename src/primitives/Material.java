package primitives;

/**
 * This class represents the material properties of a surface.
 * It defines how the surface interacts with light using the Phong reflection model.
 * <p>
 * The material includes the following coefficients:
 * <ul>
 *     <li>kD – Diffuse reflection coefficient</li>
 *     <li>kS – Specular reflection coefficient</li>
 *     <li>kR – Reflection (mirror-like) coefficient</li>
 *     <li>nShininess – Shininess factor controlling highlight size</li>
 * </ul>
 * <p>
 * All fields are public and initialized with default values:
 * kD, kS, and kR are initialized to {@link Double3#ZERO}, and nShininess is initialized to 0.
 * <p>
 * This class uses builder-style setters for easy chaining.
 */
public class Material {
    /**
     * Diffuse reflection coefficient (kD) – how much the surface scatters light.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient (kS) – how shiny the surface is.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Reflection coefficient (kR) – how much mirror-like reflection the surface has.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * Shininess factor (nShininess) – controls the size of the specular highlight.
     */
    public int nShininess = 0;

    /**
     * Default constructor.
     */
    public Material() {
    }

    // --------------------- Setter methods (Builder style) ---------------------

    /**
     * Sets the diffuse reflection coefficient using a {@link Double3} value.
     *
     * @param kD the diffuse coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient using a single double value.
     *
     * @param kD the diffuse coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient using a {@link Double3} value.
     *
     * @param kS the specular coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient using a single double value.
     *
     * @param kS the specular coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the reflection coefficient using a {@link Double3} value.
     *
     * @param kR the reflection coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient using a single double value.
     *
     * @param kR the reflection coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the shininess factor (nShininess).
     *
     * @param nShininess the shininess factor
     * @return the current Material object (for chaining)
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
