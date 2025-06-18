package primitives;

/**
 * This class represents the material properties of a surface.
 * It defines how the surface interacts with light using the Phong reflection model.
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
     * Transparency coefficient (kT) – how transparent the surface is.
     */
    public Double3 kT = Double3.ZERO;

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

    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
