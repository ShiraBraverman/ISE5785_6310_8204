package unittests.renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import scene.Scene;

public class AdaptiveAntiAliasingTest {
    java.util.Random rand = new java.util.Random(1234); // Seed for reproducible results

    @Test
    void colorfulAntiAliasingSceneImproved_MultiMode() {
        Scene scene = createScene();

        // מצב 1: AAA OFF, MT OFF
        Camera cam1 = createCamera(scene, false, false);
        long t1 = System.currentTimeMillis();
        cam1.renderImage().writeToImage("AAA_OFF_MT_OFF");
        long t2 = System.currentTimeMillis();
        System.out.println("Time (AAA OFF, MT OFF): " + (t2 - t1) + " ms");

        // מצב 2: AAA OFF, MT ON
        Camera cam2 = createCamera(scene, false, true);
        long t3 = System.currentTimeMillis();
        cam2.renderImage().writeToImage("AAA_OFF_MT_ON");
        long t4 = System.currentTimeMillis();
        System.out.println("Time (AAA OFF, MT ON): " + (t4 - t3) + " ms");

        // מצב 3: AAA ON, MT OFF
        Camera cam3 = createCamera(scene, true, false);
        long t5 = System.currentTimeMillis();
        cam3.renderImage().writeToImage("AAA_ON_MT_OFF");
        long t6 = System.currentTimeMillis();
        System.out.println("Time (AAA ON, MT OFF): " + (t6 - t5) + " ms");

        // מצב 4: AAA ON, MT ON
        Camera cam4 = createCamera(scene, true, true);
        long t7 = System.currentTimeMillis();
        cam4.renderImage().writeToImage("AAA_ON_MT_ON");
        long t8 = System.currentTimeMillis();
        System.out.println("Time (AAA ON, MT ON): " + (t8 - t7) + " ms");
    }

    private Scene createScene() {
        Scene scene = new Scene("Improved Anti-Aliasing Scene");

        scene.geometries.add(
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.3).setKs(0.2).setShininess(100).setKr(0.1))
        );
        scene.geometries.add(
                new Plane(new Point(0, 150, 0), new Vector(0, -1, 0))
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKr(0.1))
        );
        scene.geometries.add(
                new Plane(new Point(0, 0, -150), new Vector(0, 0, 1))
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.4).setKs(0.15).setShininess(80).setKr(0.1))
        );
        scene.geometries.add(
                new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0)),
                new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))
        );

        Color[] coolColors = {
                new Color(52, 41, 119), new Color(45, 85, 175), new Color(0, 140, 200),
                new Color(22, 140, 130), new Color(40, 130, 90), new Color(90, 84, 200),
                new Color(76, 60, 180), new Color(50, 110, 160)
        };

        addFlowerSpheres(scene, 50, 0, 30, 8, 20, coolColors);

        scene.geometries.add(
                new Triangle(new Point(-70, 20, -50), new Point(-30, 40, -40), new Point(-50, 20, -30))
                        .setEmission(new Color(20, 180, 180))
                        .setMaterial(new Material().setKd(0.4).setKs(0.5).setShininess(200).setKr(0.15)),
                new Triangle(new Point(70, 20, -50), new Point(30, 40, -40), new Point(50, 20, -30))
                        .setEmission(new Color(200, 60, 150))
                        .setMaterial(new Material().setKd(0.4).setKs(0.5).setShininess(200).setKr(0.15))
        );

        Material cylinderMaterial = new Material().setKd(0.4).setKs(0.5).setShininess(150).setKr(0.2);

        Cylinder cylinder1 = new Cylinder(10, 60, new Ray(new Point(-30, -50, 0), new Vector(0, 1, 0)));
        cylinder1.setEmission(new Color(50, 100, 150)).setMaterial(cylinderMaterial);

        Cylinder cylinder2 = new Cylinder(6, 40, new Ray(new Point(30, -50, 40), new Vector(0, 1, 0)));
        cylinder2.setEmission(new Color(80, 80, 120)).setMaterial(cylinderMaterial);

        scene.geometries.add(cylinder1, cylinder2);

        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 15), 0.15));

        scene.lights.add(new SpotLight(new Color(200, 120, 200), new Point(0, 130, 50), new Vector(0, -1, -1))
                .setKL(0.0007).setKQ(0.00007));
        scene.lights.add(new PointLight(new Color(150, 200, 150), new Point(-60, 120, 40))
                .setKL(0.0007).setKQ(0.00007));
        scene.lights.add(new PointLight(new Color(150, 150, 200), new Point(60, 120, 40))
                .setKL(0.0007).setKQ(0.00007));

        return scene;
    }

    private void addFlowerSpheres(Scene scene, double centerX, double centerY, double centerZ, double radius, double distance, Color[] colors) {
        Material mat = new Material().setKd(0.4).setKs(0.5).setShininess(300).setKr(0.3);
        scene.geometries.add(new Sphere(new Point(centerX, centerY, centerZ), radius)
                .setEmission(colors[0]).setMaterial(mat));

        for (int k = 0; k < 8; k++) {
            double angle = k * Math.PI / 4;
            double x = centerX + distance * Math.cos(angle);
            double y = centerY + distance * Math.sin(angle);
            Color color = colors[k % colors.length];
            scene.geometries.add(new Sphere(new Point(x, y, centerZ), radius)
                    .setEmission(color).setMaterial(mat));
        }
    }

    private Camera createCamera(Scene scene, boolean enableAAA, boolean enableMT) {
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene, renderer.RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(800, 800);

        if (enableAAA) {
            builder.enableAdaptiveAntiAliasing(4, 3); // עומק 4, סף 3
        }

        Camera camera = builder.build();

        if (enableMT) {
            camera.setMultithreading(true).setThreadsCount(6);
        }

        return camera;
    }
}
