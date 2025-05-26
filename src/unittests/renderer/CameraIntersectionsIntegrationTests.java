package unittests;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for {@link Camera} rays with various geometries.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Helper method to count all intersections between camera rays and a geometry.
     */
    private int countIntersections(Camera camera, Intersectable geometry, int nX, int nY) {
        int count = 0;
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                var intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        return count;
    }

    // === Sphere Tests ===

    @Test
    void testSphereIntersections_CenterOfViewPlane() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .build();

        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);
        int count = countIntersections(camera, sphere, 3, 3);
        assertEquals(2, count, "Wrong number of sphere intersections");
    }

    @Test
    void testSphereIntersections_AllRays() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .build();

        Sphere sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        int count = countIntersections(camera, sphere, 3, 3);
        assertEquals(18, count, "Wrong number of sphere intersections");
    }

    // === Plane Tests ===

    @Test
    void testPlaneIntersections_FrontParallel() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .build();

        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        int count = countIntersections(camera, plane, 3, 3);
        assertEquals(9, count, "Wrong number of plane intersections");
    }

    @Test
    void testPlaneIntersections_Slant() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .build();

        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, -0.5, 1));
        int count = countIntersections(camera, plane, 3, 3);
        assertEquals(9, count, "Wrong number of plane intersections");
    }

    // === Triangle Tests ===

    @Test
    void testTriangleIntersections_SmallTriangle() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .build();

        Triangle triangle = new Triangle(
                new Point(0, 1, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
        int count = countIntersections(camera, triangle, 3, 3);
        assertEquals(1, count, "Wrong number of triangle intersections");
    }

    @Test
    void testTriangleIntersections_LargerTriangle() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .build();

        Triangle triangle = new Triangle(
                new Point(0, 20, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
        int count = countIntersections(camera, triangle, 3, 3);
        assertEquals(2, count, "Wrong number of triangle intersections");
    }
}
