package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;

import java.util.HashMap;

// This class is for the walrus NPC
public class Walrus extends NPC {

    public static boolean canSpawn = true;
    

    public Walrus(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("bouncingFirstAid.png"), 11, 11), "DEFAULT", 50);
        isInteractable = true;
        talkedToTime = 200;
        canSpawn = false;
        
    }

    public void update(Player player) {
        // while npc is being talked to, it raises its tail up (in excitement?)
       // if (NPC.checkPickedUp(player)) {
       //     this.mapEntityStatus = MapEntityStatus.REMOVED;
        //} else {
            currentAnimationName = "DEFAULT";
        //}

        super.update(player);

        
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale((float) 4.0)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
