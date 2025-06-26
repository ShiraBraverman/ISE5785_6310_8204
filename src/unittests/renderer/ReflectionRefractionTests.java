package unittests.renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.RayTracerType;
import renderer.SimpleRayTracer;
import scene.Scene;

import java.util.List;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /**
    * Default constructor to satisfy JavaDoc generator
    */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /**
    * Scene for the tests
    */
   private final Scene scene = new Scene("Test scene");
   /**
    * Camera builder for the tests with triangles
    */
   private final renderer.Camera.Builder cameraBuilder = renderer.Camera.getBuilder()     //
           .setRayTracer(scene, renderer.RayTracerType.SIMPLE);

   /**
    * Produce a picture of a sphere lighted by a spot light
    */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
              new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                      .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)), //
              new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100))); //
      scene.lights.add( //
              new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                      .setKL(0.0004).setKQ(0.0000006));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(150, 150) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("refractionTwoSpheres");
   }

   /**
    * Produce a picture of a sphere lighted by a spot light
    */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
              new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
                      .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20) //
                              .setKt(new Double3(0.5, 0, 0))), //
              new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
                      .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(670, 670, 3000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKr(1
                      )), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(-1500, -1500, -2000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
              .setKL(0.00001).setKQ(0.000005));

      cameraBuilder
              .setLocation(new Point(0, 0, 10000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(10000).setVpSize(2500, 2500) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("reflectionTwoSpheresMirrored");
   }
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
              new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                      new Point(75, 75, -150))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
              new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
              new Sphere( new Point(60, 50, -50),30d).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
              new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                      .setKL(4E-5).setKQ(2E-7));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(200, 200) //
              .setResolution(600, 600) //
              .build() //
              .renderImage() //
              .writeToImage("refractionShadow");
   }

   /**
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
    */


   @Test
   void mysticObservatory() {
      // Create a simplified, elegant mystical scene
      scene.geometries.add(
              // Dark reflective floor
              new Plane(new Point(0, -60, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(5, 5, 10))
                      .setMaterial(new Material()
                              .setKd(0.3).setKs(0.4).setShininess(120)
                              .setKr(0.7)),

              // Back wall - very dark
              new Plane(new Point(0, 0, -120), new Vector(0, 0, 1))
                      .setEmission(new Color(3, 3, 8))
                      .setMaterial(new Material()
                              .setKd(0.8).setKs(0.2).setShininess(50)),

              // Left wall - dark purple
              new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(15, 5, 25))
                      .setMaterial(new Material()
                              .setKd(0.7).setKs(0.3).setShininess(60)),

              // Right wall - dark blue
              new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))
                      .setEmission(new Color(5, 10, 20))
                      .setMaterial(new Material()
                              .setKd(0.7).setKs(0.3).setShininess(60)),

              // Large central crystal orb - the main focal point
              new Sphere( new Point(0, -15, -50),35d)
                      .setEmission(new Color(10, 10, 20))
                      .setMaterial(new Material()
                              .setKd(0.1).setKs(0.3).setShininess(400)
                              .setKt(0.85).setKr(0.15)),

              // Large mirror sphere - left side, reflects the crystal
              new Sphere( new Point(-45, -25, -30),25d)
                      .setEmission(new Color(8, 8, 12))
                      .setMaterial(new Material()
                              .setKs(0.1).setKs(0.2).setShininess(500)
                              .setKr(0.95)),

              // Glowing energy orb - right side, completes the triangle
              new Sphere( new Point(40, -30, -35),20d)
                      .setEmission(new Color(255, 120, 255))
                      .setMaterial(new Material()
                              .setKd(0.2).setKs(0.6).setShininess(200)
                              .setKt(0.4).setKr(0.1))

              // Elegant transparent crystal panel - backdrop
//              new Polygon(
//                      new Point(-60, -60, -80),
//                      new Point(60, -60, -80),
//                      new Point(60, 40, -80),
//                      new Point(-60, 40, -80))
//                      .setEmission(new Color(5, 15, 30))
//                      .setMaterial(new Material()
//                              .setKD(0.1).setKS(0.4).setShininess(350)
//                              .setKt(0.8).setKr(0.2))

              // Mystical pyramid - creates interesting shadows and reflections
//              new Triangle(
//                      new Point(0, 50, -60),
//                      new Point(-25, -15, -45),
//                      new Point(25, -15, -45))
//                      .setEmission(new Color(30, 10, 50))
//                      .setMaterial(new Material()
//                              .setKD(0.4).setKS(0.5).setShininess(180)
//                              .setKt(0.3).setKr(0.5))
      );

      // Very dark ambient light
      scene.setAmbientLight(new AmbientLight(new Color(5, 5, 8)));

      // Main focused light source - much darker
      scene.lights.add(
              new PointLight(
                      new Color(180, 120, 250),
                      new Point(0, 30, -10))
                      .setKL(0.001).setKQ(0.0001)
      );

      // Secondary accent light - dimmer
      scene.lights.add(
              new PointLight(
                      new Color(100, 180, 120),
                      new Point(-50, 10, 10))
                      .setKL(0.002).setKQ(0.0002)
      );

      // Side light for shadow contrast - very dim
      scene.lights.add(
              new PointLight(
                      new Color(120, 80, 160),
                      new Point(60, -5, -20))
                      .setKL(0.003).setKQ(0.0003)
      );

      // Camera positioned for optimal viewing of all effects
      cameraBuilder
              .setLocation(new Point(0, 20, 120))
              .setDirection(new Point(0, -10, -40), Vector.AXIS_Y)
              .setVpDistance(100)
              .setVpSize(200, 200)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("mysticObservatory");
   }
}
