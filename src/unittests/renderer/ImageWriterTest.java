package unittests.renderer;

import org.junit.jupiter.api.Test;
import renderer.ImageWriter;
import primitives.Color;

public class ImageWriterTest {

    @Test
    void testWriteImageWithGrid() {
        int width = 800;
        int height = 500;
        int gridX = 16;
        int gridY = 10;
        Color backgroundColor = new Color(255, 255, 0); // צהוב
        Color gridColor = new Color(0, 0, 255);         // כחול

        ImageWriter writer = new ImageWriter(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // כל כמה פיקסלים נצבע קו רשת
                if (x % (width / gridX) == 0 || y % (height / gridY) == 0) {
                    writer.writePixel(x, y, gridColor);
                } else {
                    writer.writePixel(x, y, backgroundColor);
                }
            }
        }

        writer.writeToImage("grid_test");
    }
}
