package unittests.renderer;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

import org.junit.jupiter.api.Test;

import static java.awt.Color.*;

public class LightingTests {

    private final Scene scene = new Scene("Test scene")
            .setBackground(Color.BLACK)
            .setAmbientLight(new AmbientLight(Color.BLACK, Double3.ZERO));

    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(0, 0, 1000))
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpSize(150, 150)
            .setVpDistance(1000);

    private final Geometry sphere = new Sphere(new Point(0, 0, -50), 50d)
            .setEmission(new Color(255, 0, 0))
            .setMaterial(new Material()
                    .setKd(0.5)
                    .setKs(0.5)
                    .setShininess(100));

    private final Geometry backgroundTriangle = new Triangle(
            new Point(-150, -150, -150),
            new Point(150, -150, -150),
            new Point(0, 150, -150))
            .setEmission(Color.BLACK)
            .setMaterial(new Material()
                    .setKd(0.7)
                    .setKs(0.3)
                    .setShininess(60));

    private void prepareScene() {
        scene.setGeometries(new Geometries());
        scene.getLights().clear();
        scene.geometries.add(sphere, backgroundTriangle);
    }

    private void renderScene(String filename) {
        camera
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage(filename);
    }

    @Test
    public void testDirectionalLight() {
        prepareScene();
        scene.addLight(new DirectionalLight(
                new Color(600, 400, 0), new Vector(1, -1, -2)));

        renderScene("directionalLight");
    }

    @Test
    public void testPointLight() {
        prepareScene();
        scene.addLight(new PointLight(
                new Color(500, 300, 0), new Point(60, 60, 20))
                .setKC(1).setKL(0.001).setKQ(0.0001));

        renderScene("pointLight");
    }

    @Test
    public void testSpotLight() {
        prepareScene();
        scene.addLight(new SpotLight(
                new Color(1000, 600, 600),
                new Point(50, 50, 100),
                new Vector(-1, -1, -2))
                .setKC(1).setKL(0.0001).setKQ(0.00001));

        renderScene("spotLight");
    }
}
