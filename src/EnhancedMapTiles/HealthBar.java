package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;

import java.awt.Color;
import java.util.HashMap;

// This class is for the end level gold box tile
// when the player touches it, it will tell the player that the level has been completed
public class HealthBar extends EnhancedMapTile {
    public HealthBar(int x, int y) {
        super(x, y, new SpriteSheet(ImageLoader.load("HealthSheet.png", Color.black), 32, 8), TileType.PASSABLE);
    }

    @Override
    public void update(Player player) {
        //super.update(player);
        if (player.getPlayerHealth() == 100) {
            super.updateSpecific(0);
        }else if(player.getPlayerHealth() < 100 && player.getPlayerHealth() > 64){
            super.updateSpecific(1);
        }else if(player.getPlayerHealth() < 64 && player.getPlayerHealth() > 1){
            super.updateSpecific(2);
        }else{
            super.updateSpecific(3);
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 3), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build()
            });
        }};
    }
}
