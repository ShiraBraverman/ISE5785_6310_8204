package unittests.renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import scene.Scene;

/**
 * Unit test for rendering a colorful, sharp, aesthetic scene using Anti-Aliasing.
 * The scene includes:
 * - A dark room box (floor, ceiling, walls, back wall)
 * - Two "flower" structures made of spheres in cool colors
 * - Crown-style triangles in dark, sharp colors at the top
 * - Multiple light sources with gentle attenuation
 * - Anti-Aliasing enabled for smooth edges
 *
 * This test helps evaluate color vibrance, geometric distribution, and anti-aliasing smoothness.
 */
public class AdaptiveAntiAliasingTest {
    java.util.Random rand = new java.util.Random(1234); // Seed for reproducible results

    /**
     * Creates and renders an improved colorful scene for anti-aliasing testing.
     */
    @Test
    void colorfulAntiAliasingSceneImproved() {
        Scene scene = new Scene("Improved Anti-Aliasing Scene");

        // Add dark floor, ceiling, back wall, and side walls for a dark room box effect
        scene.geometries.add(
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0)) // Floor
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.3).setKs(0.2).setShininess(100).setKr(0.1)
                        )
        );

        scene.geometries.add(
                new Plane(new Point(0, 150, 0), new Vector(0, -1, 0)) // Ceiling
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKr(0.1)
                        )
        );

        scene.geometries.add(
                new Plane(new Point(0, 0, -150), new Vector(0, 0, 1)) // Back wall
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.4).setKs(0.15).setShininess(80).setKr(0.1)
                        )
        );

        scene.geometries.add(
                new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0)), // Left wall
                new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))  // Right wall
        );

        // Define cool colors for flower spheres
        Color[] coolColors = {
                new Color(52, 41, 119),
                new Color(45, 85, 175),
                new Color(0, 140, 200),
                new Color(22, 140, 130),
                new Color(40, 130, 90),
                new Color(90, 84, 200),
                new Color(76, 60, 180),
                new Color(50, 110, 160)
        };

        // Add two flower sphere structures to the scene
        addFlowerSpheres(scene, 50, 0, 30, 8, 20, coolColors);

        // Add two colorful angled triangles ("wing style") to the scene
        scene.geometries.add(
                new Triangle(
                        new Point(-70, 20, -50),
                        new Point(-30, 40, -40),
                        new Point(-50, 20, -30)
                )
                        .setEmission(new Color(20, 180, 180)) // Turquoise
                        .setMaterial(new Material().setKd(0.4).setKs(0.5).setShininess(200).setKr(0.15)),

                new Triangle(
                        new Point(70, 20, -50),
                        new Point(30, 40, -40),
                        new Point(50, 20, -30)
                )
                        .setEmission(new Color(200, 60, 150)) // Pink-fuchsia
                        .setMaterial(new Material().setKd(0.4).setKs(0.5).setShininess(200).setKr(0.15))
        );

        // Create materials for the cylinders
        Material cylinderMaterial = new Material()
                .setKd(0.4)
                .setKs(0.5)
                .setShininess(150)
                .setKr(0.2);

// Cylinder 1
        Cylinder cylinder1 = new Cylinder(
                10, // radius
                60, // height
                new Ray(new Point(-30, -50, 0), new Vector(0, 1, 0)) // standing on the floor
        );
        cylinder1.setEmission(new Color(50, 100, 150)); // blueish
        cylinder1.setMaterial(cylinderMaterial);

// Cylinder 2
        Cylinder cylinder2 = new Cylinder(
                6,  // smaller radius
                40, // shorter height
                new Ray(new Point(30, -50, 40), new Vector(0, 1, 0)) // standing on the floor, offset in Z
        );
        cylinder2.setEmission(new Color(80, 80, 120)); // purple-blue
        cylinder2.setMaterial(cylinderMaterial);

// Add to scene
        scene.geometries.add(cylinder1, cylinder2);

        // Add ambient light with a low intensity for gentle overall illumination
        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 15), 0.15));

        // Add three light sources for highlights and depth
        scene.lights.add(
                new SpotLight(new Color(200, 120, 200), new Point(0, 130, 50), new Vector(0, -1, -1))
                        .setKL(0.0007).setKQ(0.00007)
        );
        scene.lights.add(
                new PointLight(new Color(150, 200, 150), new Point(-60, 120, 40))
                        .setKL(0.0007).setKQ(0.00007)
        );
        scene.lights.add(
                new PointLight(new Color(150, 150, 200), new Point(60, 120, 40))
                        .setKL(0.0007).setKQ(0.00007)
        );

        // Camera configuration with anti-aliasing enabled for smooth rendering
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, renderer.RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(800, 800)
                //.enableAntiAliasing(64)
                // .enableAdaptiveAntiAliasing(64, 5) // depth=3, threshold=15
                .enableAdaptiveAntiAliasing(4, 3)
                .build();

        camera.renderImage()
                .writeToImage("colorfulAestheticAntiAliasingSceneImproved2");
    }

    /**
     * Adds a "flower" of spheres (one center sphere and 8 surrounding spheres) to the scene.
     *
     * @param scene    The scene to add the spheres to.
     * @param centerX  X-coordinate of the flower center.
     * @param centerY  Y-coordinate of the flower center.
     * @param centerZ  Z-coordinate of the flower center.
     * @param radius   Radius of each sphere.
     * @param distance Distance from the center to surrounding spheres.
     * @param colors   Array of colors to use for the spheres.
     */
    void addFlowerSpheres(Scene scene, double centerX, double centerY, double centerZ, double radius, double distance, Color[] colors) {
        Material mat = new Material()
                .setKd(0.4)
                .setKs(0.5)
                .setShininess(300)
                .setKr(0.3);

        // Add center sphere
        scene.geometries.add(
                new Sphere(new Point(centerX, centerY, centerZ), radius)
                        .setEmission(colors[0])
                        .setMaterial(mat)
        );

        // Add 8 surrounding spheres in a circle around the center sphere
        for (int k = 0; k < 8; k++) {
            double angle = k * Math.PI / 4;
            double x = centerX + distance * Math.cos(angle);
            double y = centerY + distance * Math.sin(angle);
            Color color = colors[k % colors.length];

            scene.geometries.add(
                    new Sphere(new Point(x, y, centerZ), radius)
                            .setEmission(color)
                            .setMaterial(mat)
            );
        }
    }
}
