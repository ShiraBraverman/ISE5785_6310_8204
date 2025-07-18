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


import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.concurrent.atomic.AtomicInteger;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a virtual camera in a 3D space,
 * defining its position, orientation, and the view plane properties.
 */
public class Camera implements Cloneable {
    private boolean antiAliasing = false; // ברירת מחדל כבוי
    private int samples = 1; // כמות קרניים לאנטי-אייסינג
    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width = 0.0, height = 0.0, distance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int nX = 1, nY = 1; // default resolution
    private boolean adaptiveAntiAliasing = false;
    private int maxAdaptiveDepth = 2; // עומק רקורסיה מקסימלי
    private double adaptiveThreshold = 10.0;
    private static final double MIN_PIXEL_SIZE = 0.0001;
    private boolean multithreading = false;
    private int threadsCount = Runtime.getRuntime().availableProcessors(); // ברירת מחדל


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

    private Ray constructRayThroughPoint(double xShift, double yShift) {
        Point pIJ = p0.add(vTo.scale(distance));
        if (!isZero(xShift)) pIJ = pIJ.add(vRight.scale(xShift));
        if (!isZero(yShift)) pIJ = pIJ.add(vUp.scale(yShift));
        return new Ray(p0, pIJ.subtract(p0).normalize());
    }
    public Camera setMultithreading(boolean enabled) {
        this.multithreading = enabled;
        return this;
    }

    public Camera setThreadsCount(int count) {
        this.threadsCount = count;
        return this;
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

        if (multithreading)
            return renderImageMultiThreaded();
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Color finalColor = Color.BLACK;
                if (adaptiveAntiAliasing) {
                    double Ry = height / nY;
                    double Rx = width / nX;
                    double Yi = -(i - (nY - 1) / 2d) * Ry;
                    double Xj = (j - (nX - 1) / 2d) * Rx;
                    finalColor = adaptiveAntiAliasing(nX, nY, j, i, maxAdaptiveDepth, Rx, Ry, Xj, Yi);
                } else if (antiAliasing) {
                    List<Ray> rays = constructAARays(nX, nY, j, i);
                    for (Ray ray : rays) {
                        finalColor = finalColor.add(rayTracer.traceRay(ray));
                    }
                    finalColor = finalColor.scale(1.0 / rays.size());
                } else {
                    Ray ray = constructRay(nX, nY, j, i);
                    finalColor = rayTracer.traceRay(ray);
                }
                imageWriter.writePixel(j, i, finalColor);
            }
        }
        return this;
    }
    private Camera renderImageMultiThreaded() {
        final int nX = this.nX;
        final int nY = this.nY;
        final AtomicInteger nextRow = new AtomicInteger(0); // אינדקס שורה הבא

        Thread[] threads = new Thread[threadsCount];

        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(() -> {
                int row;
                while ((row = nextRow.getAndIncrement()) < nY) {
                    for (int col = 0; col < nX; col++) {
                        Color finalColor;
                        if (adaptiveAntiAliasing) {
                            double Ry = height / nY;
                            double Rx = width / nX;
                            double Yi = -(row - (nY - 1) / 2d) * Ry;
                            double Xj = (col - (nX - 1) / 2d) * Rx;
                            finalColor = adaptiveAntiAliasing(nX, nY, col, row, maxAdaptiveDepth, Rx, Ry, Xj, Yi);
                        } else if (antiAliasing) {
                            List<Ray> rays = constructAARays(nX, nY, col, row);
                            finalColor = Color.BLACK;
                            for (Ray ray : rays) {
                                finalColor = finalColor.add(rayTracer.traceRay(ray));
                            }
                            finalColor = finalColor.scale(1.0 / rays.size());
                        } else {
                            Ray ray = constructRay(nX, nY, col, row);
                            finalColor = rayTracer.traceRay(ray);
                        }
                        imageWriter.writePixel(col, row, finalColor);
                    }
                }
            });
        }

        for (Thread thread : threads) thread.start();
        try {
            for (Thread thread : threads) thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Multithreading interrupted", e);
        }

        return this;
    }


    private Color adaptiveAntiAliasing(int nX, int nY, int j, int i, int depth, double pixelWidth, double pixelHeight, double centerX, double centerY) {
        if (depth == 0 || pixelWidth < MIN_PIXEL_SIZE || pixelHeight < MIN_PIXEL_SIZE) {
            Ray ray = constructRayThroughPoint(centerX, centerY);
            return rayTracer.traceRay(ray);
        }

        double halfWidth = pixelWidth / 2;
        double halfHeight = pixelHeight / 2;

        // 4 פינות
        double[][] offsets = {
                {-halfWidth, -halfHeight},
                {halfWidth, -halfHeight},
                {-halfWidth, halfHeight},
                {halfWidth, halfHeight}
        };

        Color[] colors = new Color[4];
        for (int k = 0; k < 4; k++) {
            double x = centerX + offsets[k][0];
            double y = centerY + offsets[k][1];
            Ray ray = constructRayThroughPoint(x, y);
            colors[k] = rayTracer.traceRay(ray);
        }

        boolean needSplit = false;
        for (int m = 0; m < 4 && !needSplit; m++) {
            for (int n = m + 1; n < 4; n++) {
                if (colors[m].difference(colors[n]) > adaptiveThreshold) {
                    needSplit = true;
                    break;
                }
            }
        }

        if (!needSplit) {
            return colors[0].add(colors[1]).add(colors[2]).add(colors[3]).scale(0.25);
        } else {
            Color totalColor = Color.BLACK;
            double[][] quarterOffsets = {
                    {-halfWidth / 2, -halfHeight / 2},
                    {halfWidth / 2, -halfHeight / 2},
                    {-halfWidth / 2, halfHeight / 2},
                    {halfWidth / 2, halfHeight / 2}
            };
            for (int k = 0; k < 4; k++) {
                double x = centerX + quarterOffsets[k][0];
                double y = centerY + quarterOffsets[k][1];
                totalColor = totalColor.add(adaptiveAntiAliasing(nX, nY, j, i, depth - 1, halfWidth, halfHeight, x, y));
            }
            return totalColor.scale(0.25);
        }
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

        public Builder enableAntiAliasing(int samples) {
            camera.antiAliasing = true;
            camera.samples = samples;
            return this;
        }

        public Builder enableAdaptiveAntiAliasing(int maxDepth, double threshold) {
            camera.adaptiveAntiAliasing = true;
            camera.maxAdaptiveDepth = maxDepth;
            camera.adaptiveThreshold = threshold;
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
                camera.rayTracer = new SimpleRayTracer(null);
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

    private List<Ray> constructAARays(int nX, int nY, int j, int i) {
        List<Ray> rays = new ArrayList<>();
        double Ry = height / nY;
        double Rx = width / nX;

        double Yi = -(i - (nY - 1) / 2d) * Ry;
        double Xj = (j - (nX - 1) / 2d) * Rx;

        double subRx = Rx / Math.sqrt(samples);
        double subRy = Ry / Math.sqrt(samples);

        for (int row = 0; row < Math.sqrt(samples); row++) {
            for (int col = 0; col < Math.sqrt(samples); col++) {
                double xShift = Xj + (col + 0.5 - Math.sqrt(samples) / 2) * subRx;
                double yShift = Yi + (row + 0.5 - Math.sqrt(samples) / 2) * subRy;

                Point pIJ = p0.add(vTo.scale(distance));
                if (!isZero(xShift)) pIJ = pIJ.add(vRight.scale(xShift));
                if (!isZero(yShift)) pIJ = pIJ.add(vUp.scale(yShift));

                rays.add(new Ray(p0, pIJ.subtract(p0).normalize()));
            }
        }
        return rays;
    }
}
