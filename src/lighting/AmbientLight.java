package lighting;

import java.awt.Color;

public class AmbientLight {
    // שדה עוצמת התאורה הסביבתית - פרטי ו-final
    private final Color intensity;

    // שדה סטטי קבוע של תאורה שחורה (אין תאורה)
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    // בנאי המקבל עוצמה מסוג Color
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    // מתודה ציבורית המחזירה את עוצמת התאורה
    public Color getIntensity() {
        return intensity;
    }
}
