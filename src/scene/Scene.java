package scene;

import lighting.AmbientLight;
import lighting.LightSource;
import java.util.LinkedList;
import java.util.List;
import primitives.*;
import geometries.Geometries;

/**
 * This class represents a 3D scene to be rendered.
 * It holds all the essential components needed to describe the scene,
 * including background color, ambient light, geometries, and light sources.
 */
public class Scene {
    /**
     * The name of the scene (used for identification purposes).
     */
    public String name;

    /**
     * The background color of the scene.
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The collection of geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * The list of light sources in the scene.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructor that initializes the scene with a given name.
     *
     * @param name The name of the scene.
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background The background color to set.
     * @return This Scene instance (for method chaining).
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight The ambient light to set.
     * @return This Scene instance (for method chaining).
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries The geometries to set.
     * @return This Scene instance (for method chaining).
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Adds a light source to the scene.
     *
     * @param light The light source to add.
     * @return This Scene instance (for method chaining).
     */
    public Scene addLight(LightSource light) {
        lights.add(light);
        return this;
    }

    /**
     * Returns the list of light sources in the scene.
     *
     * @return The list of {@link LightSource} objects.
     */
    public List<LightSource> getLights() {
        return lights;
    }
}
