package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;

/**
 * Abstract base class for ray tracers.
 * Contains the scene to be rendered and defines the contract for tracing rays.
 */
public abstract class RayTracerBase {

    /**
     * The scene to be rendered by the ray tracer.
     */
    protected final Scene scene;

    /**
     * Constructor initializing the ray tracer with the given scene.
     *
     * @param scene the scene to render
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method to trace a ray and return the color intensity along the ray.
     *
     * @param ray the ray to trace
     * @return the color intensity for the ray
     */
    public abstract Color traceRay(Ray ray);
}
