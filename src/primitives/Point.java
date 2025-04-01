package primitives;


/**
 * This class represents a point in 3D space using three coordinates (x, y, z).
 * It supports basic geometric operations such as vector addition, subtraction,
 * and distance calculations. The point is defined by the class's coordinates field,
 * which is an instance of the Double3 class.
 */
public class Point {

    /**
     * The coordinates of the point in 3D space.
     */
    protected Double3 coordinates;

    /**
     * A constant representing the zero point (0,0,0).
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructor to initialize the point with three coordinates.
     *
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @param z The z-coordinate of the point
     */
    public Point(double x, double y, double z) {
        this.coordinates = new Double3(x, y, z);  // Create a new Double3 object to represent the point's coordinates
    }

    /**
     * Constructor to initialize the point with a Double3 object representing the coordinates.
     *
     * @param coordinates The Double3 object representing the coordinates of the point
     */
    public Point(Double3 coordinates) {
        this.coordinates = coordinates;  // Directly use the provided Double3 object
    }

    /**
     * Subtracts another point from the current point to create a vector.
     * The resulting vector represents the direction and distance from the other point to this point.
     *
     * @param other The other point to subtract from the current point
     * @return A new Vector representing the vector from the other point to this point
     */
    public Vector subtract(Point other) {
        if (this.equals(other)) {
            return new Vector(Double3.ZERO);  // If both points are identical, return a zero vector
        }
        return new Vector(coordinates.subtract(other.coordinates));  // Perform the subtraction and return a new vector
    }

    /**
     * Adds a vector to the current point to create a new point.
     * This operation results in translating the point by the vector.
     *
     * @param vector The vector to add to the current point
     * @return A new Point representing the result of the addition
     */
    public Point add(Vector vector) {
        return new Point(this.coordinates.add(vector.coordinates));  // Add the vector's coordinates to the current point's coordinates
    }

    /**
     * Calculates the squared distance between this point and another point.
     * The squared distance avoids the computational cost of taking a square root.
     *
     * @param other The other point
     * @return The squared distance between the two points
     */
    public double distanceSquared(Point other) {
        Double3 diff = this.coordinates.subtract(other.coordinates);  // Get the difference between the coordinates
        return diff.d1() * diff.d1() + diff.d2() * diff.d2() + diff.d3() * diff.d3();  // Calculate squared distance using the 3D difference
    }

    /**
     * Calculates the distance between this point and another point.
     * This method computes the distance by taking the square root of the squared distance.
     *
     * @param other The other point
     * @return The distance between the two points
     */
    public double distance(Point other) {
        return Math.sqrt(this.distanceSquared(other));  // Use the distanceSquared method for efficiency
    }

    /**
     * Returns a string representation of the point.
     * The string representation is based on the coordinates of the point.
     *
     * @return A string representing the point in 3D space
     */
    @Override
    public String toString() {
        return coordinates.toString();  // Return the string representation of the coordinates
    }

    /**
     * Compares this point with another object to check for equality.
     * Two points are considered equal if their coordinates are the same.
     *
     * @param obj The object to compare to this point
     * @return true if the points are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Same object reference, return true.
        if (obj instanceof Point other) {
            return coordinates.equals(other.coordinates);  // Compare the coordinates of both points
        }
        return false;
    }

    /**
     * Computes the hash code for the point.
     * The hash code is based on the hash code of the coordinates.
     *
     * @return The hash code value for the point
     */
    @Override
    public int hashCode() {
        return coordinates.hashCode();  // Use the hash code of the coordinates for the point's hash code
    }
}
