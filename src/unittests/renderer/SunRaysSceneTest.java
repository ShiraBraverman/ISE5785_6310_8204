package unittests.renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

public class SunRaysSceneTest {

    @Test
    void sunnyRoomScene() {
        Scene scene = new Scene("Sunny Room Scene");

        // חדר כהה כמו קודם
        scene.geometries.add(
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0)) // רצפה
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.3).setKs(0.2).setShininess(100).setKr(0.1)),
                new Plane(new Point(0, 150, 0), new Vector(0, -1, 0)) // תקרה
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKr(0.1)),
                new Plane(new Point(0, 0, -150), new Vector(0, 0, 1)) // קיר אחורי
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKd(0.4).setKs(0.15).setShininess(80).setKr(0.1)),
                new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0)), // קיר שמאל
                new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))  // קיר ימין
        );

        // השמש - כדור צהוב גדול בתקרה
        scene.geometries.add(
                new Sphere(new Point(0, 130, 0), 25)
                        .setEmission(new Color(255, 220, 80)) // צהוב שמש
                        .setMaterial(new Material().setKd(0.4).setKs(0.6).setShininess(300).setKr(0.2))
        );

        // שלוש קרניים צהובות דקות היוצאות מהשמש
        scene.geometries.add(
                new Triangle(
                        new Point(-5, 110, -10),
                        new Point(0, 60, -5),
                        new Point(5, 110, -10)
                ).setEmission(new Color(255, 220, 100))
                        .setMaterial(new Material().setKd(0.5).setKs(0.4).setShininess(150)),
                new Triangle(
                        new Point(-30, 110, 0),
                        new Point(-10, 60, 0),
                        new Point(-25, 110, 10)
                ).setEmission(new Color(255, 220, 100))
                        .setMaterial(new Material().setKd(0.5).setKs(0.4).setShininess(150)),
                new Triangle(
                        new Point(30, 110, 0),
                        new Point(10, 60, 0),
                        new Point(25, 110, 10)
                ).setEmission(new Color(255, 220, 100))
                        .setMaterial(new Material().setKd(0.5).setKs(0.4).setShininess(150))
        );

        // תאורה
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 200), 0.15));
        scene.lights.add(
                new SpotLight(new Color(255, 240, 200), new Point(0, 130, 0), new Vector(0, -1, 0))
                        .setKL(0.0007).setKQ(0.00007)
        );

        // מצלמה
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(800, 800)
                //.enableAdaptiveAntiAliasing(4, 3)
                .build();

        camera.renderImage()
                .writeToImage("sunnyRoomScene");
    }
}
