package geometries;

import primitives.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Geometries class represents a collection (aggregate) of multiple geometric objects.
 * This class follows the Composite design pattern, allowing multiple geometries to be treated as one.
 * It implements the Intersectable interface, so it can find intersections for all contained geometries.
 */
public class Geometries extends Intersectable {

    /**
     * A list that stores all the geometric objects in this collection.
     * Each object implements the Intersectable interface.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor that creates an empty collection of geometries.
     */
    public Geometries() {
        // No geometries added initially
    }

    /**
     * Constructor that accepts multiple geometries and adds them to the collection.
     *
     * @param geometries One or more intersectable geometries to add to this collection.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);  // Use the add method to insert geometries
    }

    /**
     * Adds one or more geometries to this collection.
     *
     * @param geometries One or more intersectable geometries to add.
     */
    public void add(Intersectable... geometries) {
        if (geometries != null) {
            this.geometries.addAll(Arrays.asList(geometries));
        }
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> result = null;

        for (Intersectable geo : geometries) {
            List<Intersection> intersections = geo.calculateIntersections(ray);
            if (intersections != null && !intersections.isEmpty()) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(intersections);
            }
        }

        return result;
    }
}