package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.Map;
import Maps.LevelSelectMap;
import SpriteFont.SpriteFont;

import java.awt.*;


public class LevelSelectScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentLevelHovered = 0; // current level being "hovered" over
    protected int levelSelected = -1;
    protected SpriteFont level1;
    protected SpriteFont level2;
    protected SpriteFont level3;
    protected Sprite line;
    protected Sprite levelImage1;
    protected Sprite levelImage2;
    protected Sprite levelImage3;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public LevelSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        //level images
        levelImage1 = new Sprite(ImageLoader.load("Level1.png"));
        levelImage1.setWidth(levelImage1.getWidth()-170 );
        levelImage1.setHeight(levelImage1.getHeight()-170);
        levelImage1.setLocation(levelImage1.getX() + 50, levelImage1.getY() + 100);

        levelImage2 = new Sprite(ImageLoader.load("Level2.png"));
        levelImage2.setWidth(levelImage2.getWidth()-170);
        levelImage2.setHeight(levelImage2.getHeight()-170);
        levelImage2.setLocation(levelImage2.getX() + 280, levelImage2.getY() + 100);

        levelImage3 = new Sprite(ImageLoader.load("Level3.png"));
        levelImage3.setWidth(levelImage3.getWidth()-170);
        levelImage3.setHeight(levelImage3.getHeight()-170);
        levelImage3.setLocation(levelImage3.getX() + 510, levelImage3.getY() + 100);
    
        //level text
        level1 = new SpriteFont("Level 1", 100, 350, "Arial", 35, new Color(49, 207, 240));
        level1.setOutlineColor(Color.black);
        level1.setOutlineThickness(4);
        level2 = new SpriteFont("Level 2", 330, 350, "Arial", 35, new Color(49, 207, 240));
        level2.setOutlineColor(Color.black);
        level2.setOutlineThickness(4);
        level3 = new SpriteFont("Level 3", 560, 350, "Arial", 35, new Color(49, 207, 240));
        level3.setOutlineColor(Color.black);
        level3.setOutlineThickness(4);
    
        // // Line
        // line = new Sprite(ImageLoader.load("LINEb.jpg"));
        // line.setHeight(line.getHeight()+100);
        // line.setLocation(line.getX() + 372, line.getY());
           
         // Background
         background = new LevelSelectMap();
         background.setAdjustCamera(false);
         levelSelected = -1;
         keyLocker.lockKey(Key.SPACE);

         keyPressTimer = 0;
    }

    public void update() {
        //update background map (to play tile animations)
        background.update(null);

        //level "hovered" over will change when left/right keys are pressed 
        if (Keyboard.isKeyDown(Key.RIGHT) &&  keyPressTimer == 0) {
            keyPressTimer = 8;
            if(currentLevelHovered == 0){
                currentLevelHovered = 1;
            }else if(currentLevelHovered == 1){
                currentLevelHovered = 2;
            }
        } else if (Keyboard.isKeyDown(Key.LEFT) &&  keyPressTimer == 0) {
            keyPressTimer = 8;
            if(currentLevelHovered == 2){
                currentLevelHovered = 1;
            }else if(currentLevelHovered == 1){
                currentLevelHovered = 0;
            }
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // sets the color of font based on which level is being hovered over
        if(currentLevelHovered == 0){
            level1.setColor(new Color(250, 0, 0));
            level2.setColor(new Color(49, 207, 240));
            level3.setColor(new Color(49, 207, 240));
        }else if (currentLevelHovered == 1){
            level1.setColor(new Color(49, 207, 240));
            level2.setColor(new Color(250, 0, 0));
            level3.setColor(new Color(49, 207, 240));
        }else if (currentLevelHovered ==2){
            level1.setColor(new Color(49, 207, 240));
            level2.setColor(new Color(49, 207, 240));
            level3.setColor(new Color(250, 0, 0));
        }

        //if spacebar is pressed on level, that level is selected
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            levelSelected = currentLevelHovered;
            screenCoordinator.setGameState(GameState.LEVEL); // Transition to selected level
        }
    }

    public void draw(GraphicsHandler graphicsHandler){
        background.draw(graphicsHandler);
        level1.draw(graphicsHandler);
        level2.draw(graphicsHandler);
        level3.draw(graphicsHandler);
        //line.draw(graphicsHandler);
        levelImage1.draw(graphicsHandler);
        levelImage2.draw(graphicsHandler);
        levelImage3.draw(graphicsHandler);

    }
}