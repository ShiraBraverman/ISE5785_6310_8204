package primitives;

/**
 * This class represents a vector in 3D space, extending Point.
 * It includes vector operations like addition, scaling, dot and cross products,
 * as well as methods for calculating length and normalization.
 */

public class Vector extends Point {

    /**
     * Constructor to initialize the vector with three coordinates.
     *
     * @param x The x-coordinate of the vector
     * @param y The y-coordinate of the vector
     * @param z The z-coordinate of the vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (coordinates.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Constructor to initialize the vector from a Double3 object.
     *
     * @param coordinates The coordinates of the vector
     */
    public Vector(Double3 coordinates) {
        super(coordinates);
        if (coordinates.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Adds another vector to the current vector and returns the result.
     *
     * @param other The vector to add
     * @return A new Vector representing the sum of the two vectors
     */
    public Vector add(Vector other) {
        return new Vector(coordinates.add(other.coordinates));
    }

    /**
     * Scales the vector by a scalar (a number) and returns the result.
     *
     * @param scalar The scalar to multiply the vector by
     * @return A new Vector representing the scaled vector
     */
    public Vector scale(Double scalar) {
        return new Vector(coordinates.scale(scalar));
    }

    /**
     * Calculates the dot product (scalar product) of the current vector and another vector.
     *
     * @param other The other vector to calculate the dot product with
     * @return The dot product of the two vectors
     */
    public double dotProduct(Vector other) {
        // שימוש ב-Double3 לשם חישוב מכפלת סקלר
        Double3 thisCoordinates = this.coordinates;
        Double3 otherCoordinates = other.coordinates;
        return thisCoordinates.product(otherCoordinates).d1() +
                thisCoordinates.product(otherCoordinates).d2() +
                thisCoordinates.product(otherCoordinates).d3();
    }


    /**
     * Calculates the cross product (vector product) of the current vector and another vector.
     *
     * @param other The other vector to calculate the cross product with
     * @return A new Vector representing the cross product of the two vectors
     */
    public Vector crossProduct(Vector other) {
        Double3 thisCoordinates = this.coordinates;
        Double3 otherCoordinates = other.coordinates;

        double x = thisCoordinates.d2() * otherCoordinates.d3() - thisCoordinates.d3() * otherCoordinates.d2();
        double y = thisCoordinates.d3() * otherCoordinates.d1() - thisCoordinates.d1() * otherCoordinates.d3();
        double z = thisCoordinates.d1() * otherCoordinates.d2() - thisCoordinates.d2() * otherCoordinates.d1();

        return new Vector(new Double3(x, y, z));
    }


    /**
     * Calculates the squared length (magnitude) of the vector.
     *
     * @return The squared length of the vector
     */
    public double lengthSquared() {
        return dotProduct(this);  // v · v = |v|^2
    }

    /**
     * Calculates the length (magnitude) of the vector.
     *
     * @return The length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());  // sqrt(v · v)
    }

    /**
     * Normalizes the vector, i.e., converts it to a unit vector in the same direction.
     *
     * @return A new Vector representing the normalized vector
     */
    public Vector normalize() {
        double len = length();
        if (len == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        return this.scale(1 / len);
    }

    @Override
    public String toString() {
        return "Vector" + coordinates.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector other) {
            return coordinates.equals(other.coordinates);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }
}
