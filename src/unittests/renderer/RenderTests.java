package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * Testing basic rendering functionality with different scenes and settings.
 */
class RenderTests {
    RenderTests() {}

    /** Camera builder used for all tests */
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)
            .setVpDistance(100)
            .setVpSize(500, 500);

    /**
     * Render a scene with basic geometry and background color.
     * Includes a blue grid over yellow/greenish background and ambient light.
     */
    @Test
    void renderTwoColorTest() {
        Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
                .setAmbientLight(new AmbientLight(new Color(255, 191, 191)));
        scene.geometries
                .add(
                        new Sphere(new Point(0, 0, -100), 50d),
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

        camera
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage("Two color render test");
    }

    /**
     * Render a scene with materials using different diffuse coefficients (kd).
     */
    @Test
    void renderMaterialColorTest() {
        Scene scene = new Scene("Material color")
                .setBackground(Color.BLACK)
                .setAmbientLight(new AmbientLight(new Color(WHITE)));

        scene.geometries
                .add(
                        new Sphere(new Point(0, 0, -100), 50d)
                                .setMaterial(new Material().setKd(new Double3(0.4))),
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                                .setMaterial(new Material().setKd(new Double3(0.0, 0.8, 0.0))),
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                                .setMaterial(new Material().setKd(new Double3(0.8, 0.0, 0.0))),
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                                .setMaterial(new Material().setKd(new Double3(0.0, 0.0, 0.8))));

        camera
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .printGrid(100, new Color(WHITE))
                .writeToImage("material color render test");
    }

    /**
     * Render a scene with emission colors on triangles to test color blending and display.
     */
    @Test
    void renderMultiColorTest() {
        Scene scene = new Scene("Multi color").setAmbientLight(new AmbientLight(new Color(51, 51, 51)));
        scene.geometries
                .add(
                        new Sphere(new Point(0, 0, -100), 50),
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                                .setEmission(new Color(GREEN)),
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                                .setEmission(new Color(RED)),
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                                .setEmission(new Color(BLUE)));

        camera
                .setRayTracer(scene, renderer.RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .printGrid(100, new Color(WHITE))
                .writeToImage("color render test");
    }

    /**
     * Render a basic scene initialized from XML configuration (manually).
     */
    @Test
    void basicRenderXml() {
        Scene scene = new Scene("Using XML");

        camera
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage("xml render test");
    }

    /**
     * Render a basic scene initialized from JSON configuration (manually).
     */
    @Test
    void basicRenderJson() {
        Scene scene = new Scene("Using Json");

        camera
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage("json render test");
    }
}
