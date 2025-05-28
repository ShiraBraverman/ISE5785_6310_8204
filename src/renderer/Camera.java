package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Color;
import renderer.ImageWriter.*;
import scene.Scene;
import renderer.SimpleRayTracer;
import renderer.RayTracerBase;
import renderer.RayTracerType;


import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a virtual camera in a 3D space,
 * defining its position, orientation, and the view plane properties.
 */
public class Camera implements Cloneable {

    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width = 0.0, height = 0.0, distance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int nX = 1, nY = 1; // default resolution

    /**
     * Private constructor to enforce the use of the Builder pattern.
     */
    private Camera() {}

    /**
     * Gets a new instance of the {@link Builder} to create a {@code Camera}.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel (i, j) in the view plane.
     *
     * @param nX number of horizontal pixels
     * @param nY number of vertical pixels
     * @param j  column index (pixel)
     * @param i  row index (pixel)
     * @return the constructed ray through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double Ry = height / nY;
        double Rx = width / nX;
        Point pIJ = p0;

        double Yi = -(i - (nY - 1) / 2d) * Ry;
        double Xj = (j - (nX - 1) / 2d) * Rx;

        if (!isZero(Xj)) pIJ = pIJ.add(vRight.scale(Xj));
        if (!isZero(Yi)) pIJ = pIJ.add(vUp.scale(Yi));

        pIJ = pIJ.add(vTo.scale(distance));

        return new Ray(p0, pIJ.subtract(p0).normalize());
    }

    /**
     * Renders the image.
     *
     * @return the camera object
     */
    public Camera renderImage() {
        if (imageWriter == null)
            throw new IllegalStateException("imageWriter is not initialized");
        if (rayTracer == null)
            throw new IllegalStateException("rayTracer is not initialized");

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = constructRay(nX, nY, j, i);
                Color color = rayTracer.traceRay(ray);
                imageWriter.writePixel(j, i, color);
            }
        }
        return this;
    }

    /**
     * Prints a grid on the image.
     *
     * @param interval the interval between lines
     * @param color    the color of the grid lines
     * @return the camera object
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new IllegalStateException("imageWriter is not initialized");

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the image to a file.
     *
     * @param filename the file name without extension
     * @return the camera object
     */
    public Camera writeToImage(String filename) {
        if (imageWriter == null)
            throw new IllegalStateException("imageWriter is not initialized");

        imageWriter.writeToImage(filename);
        return this;
    }

    /**
     * Builder class to construct {@link Camera} instances using chained methods.
     */
    public static class Builder {

        private final Camera camera = new Camera();

        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("the vectors must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        public Builder setDirection(Point target, Vector up) {
            Vector vTo = target.subtract(camera.p0).normalize();
            Vector vRight = vTo.crossProduct(up).normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vUp = vUp;
            camera.vRight = vRight;
            return this;
        }

        public Builder setDirection(Point target) {
            Vector defaultUp = new Vector(0, 1, 0);
            Vector vTo = target.subtract(camera.p0).normalize();

            if (isZero(vTo.dotProduct(defaultUp))) {
                throw new IllegalArgumentException("vTo cannot be parallel to the default up vector");
            }

            Vector vRight = vTo.crossProduct(defaultUp).normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vUp = vUp;
            camera.vRight = vRight;

            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the camera.
         *
         * @param nX number of pixels in X-direction
         * @param nY number of pixels in Y-direction
         * @return the builder instance
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("Resolution must be positive");
            }
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene        the scene to trace
         * @param rayTracerType the type of ray tracer
         * @return the builder instance
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            if (rayTracerType == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            } else {
                camera.rayTracer = null;
            }
            return this;
        }

        public Camera build() {
            final String description = "Missing rendering data";
            final String className = "Camera";

            if (camera.p0 == null) throw new MissingResourceException(description, className, "p0");
            if (camera.vTo == null) throw new MissingResourceException(description, className, "vTo");
            if (camera.vUp == null) throw new MissingResourceException(description, className, "vUp");
            if (isZero(camera.width)) throw new MissingResourceException(description, className, "width");
            if (isZero(camera.height)) throw new MissingResourceException(description, className, "height");
            if (isZero(camera.distance)) throw new MissingResourceException(description, className, "distance");
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException("Resolution values must be positive");
            }

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null); // Empty scene
            }

            camera.vRight = (camera.vTo.crossProduct(camera.vUp)).normalize();

            if (!isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !isZero(camera.vUp.dotProduct(camera.vRight))) {
                throw new IllegalArgumentException("The 3 vectors must be orthogonal");
            }

            if (!isZero(camera.vTo.length() - 1) ||
                    !isZero(camera.vRight.length() - 1) ||
                    !isZero(camera.vUp.length() - 1)) {
                throw new IllegalArgumentException("The 3 vectors must be normalized");
            }

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
