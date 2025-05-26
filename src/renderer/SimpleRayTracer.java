package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;

/**
 * Simple ray tracer implementation.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor initializing SimpleRayTracer with the given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Currently not implemented.
     *
     * @param ray the ray to trace
     * @return (not used)
     * @throws UnsupportedOperationException always thrown since method is not implemented
     */
    @Override
    public Color traceRay(Ray ray) {
        throw new UnsupportedOperationException("SimpleRayTracer.traceRay is not implemented yet");
    }
}
