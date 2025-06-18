package scene;

import lighting.AmbientLight;
import lighting.LightSource;
import java.util.LinkedList;
import java.util.List;
import primitives.*;
import geometries.Geometries;


public class Scene {
    // שדות ציבוריים (PDS - Plain Data Structure)
    public String name;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();  // הנחה: יש מחלקה Geometries עם בנאי ברירת מחדל
    private List<LightSource> lights = new LinkedList<>();

    // בנאי המקבל רק את שם הסצנה
    public Scene(String name) {
        this.name = name;
    }

    // מתודות עדכון (setters) בסגנון Builder, מחזירות את האובייקט עצמו (this)

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene addLight(LightSource light) {
        lights.add(light);
        return this;
    }

    public List<LightSource> getLights() {
        return lights;
    }
}
