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
                        .withScale(1)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 40)
                        .withScale(1)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 40)
                        .withScale(1)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 3), 40)
                        .withScale(1)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 40)
                        .withScale(1)
                        .withBounds(1, 1, 14, 14)
                        .build(),
            });
        }};
    }

    //Used to animate based specifically on the input frame number
    public void updateSpecific(int shootTimer){
        if(shootTimer <= 50 && shootTimer >= 38){
            setCurrentAnimationFrameIndex(0);
        }else if(shootTimer < 38 && shootTimer >= 26){
            setCurrentAnimationFrameIndex(1);
        }else if(shootTimer < 26 && shootTimer >= 12){
            setCurrentAnimationFrameIndex(2);
        }else if(shootTimer < 12 && shootTimer > 0){
            setCurrentAnimationFrameIndex(3);
        }else{
            setCurrentAnimationFrameIndex(4);
        }
        updateCurrentFrame();
    }
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
