package primitives;

/**
 * Represents a vector in 3D space, extending the Point class.
 * This class provides a variety of vector operations, including:
 * - Vector addition
 * - Scalar multiplication (scaling)
 * - Dot product (scalar product)
 * - Cross product (vector product)
 * - Length (magnitude) calculation
 * - Normalization (unit vector)
 *
 * A Vector is represented by three coordinates in 3D space (x, y, z),
 * and operates on those coordinates using mathematical vector operations.
 * Note: A zero vector (0, 0, 0) is not allowed for a valid Vector.
 */
public class Vector extends Point {
    public static final Vector AXIS_X = new Vector(1, 0, 0);
    public static final Vector AXIS_Y = new Vector(0, 1, 0);
    public static final Vector AXIS_Z = new Vector(0, 0, 1);
    /**
     * Constructs a Vector using three coordinates (x, y, z).
     * If the vector is a zero vector, an IllegalArgumentException is thrown.
     *
     * @param x The x-coordinate of the vector
     * @param y The y-coordinate of the vector
     * @param z The z-coordinate of the vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (primitives.Util.isZero(x) && primitives.Util.isZero(y) && primitives.Util.isZero(z)) {
            throw new ArithmeticException("Zero vector is not allowed");
        }
    }

    /**
     * Constructs a Vector using a Double3 object that holds the vector's coordinates.
     * If the vector is a zero vector, an IllegalArgumentException is thrown.
     *
     * @param coordinates The coordinates of the vector as a Double3 object
     */
    public Vector(Double3 coordinates) {
        super(coordinates);
        if (coordinates.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }


    /**
     * Adds another vector to the current vector and returns a new vector representing the sum.
     *
     * @param other The vector to add to the current vector
     * @return A new Vector representing the sum of the two vectors
     */
    public Vector add(Vector other) {
        return new Vector(coordinates.add(other.coordinates));
    }

    /**
     * Scales the vector by a scalar and returns a new vector representing the scaled version.
     * Scaling multiplies the vector by the given scalar value.
     *
     * @param scalar The scalar to multiply the vector by
     * @return A new Vector representing the scaled vector
     */
    public Vector scale(Double scalar) {
        if (primitives.Util.isZero(scalar)) {
            return this;
        }
        return new Vector(coordinates.scale(scalar));
    }
    public Vector scale(int scalar) {
        if (primitives.Util.isZero(scalar)) {
        return this;
    }
    return scale((double) scalar);}
    /**
     * Calculates the dot product (scalar product) of the current vector and another vector.
     * The dot product is a measure of the vectors' similarity in direction.
     *
     * @param other The other vector to calculate the dot product with
     * @return The dot product of the two vectors as a double value
     */
    public double dotProduct(Vector other) {
        // Dot product calculation using the Double3 class
        Double3 thisCoordinates = this.coordinates;
        Double3 otherCoordinates = other.coordinates;
        return thisCoordinates.d1() * otherCoordinates.d1() +
                thisCoordinates.d2() * otherCoordinates.d2() +
                thisCoordinates.d3() * otherCoordinates.d3();
    }

    /**
     * Calculates the cross product (vector product) of the current vector and another vector.
     * The cross product is a vector perpendicular to both input vectors, with a magnitude
     * that is proportional to the area of the parallelogram spanned by the vectors.
     *
     * @param other The other vector to calculate the cross product with
     * @return A new Vector representing the cross product of the two vectors
     */
    public Vector crossProduct(Vector other) {
        Double3 thisCoordinates = this.coordinates;
        Double3 otherCoordinates = other.coordinates;

        // Calculating the cross product components
        double x = thisCoordinates.d2() * otherCoordinates.d3() - thisCoordinates.d3() * otherCoordinates.d2();
        double y = thisCoordinates.d3() * otherCoordinates.d1() - thisCoordinates.d1() * otherCoordinates.d3();
        double z = thisCoordinates.d1() * otherCoordinates.d2() - thisCoordinates.d2() * otherCoordinates.d1();

        return new Vector(new Double3(x, y, z));
    }

    /**
     * Calculates the squared length (magnitude) of the vector.
     * The squared length is the dot product of the vector with itself.
     *
     * @return The squared length of the vector
     */
    public double lengthSquared() {
        return dotProduct(this);  // v · v = |v|^2
    }

    /**
     * Calculates the length (magnitude) of the vector.
     * The length is the square root of the squared length.
     *
     * @return The length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());  // sqrt(v · v)
    }

    /**
     * Normalizes the vector, converting it to a unit vector in the same direction.
     * The resulting vector will have a length of 1, but the same direction as the original vector.
     * If the vector has a length of zero (a zero vector), an exception will be thrown.
     *
     * @return A new Vector representing the normalized (unit) vector
     * @throws ArithmeticException If the vector is a zero vector and cannot be normalized
     */
    public Vector normalize() {
        double len = length();
        if (primitives.Util.isZero(len)) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        return this.scale(1 / len);
    }


    /**
     * Returns a string representation of the vector in the format "Vector(x, y, z)".
     *
     * @return A string representation of the vector
     */
    @Override
    public String toString() {
        return "Vector" + coordinates.toString();
    }

    /**
     * Compares the current vector to another object for equality.
     * Two vectors are considered equal if they have the same coordinates.
     *
     * @param obj The object to compare to
     * @return true if the object is a vector with the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector other) {
            return coordinates.equals(other.coordinates);
        }
        return false;
    }

    /**
     * Returns the hash code for the vector, based on its coordinates.
     *
     * @return The hash code of the vector
     */
    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }
}
