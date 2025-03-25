package primitives;

/**
 * This class represents a point in 3D space using three coordinates.
 * It supports basic geometric operations such as vector addition and subtraction,
 * and distance calculation.
 */
public class Point {

    // Protected field for the coordinates of the point
    protected Double3 coordinates;

    // Static constant for the origin point (0,0,0)
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructor to initialize the point with three coordinates.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     */
    public Point(double x, double y, double z) {
        this.coordinates = new Double3(x, y, z);
    }

    /**
     * Constructor to initialize the point with a Double3 object.
     *
     * @param coordinates The Double3 object representing the coordinates
     */
    public Point(Double3 coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Subtracts another point from the current point to create a vector.
     *
     * @param other The other point to subtract from the current point
     * @return A new Double3 representing the vector from the other point to this point
     */
    public Vector subtract(Point other) {
        if (this.equals(other)) {
            return new Vector(Double3.ZERO);
        }
        return new Vector(coordinates.subtract(other.coordinates));
    }

    /**
     * Adds a vector to the current point to create a new point.
     *
     * @param vector The vector to add to the current point
     * @return A new Point representing the result of the addition
     */
    public Point add(Vector vector) {
        return new Point(this.coordinates.add(vector.coordinates));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param other The other point
     * @return The squared distance between the two points
     */
    public double distanceSquared(Point other) {
        Double3 diff = this.coordinates.subtract(other.coordinates);
        return diff.d1() * diff.d1() + diff.d2() * diff.d2() + diff.d3() * diff.d3();
    }




    /**
     * Calculates the distance between this point and another point.
     *
     * @param other The other point
     * @return The distance between the two points
     */
    public double distance(Point other) {
        return Math.sqrt(this.distanceSquared(other));  // Use the distanceSquared method for efficiency
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Point other) {
            return coordinates.equals(other.coordinates);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }
}
