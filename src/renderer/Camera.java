package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a virtual camera in a 3D space,
 * defining its position, orientation, and the view plane properties.
 * It uses the Builder design pattern for flexible construction.
 */
public class Camera implements Cloneable {

    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width = 0.0, height = 0.0, distance = 0.0;

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
     * Builder class to construct {@link Camera} instances using chained methods.
     */
    public static class Builder {

        private final Camera camera = new Camera();

        /**
         * Sets the camera's location in 3D space.
         *
         * @param p0 the camera location
         * @return the builder instance
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the direction of the camera using orthogonal vectors.
         *
         * @param vTo the "to" direction
         * @param vUp the "up" direction
         * @return the builder instance
         * @throws IllegalArgumentException if vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("the vectors must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the direction of the camera based on a target point and an up vector.
         *
         * @param target the point the camera looks at
         * @param up     the up vector
         * @return the builder instance
         */
        public Builder setDirection(Point target, Vector up) {
            Vector vTo = target.subtract(camera.p0).normalize();
            Vector vRight = vTo.crossProduct(up).normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vUp = vUp;
            camera.vRight = vRight;
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and a default up vector (Y-axis).
         *
         * @param target the point the camera looks at
         * @return the builder instance
         * @throws IllegalArgumentException if the calculated direction is invalid
         */
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

        /**
         * Sets the view plane size.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the builder instance
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the view plane distance
         * @return the builder instance
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * (Optional) Method stub to set resolution. Currently not implemented.
         *
         * @param nX number of pixels in X-direction
         * @param nY number of pixels in Y-direction
         * @return currently returns null
         */
        public Builder setResolution(int nX, int nY) {
            return null;
        }

        /**
         * Finalizes the camera construction after validation.
         *
         * @return the constructed Camera instance
         * @throws MissingResourceException if any required field is missing
         * @throws IllegalArgumentException if direction vectors are not valid
         */
        public Camera build() {
            final String description = "Missing rendering data";
            final String className = "Camera";

            if (camera.p0 == null) throw new MissingResourceException(description, className, "p0");
            if (camera.vTo == null) throw new MissingResourceException(description, className, "vTo");
            if (camera.vUp == null) throw new MissingResourceException(description, className, "vUp");
            if (isZero(camera.width)) throw new MissingResourceException(description, className, "width");
            if (isZero(camera.height)) throw new MissingResourceException(description, className, "height");
            if (isZero(camera.distance)) throw new MissingResourceException(description, className, "distance");

            if (camera.width < 0) throw new IllegalArgumentException("width must be positive");
            if (camera.height < 0) throw new IllegalArgumentException("height must be positive");
            if (camera.distance < 0) throw new IllegalArgumentException("distance must be positive");

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
