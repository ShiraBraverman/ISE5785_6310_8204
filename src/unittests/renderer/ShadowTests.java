package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Testing basic shadows
 * @author Dan
 */
class ShadowTests {

    private final Scene scene = new Scene("Test scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Point.ZERO, Vector.AXIS_Y)
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(scene, RayTracerType.SIMPLE);

    private final Intersectable sphere = new Sphere(new Point(0, 0, -200), 60d)
            .setEmission(new Color(BLUE))
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));

    private final Material trMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(30);

    private void sphereTriangleHelper(String pictName, Triangle triangle, Point spotLocation) {
        triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial);
        scene.geometries.add(sphere, triangle);

        scene.lights.add(new SpotLight(new Color(400, 240, 0), spotLocation, new Vector(1, 1, -3))
                .setKL(1E-5).setKQ(1.5E-7));

        camera.setResolution(400, 400)
                .build()
                .renderImage()
                .writeToImage(pictName);
    }

    @Test
    void sphereTriangleInitial() {
        Triangle triangle = new Triangle(
                new Point(-70, -40, 0),
                new Point(-40, -70, 0),
                new Point(-68, -68, -4)
        );
        sphereTriangleHelper("shadowSphereTriangleInitial", triangle, new Point(-100, -100, 200));
    }

    @Test
    void sphereTriangleMove1() {
        Triangle triangle = new Triangle(
                new Point(-60, -30, 0),
                new Point(-30, -60, 0),
                new Point(-58, -58, -4)
        );
        sphereTriangleHelper("shadowSphereTriangleMove1", triangle, new Point(-100, -100, 200));
    }

    @Test
    void sphereTriangleMove2() {
        Triangle triangle = new Triangle(
                new Point(-50, -20, 0),
                new Point(-20, -50, 0),
                new Point(-48, -48, -4)
        );
        sphereTriangleHelper("shadowSphereTriangleMove2", triangle, new Point(-100, -100, 200));
    }

    @Test
    void sphereTriangleSpot1() {
        Triangle triangle = new Triangle(
                new Point(-70, -40, 0),
                new Point(-40, -70, 0),
                new Point(-68, -68, -4)
        );
        sphereTriangleHelper("shadowSphereTriangleSpot1", triangle, new Point(-90, -90, 150));
    }

    @Test
    void sphereTriangleSpot2() {
        Triangle triangle = new Triangle(
                new Point(-70, -40, 0),
                new Point(-40, -70, 0),
                new Point(-68, -68, -4)
        );
        sphereTriangleHelper("shadowSphereTriangleSpot2", triangle, new Point(-80, -80, 100));
    }

    @Test
    void trianglesSphere() {
        scene.geometries.add(
                new Triangle(
                        new Point(-150, -150, -115),
                        new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setEmission(new Color(20, 20, 20)) // צבע בסיס אפור כהה
                        .setMaterial(new Material().setKd(0.5).setKs(0.8).setShininess(60)), // הוספת kD

                new Triangle(
                        new Point(-150, -150, -115),
                        new Point(-70, 70, -140),
                        new Point(75, 75, -150))
                        .setEmission(new Color(20, 20, 20)) // צבע בסיס אפור כהה
                        .setMaterial(new Material().setKd(0.5).setKs(0.8).setShininess(60)), // הוספת kD

                new Sphere(new Point(0, 0, -11), 30d)
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))
        );

        scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

        scene.lights.add(new SpotLight(new Color(700, 400, 400),
                new Point(40, 40, 115),
                new Vector(-1, -1, -4))
                .setKL(4E-4).setKQ(2E-5));

        camera.setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("shadowTrianglesSphere");
    }

    @Test
    void debugSimpleShadowTest() {
        scene.lights.clear();

        // ריבוע שטוח על ציר Z
        Triangle floor = new Triangle(
                new Point(-100, -100, 0),
                new Point(100, -100, 0),
                new Point(0, 100, 0)
        );
        floor.setEmission(new Color(100, 100, 100));
        floor.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));


        // כדור באוויר מעל הריבוע
        Sphere ball = new Sphere(new Point(0, 0, 50), 30d);
        ball.setEmission(new Color(0, 0, 255));
        ball.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));

        // 👇 הוספה בשתי שורות
        scene.geometries.add(floor);
        scene.geometries.add(ball);

        // אור שמכוון מהצד (כדי שייצר צל)
        scene.lights.add(new SpotLight(new Color(500, 300, 0),
                new Point(100, 100, 100), // מיקום האור
                new Vector(-1, -1, -1))   // כיוון הקרן
                .setKL(1E-5).setKQ(1.5E-7));

        // אור אמביאנט חלש כדי שלא "ישטוף" הכל
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        camera
                .setResolution(300, 300)
                .build()
                .renderImage()
                .writeToImage("debugSimpleShadowTest");
    }


    @Test
    void simpleShadowDebugTest() {
        scene.lights.clear();

        // ריבוע שטוח
        Triangle floor = new Triangle(
                new Point(-50, -50, 0),
                new Point(50, -50, 0),
                new Point(0, 50, 0)
        );
        floor.setEmission(new Color(100, 100, 100));
        floor.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));

        // כדור מעל הריבוע
        Sphere ball = new Sphere(new Point(0, 0, 50), 20d);
        ball.setEmission(new Color(0, 0, 255));
        ball.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));

        scene.geometries.add(floor, ball);

        // אור שמגיע מהכיוון (100, 100, 100)
        scene.lights.add(new SpotLight(new Color(500, 300, 0),
                new Point(100, 100, 100),
                new Vector(-1, -1, -1))
                .setKL(1E-5).setKQ(1.5E-7));

        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        camera
                .setResolution(300, 300)
                .build()
                .renderImage()
                .writeToImage("simpleShadowDebugTest");
    }

}
