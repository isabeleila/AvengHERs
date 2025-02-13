package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont tutorial;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    Sound sound = new Sound();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {

        playMusic(0);

        playGame = new SpriteFont("PLAY GAME", 290, 170, "Arial", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        tutorial = new SpriteFont("TUTORIAL", 303, 210, "Arial", 30, new Color(49, 207, 240));
        tutorial.setOutlineColor(Color.black);
        tutorial.setOutlineThickness(3);
        credits = new SpriteFont("CREDITS", 310, 250, "Arial", 30, new Color(49, 207, 240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if (Keyboard.isKeyDown(Key.DOWN) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        }else if(Keyboard.isKeyDown(Key.S) &&  keyPressTimer == 0){
            keyPressTimer = 14;
            currentMenuItemHovered++;
        }else if (Keyboard.isKeyDown(Key.UP) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        }else if(Keyboard.isKeyDown(Key.W) &&  keyPressTimer == 0){
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        /* if (currentMenuItemHovered > 1) {
           currentMenuItemHovered = 0;
           } else if (currentMenuItemHovered < 0) {
           currentMenuItemHovered = 1;
           } */ 

        if (currentMenuItemHovered > 2) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 2;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(new Color(250, 0, 0));
            tutorial.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(new Color(49, 207, 240));
            tutorial.setColor(new Color(250, 0, 0));
            credits.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 2) {
            playGame.setColor(new Color(49, 207, 240));
            tutorial.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(250, 0, 0));
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.CHARACTER);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.TUTORIAL);
            } else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
            stopMusic();
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        tutorial.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        }

        public void playMusic(int i){
            sound.setFile(i);
            sound.play();
            sound.loop();
        }
    
        public void stopMusic(){
            sound.stop();
        }
    
        public void playSE(int i){
            sound.setFile(i);
            sound.play();
        }
}