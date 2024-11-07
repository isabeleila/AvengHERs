package Level;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;

public class ShootBarSprite extends GameObject{

    public ShootBarSprite(SpriteSheet spriteSheet, float x, float y, String startingAnimation,int health) {
        super(spriteSheet, x, y, startingAnimation, health);
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
            });
        }};
    }

    //Used to animate based specifically on the input frame number
    public void updateSpecific(int shootTimer){
        if(shootTimer == 0){
            setCurrentAnimationFrameIndex(1);
        }else{
            setCurrentAnimationFrameIndex(0);
        }
        updateCurrentFrame();
    }
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
