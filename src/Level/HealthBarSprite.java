package Level;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;

public class HealthBarSprite extends GameObject{

    public HealthBarSprite(SpriteSheet spriteSheet, float x, float y, String startingAnimation, int health, Player player) {
        super(spriteSheet, x, y, startingAnimation, health);
        //TODO Auto-generated constructor stub
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
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 4), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 5), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 6), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 7), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 8), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 9), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 10), 40)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build()
            });
        }};
    }

    //Used to animate based specifically on the input frame number
    public void updateSpecific(int frameNumber){
		if (currentFrameIndex != frameNumber){
			currentFrameIndex = frameNumber;	
			updateCurrentFrame();
		}
	}

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
