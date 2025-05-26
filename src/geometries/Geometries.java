package geometries;

import primitives.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * A class that represents a collection (aggregate) of geometric objects,
 * implemented according to the Composite design pattern.
 */
public class Geometries implements Intersectable {

    // A list to hold all geometric objects in the collection
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor - creates an empty list of geometries.
     */
    public Geometries() {
        // Empty constructor - do not add anything here
    }

    /**
     * Constructor that accepts multiple geometries and adds them to the collection.
     *
     * @param geometries one or more intersectable geometries to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries); // DRY principle - delegate to the add method
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries one or more intersectable geometries to add
     */
    public void add(Intersectable... geometries) {
        if (geometries != null) {
            this.geometries.addAll(Arrays.asList(geometries));
        }
    }

    /**
     * Finds intersections of a ray with all geometries in the collection.
     *
     * @param ray the ray to test for intersections
     * @return a list of intersection points, or null if there are none
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;

        for (Intersectable geo : geometries) {
            List<Point> intersections = geo.findIntersections(ray);
            if (intersections != null && !intersections.isEmpty()) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(intersections);
            }
        }

        return result == null || result.isEmpty() ? null : result;
    }

}
