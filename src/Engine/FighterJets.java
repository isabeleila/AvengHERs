package Engine;

import java.awt.*;
import java.util.Random;

/**
 * Draws fighter jets flying across the upper portion of the screen.
 * Jets are in screen-space so they stay fixed as the camera scrolls.
 */
public class FighterJets {

    private static final int JET_COUNT = 4;
    private static final int W = 800; // approximate screen width
    private static final int H = 605;
    private static final int MAX_Y = (int) (H * 0.50f); // jets stay in top half

    // per-jet state
    private final float[] x       = new float[JET_COUNT];
    private final float[] y       = new float[JET_COUNT];
    private final float[] speed   = new float[JET_COUNT]; // positive = right, negative = left
    private final int[]   size    = new int[JET_COUNT];   // scale factor (body length base)

    public FighterJets() {
        Random rng = new Random(0xF16E7135L);
        for (int i = 0; i < JET_COUNT; i++) {
            speed[i] = (rng.nextFloat() * 1.5f + 0.8f) * (rng.nextBoolean() ? 1 : -1);
            size[i]  = 5 + rng.nextInt(4); // body half-length: 5-8 px
            y[i]     = 20 + rng.nextInt(Math.max(MAX_Y - 20, 1));
            // stagger starting positions so they aren't all bunched at an edge
            x[i] = speed[i] > 0
                    ? -(rng.nextFloat() * W)        // will fly in from the left
                    : W + (rng.nextFloat() * W);    // will fly in from the right
        }
    }

    public void update() {
        int screenW = ScreenManager.getScreenWidth();
        for (int i = 0; i < JET_COUNT; i++) {
            x[i] += speed[i];
            // wrap: when fully off the opposite edge, reset to come from the original side
            if (speed[i] > 0 && x[i] > screenW + 80) {
                x[i] = -80;
            } else if (speed[i] < 0 && x[i] < -80) {
                x[i] = screenW + 80;
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        Graphics2D g2 = graphicsHandler.getGraphics();
        Composite original = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));

        for (int i = 0; i < JET_COUNT; i++) {
            drawJet(g2, (int) x[i], (int) y[i], size[i], speed[i] > 0);
        }

        g2.setComposite(original);
    }

    /**
     * Draws a simple fighter jet as filled polygons.
     * @param cx      center x of the jet body
     * @param cy      center y of the jet body
     * @param s       half-length of the fuselage (scale)
     * @param facingRight  true = nose points right, false = nose points left
     */
    private void drawJet(Graphics2D g2, int cx, int cy, int s, boolean facingRight) {
        int dir = facingRight ? 1 : -1;

        // --- fuselage (long narrow body) ---
        // tip to tail: nose is at cx + dir*s, tail at cx - dir*s
        int noseX  = cx + dir * s;
        int tailX  = cx - dir * s;

        int[] bodyX = { noseX, cx + dir * (s / 2), tailX,        tailX,        cx + dir * (s / 2) };
        int[] bodyY = { cy,    cy - s / 5,          cy - s / 6,  cy + s / 6,   cy + s / 5         };
        g2.setColor(new Color(160, 160, 175));
        g2.fillPolygon(bodyX, bodyY, bodyX.length);

        // --- main delta wings ---
        int wingRootX  = cx - dir * (s / 4);   // wing root starts just behind center
        int wingTipX   = cx - dir * s;          // wing tip lines up with tail
        int wingSpan   = s + s / 3;             // how far up/down the wings reach

        int[] wingXTop = { wingRootX, wingTipX, wingTipX };
        int[] wingYTop = { cy,        cy - wingSpan, cy - s / 6 };
        g2.setColor(new Color(130, 130, 145));
        g2.fillPolygon(wingXTop, wingYTop, 3);

        int[] wingXBot = { wingRootX, wingTipX, wingTipX };
        int[] wingYBot = { cy,        cy + wingSpan, cy + s / 6 };
        g2.fillPolygon(wingXBot, wingYBot, 3);

        // --- canard fins (small fins near nose) ---
        int canardRootX = cx + dir * (s / 3);
        int canardTipX  = cx + dir * (s / 5);
        int canardSpan  = s / 3;

        int[] canardXTop = { canardRootX, canardTipX, canardTipX };
        int[] canardYTop = { cy - s / 8,  cy - canardSpan, cy - s / 6 };
        g2.setColor(new Color(140, 140, 160));
        g2.fillPolygon(canardXTop, canardYTop, 3);

        int[] canardXBot = { canardRootX, canardTipX, canardTipX };
        int[] canardYBot = { cy + s / 8,  cy + canardSpan, cy + s / 6 };
        g2.fillPolygon(canardXBot, canardYBot, 3);

        // --- cockpit canopy ---
        int canopyFX = cx + dir * (s * 2 / 3);
        int canopyBX = cx + dir * (s / 6);
        int[] canopyX = { canopyFX, canopyBX, canopyBX, canopyFX };
        int[] canopyY = { cy - s / 8, cy - s / 4, cy - s / 6, cy };
        g2.setColor(new Color(100, 180, 220, 200));
        g2.fillPolygon(canopyX, canopyY, 4);

        // --- engine exhaust glow (simple oval at the tail) ---
        int glowW = s / 4;
        int glowH = s / 5;
        g2.setColor(new Color(255, 140, 30, 180));
        g2.fillOval(tailX - dir * glowW / 2, cy - glowH / 2, glowW, glowH);

        // --- faint exhaust trail ---
        int trailLen = s * 2;
        GradientPaint trail = new GradientPaint(
                tailX, cy,               new Color(255, 120, 20, 120),
                tailX - dir * trailLen, cy, new Color(255, 120, 20, 0));
        g2.setPaint(trail);
        g2.fillRect(
                Math.min(tailX, tailX - dir * trailLen),
                cy - glowH / 2,
                trailLen,
                glowH);
        g2.setPaint(null);
    }
}
