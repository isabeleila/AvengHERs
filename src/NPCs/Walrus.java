package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Keyboard;
import GameObject.*;
import GameObject.Frame;
import Scene.Map;
import Scene.NPC;
import Scene.Player;
import Utils.Point;
import java.awt.*;
import java.util.HashMap;

public class Walrus extends NPC {

    public Walrus(Point location, Map map) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Walrus.png"), 24, 24), "TAIL_DOWN", 5000, map);
    }

    @Override
    protected SpriteFont createMessage() {
        return new SpriteFont("Hello!", getX(), getY() - 10, "Arial", 12, Color.BLACK);
    }

    public void update(Keyboard keyboard, Player player) {
        if (talkedTo) {
            currentAnimationName = "TAIL_UP";
        } else {
            currentAnimationName = "TAIL_DOWN";
        }
        super.update(keyboard, player);
    }

    @Override
    public HashMap<String, Frame[]> getAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
           put("TAIL_DOWN", new Frame[] {
                   new FrameBuilder(spriteSheet.getSprite(0, 0), 0)
                           .withScale(3)
                           .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                           .build()
           });
            put("TAIL_UP", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 0)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public void drawMessage(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangleWithBorder(Math.round(getCalibratedXLocation(map) - 2), Math.round(getCalibratedYLocation(map) - 24), 40, 25, Color.WHITE, Color.BLACK, 2);
        message.setLocation(getCalibratedXLocation(map) + 2, getCalibratedYLocation(map)- 8);
        message.draw(graphicsHandler);
    }
}
