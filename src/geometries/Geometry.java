package geometries;

import primitives.*;

import java.util.List;

/**
 * Abstract class representing a generic geometric object in 3D space.
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry (default is black).
     */
    protected Color emission = Color.BLACK;

    /**
     * The material properties of the geometry (default is new Material()).
     */
    private Material material = new Material();

    /**
     * Default constructor for the Geometry class.
     */
    public Geometry() {
        // No specific initialization needed here
    }

    /**
     * Getter for the emission color.
     *
     * @return the emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Setter for the emission color using builder pattern.
     *
     * @param emission the color to set
     * @return this geometry instance (for method chaining)
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Getter for the material.
     *
     * @return the material of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Setter for the material using builder pattern.
     *
     * @param material the material to set
     * @return this geometry instance (for method chaining)
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Abstract method to get the normal vector to the surface at a given point.
     *
     * @param point The point on the geometry's surface where the normal is requested.
     * @return The normal vector at the specified point (usually normalized).
     */
    public abstract Vector getNormal(Point point);

    @Override
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
}
