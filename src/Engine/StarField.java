package Engine;

import java.awt.Color;
import java.util.Random;

/**
 * Draws twinkling pixel stars over the upper portion of the screen.
 * Stars are in screen-space so they stay fixed as the camera scrolls.
 */
public class StarField {

    private static final int STAR_COUNT = 80;
    private static final float SKY_FRACTION = 0.62f; // stars confined to top 62% of screen

    private final int[] x      = new int[STAR_COUNT];
    private final int[] y      = new int[STAR_COUNT];
    private final int[] period = new int[STAR_COUNT]; // frames per full twinkle cycle
    private final int[] phase  = new int[STAR_COUNT]; // per-star offset so they desync
    private final int[] size   = new int[STAR_COUNT]; // 1 or 2 px

    private int tick = 0;

    public StarField() {
        Random rng = new Random(0xBEEF_CAFE);          // fixed seed → same stars every run
        int w = ScreenManager.getScreenWidth();
        int h = (int) (ScreenManager.getScreenHeight() * SKY_FRACTION);

        for (int i = 0; i < STAR_COUNT; i++) {
            x[i]      = rng.nextInt(w);
            y[i]      = rng.nextInt(Math.max(h, 1));
            period[i] = 90 + rng.nextInt(90);          // 90-179 frames (~1.5-3 s at 60fps)
            phase[i]  = rng.nextInt(180);
            size[i]   = (rng.nextInt(4) == 0) ? 3 : 2; // 1-in-4 chance of 3×3 star
        }
    }

    public void update() {
        tick++;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        for (int i = 0; i < STAR_COUNT; i++) {
            double angle = (2.0 * Math.PI * ((tick + phase[i]) % period[i])) / period[i];
            float brightness = (float) ((Math.sin(angle) + 1.0) / 2.0); // 0.0 → 1.0

            // Interpolate from dim (barely visible) to bright warm-white
            int r = 40 + (int) (brightness * 215);
            int g = 40 + (int) (brightness * 215);
            int b = 30 + (int) (brightness * 170);
            int a = 30 + (int) (brightness * 225); // alpha: nearly invisible → opaque

            Color c = new Color(r, g, b, a);
            graphicsHandler.drawFilledRectangle(x[i], y[i], size[i], size[i], c);
        }
    }
}
