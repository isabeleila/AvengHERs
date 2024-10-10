package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.Map;
import Maps.CharacterMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the main menu screen
public class CharacterSelectScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont character11;
    protected SpriteFont character12;
    protected SpriteFont character21;
    protected SpriteFont character22;
    protected SpriteFont character31;
    protected SpriteFont character32;
    protected SpriteFont character41;
    protected SpriteFont character42;
    protected SpriteFont player1;
    protected SpriteFont player2;
    protected Sprite line;
    protected Sprite hulkL;
    protected Sprite ironmanL;
    protected Sprite captainAmericaL;
    protected Sprite spidermanL;
    protected Sprite hulkR;
    protected Sprite ironmanR;
    protected Sprite captainAmericaR;
    protected Sprite spidermanR;
    protected Map background;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public CharacterSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Characters Left
        hulkL = new Sprite(ImageLoader.load("HULKtest.jpg"));
        hulkL.setWidth(hulkL.getWidth()-30);
        hulkL.setHeight(hulkL.getHeight()-30);
        hulkL.setLocation(hulkL.getX() + 10, hulkL.getY() + 10);
        ironmanL = new Sprite(ImageLoader.load("IronMan1.jpg"));
        ironmanL.setWidth(ironmanL.getWidth()-30);
        ironmanL.setHeight(ironmanL.getHeight()-30);
        ironmanL.setLocation(ironmanL.getX() + 190, ironmanL.getY() + 10);
        captainAmericaL = new Sprite(ImageLoader.load("CaptainAmerica2.jpg"));
        captainAmericaL.setWidth(captainAmericaL.getWidth()-30);
        captainAmericaL.setHeight(captainAmericaL.getHeight()-30);
        captainAmericaL.setLocation(captainAmericaL.getX() + 10, captainAmericaL.getY() + 270);
        spidermanL = new Sprite(ImageLoader.load("SpiderMan2.jpg"));
        spidermanL.setWidth(spidermanL.getWidth()-30);
        spidermanL.setHeight(spidermanL.getHeight()-30);
        spidermanL.setLocation(spidermanL.getX() + 190, spidermanL.getY() + 270);
        // Characters Right
        hulkR = new Sprite(ImageLoader.load("HULKtest.jpg"));
        hulkR.setWidth(hulkR.getWidth()-30);
        hulkR.setHeight(hulkR.getHeight()-30);
        hulkR.setLocation(hulkR.getX() + 400, hulkR.getY() + 10);
        ironmanR = new Sprite(ImageLoader.load("IronMan1.jpg"));
        ironmanR.setWidth(ironmanR.getWidth()-30);
        ironmanR.setHeight(ironmanR.getHeight()-30);
        ironmanR.setLocation(ironmanR.getX() + 580, ironmanR.getY() + 10);
        captainAmericaR = new Sprite(ImageLoader.load("CaptainAmerica2.jpg"));
        captainAmericaR.setWidth(captainAmericaR.getWidth()-30);
        captainAmericaR.setHeight(captainAmericaR.getHeight()-30);
        captainAmericaR.setLocation(captainAmericaR.getX() + 400, captainAmericaR.getY() + 270);
        spidermanR = new Sprite(ImageLoader.load("SpiderMan2.jpg"));
        spidermanR.setWidth(spidermanR.getWidth()-30);
        spidermanR.setHeight(spidermanR.getHeight()-30);
        spidermanR.setLocation(spidermanR.getX() + 580, spidermanR.getY() + 270);
        // Player 1
        player1 = new SpriteFont("Player 1", 130, 533, "Arial", 30, new Color(255,255,255));
        player1.setOutlineColor(Color.black);
        player1.setOutlineThickness(4);
        // Player 2
        player2 = new SpriteFont("Player 2", 520, 533, "Arial", 30, new Color(255,255,255));
        player2.setOutlineColor(Color.black);
        player2.setOutlineThickness(4);
        // Character Text Left
        character11 = new SpriteFont("Character 1", 15, 230, "Arial", 30, new Color(49, 207, 240));
        character11.setOutlineColor(Color.black);
        character11.setOutlineThickness(4);
        character21 = new SpriteFont("Character 2", 195, 230, "Arial", 30, new Color(49, 207, 240));
        character21.setOutlineColor(Color.black);
        character21.setOutlineThickness(4);
        character31 = new SpriteFont("Character 3", 15, 490, "Arial", 30, new Color(49, 207, 240));
        character31.setOutlineColor(Color.black);
        character31.setOutlineThickness(4);
        character41 = new SpriteFont("Character 4", 195, 490, "Arial", 30, new Color(49, 207, 240));
        character41.setOutlineColor(Color.black);
        character41.setOutlineThickness(4);
        // Character Text Right
        character12 = new SpriteFont("Character 1", 405, 230, "Arial", 30, new Color(49, 207, 240));
        character12.setOutlineColor(Color.black);
        character12.setOutlineThickness(4);
        character22 = new SpriteFont("Character 2", 585, 230, "Arial", 30, new Color(49, 207, 240));
        character22.setOutlineColor(Color.black);
        character22.setOutlineThickness(4);
        character32 = new SpriteFont("Character 3", 405, 490, "Arial", 30, new Color(49, 207, 240));
        character32.setOutlineColor(Color.black);
        character32.setOutlineThickness(4);
        character42 = new SpriteFont("Character 4", 585, 490, "Arial", 30, new Color(49, 207, 240));
        character42.setOutlineColor(Color.black);
        character42.setOutlineThickness(4);
        // Line
        line = new Sprite(ImageLoader.load("LINEb.jpg"));
        line.setHeight(line.getHeight()+100);
        line.setLocation(line.getX() + 372, line.getY());
        // Background
        background = new CharacterMap();
        background.setAdjustCamera(false);
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // if keys are pressed, change menu item "hovered" over
        if(Keyboard.isKeyDown(Key.DOWN)){
            if(currentMenuItemHovered == 0){
                currentMenuItemHovered = 2;
            }else if(currentMenuItemHovered == 1){
                currentMenuItemHovered = 3;
            }
        }else if(Keyboard.isKeyDown(Key.UP)){
            if(currentMenuItemHovered == 2){
                currentMenuItemHovered = 0;
            }else if(currentMenuItemHovered == 3){
                currentMenuItemHovered = 1;
            }
        }else if(Keyboard.isKeyDown(Key.LEFT)){
            if(currentMenuItemHovered == 1){
                currentMenuItemHovered = 0;
            }else if(currentMenuItemHovered == 3){
                currentMenuItemHovered = 2;
            }
        }else if(Keyboard.isKeyDown(Key.RIGHT)){
            if(currentMenuItemHovered == 0){
                currentMenuItemHovered = 1;
            }else if(currentMenuItemHovered == 2){
                currentMenuItemHovered = 3;
            }
        }

        // sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            character11.setColor(new Color(250, 0, 0));
            character12.setColor(new Color(250, 0, 0));
            character21.setColor(new Color(49, 207, 240));
            character22.setColor(new Color(49, 207, 240));
            character31.setColor(new Color(49, 207, 240));
            character32.setColor(new Color(49, 207, 240));
            character41.setColor(new Color(49, 207, 240));
            character42.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 1) {
            character11.setColor(new Color(49, 207, 240));
            character12.setColor(new Color(49, 207, 240));
            character21.setColor(new Color(250, 0, 0));
            character22.setColor(new Color(250, 0, 0));
            character31.setColor(new Color(49, 207, 240));
            character32.setColor(new Color(49, 207, 240));
            character41.setColor(new Color(49, 207, 240));
            character42.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 2) {
            character11.setColor(new Color(49, 207, 240));
            character12.setColor(new Color(49, 207, 240));
            character21.setColor(new Color(49, 207, 240));
            character22.setColor(new Color(49, 207, 240));
            character31.setColor(new Color(250, 0, 0));
            character32.setColor(new Color(250, 0, 0));
            character41.setColor(new Color(49, 207, 240));
            character42.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 3) {
            character11.setColor(new Color(49, 207, 240));
            character12.setColor(new Color(49, 207, 240));
            character21.setColor(new Color(49, 207, 240));
            character22.setColor(new Color(49, 207, 240));
            character31.setColor(new Color(49, 207, 240));
            character32.setColor(new Color(49, 207, 240));
            character41.setColor(new Color(250, 0, 0));
            character42.setColor(new Color(250, 0, 0));
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            screenCoordinator.setGameState(GameState.LEVEL);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        character11.draw(graphicsHandler);
        character21.draw(graphicsHandler);
        character31.draw(graphicsHandler);
        character41.draw(graphicsHandler);
        character12.draw(graphicsHandler);
        character22.draw(graphicsHandler);
        character32.draw(graphicsHandler);
        character42.draw(graphicsHandler);
        player1.draw(graphicsHandler);
        player2.draw(graphicsHandler);
        line.draw(graphicsHandler);
        hulkL.draw(graphicsHandler);
        ironmanL.draw(graphicsHandler);
        captainAmericaL.draw(graphicsHandler);
        spidermanL.draw(graphicsHandler);
        hulkR.draw(graphicsHandler);
        ironmanR.draw(graphicsHandler);
        captainAmericaR.draw(graphicsHandler);
        spidermanR.draw(graphicsHandler);
        }
}