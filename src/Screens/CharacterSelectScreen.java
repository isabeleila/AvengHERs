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
    protected int currentMenuItemHoveredL = 0; // current menu item being "hovered" over LEFT
    protected int currentMenuItemHoveredR = 0; // current menu item being "hovered" over RIGHT
    protected static int menuItemSelectedL = -1;
    protected static int menuItemSelectedR = -1;
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
    protected Sprite readyL;
    protected Sprite readyR;
    protected Sprite c1;
    protected Sprite c2;
    protected Sprite c3;
    protected Sprite c4;
    protected Map background;
    protected int pointerLocationX, pointerLocationY;
    protected boolean playerPressedStart1 = false;
    protected boolean playerPressedStart2 = false;
    protected boolean player1Ready = false;
    protected boolean player2Ready = false;
    protected KeyLocker keyLocker = new KeyLocker();
    Sound sound= new Sound();

    public CharacterSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    // Use these methods to get which character is selected
    // -1 is none selected yet
    // 0 is Character1 (Hulk)
    // 1 is Character2 (IronMan)
    // 2 is Character3 (CaptainAmerica)
    // 3 is Character4 (Spiderman)

    public static int getCharacterL(){
        return menuItemSelectedL;
    }

    public static int getCharacterR(){
        return menuItemSelectedR;
    }

    public void ReadyScreen(int character, int side){
        // Display image with character and say that player is ready
        if(side == 0){ // LEFT
            readyL.setScale(1);
            if(character == 0){ // Character 1
                c1.setLocation(40, 40);
                c1.setScale(1);
            } else if(character == 1){ // Character 2
                c2.setLocation(40, 40);
                c2.setScale(1);
            } else if(character == 2){ // Character 3
                c3.setLocation(40, 40);
                c3.setScale(1);
            } else if(character == 3){ // Character 4
                c4.setLocation(40, 40);
                c4.setScale(1);
            }
        }else if(side == 1){ // RIGHT
            readyR.setScale(1);
            if(character == 0){ // Character 1
                c1.setScale(1);
            } else if(character == 1){ // Character 2
                c2.setScale(1);
            } else if(character == 2){ // Character 3
                c3.setScale(1);
            } else if(character == 3){ // Character 4
                c4.setScale(1);
            }
        }
    }

    @Override
    public void initialize() {
        c1 = new Sprite(ImageLoader.load("HULKtest.jpg"));
        c1.setLocation(430, 40);
        c1.setWidth(c1.getWidth()+80);
        c1.setHeight(c1.getHeight()+80);
        c1.setScale(0);
        c2 = new Sprite(ImageLoader.load("IronMan1.jpg"));
        c2.setLocation(430, 40);
        c2.setWidth(c2.getWidth()+80);
        c2.setHeight(c2.getHeight()+80);
        c2.setScale(0);
        c3 = new Sprite(ImageLoader.load("CaptainAmerica2.jpg"));
        c3.setLocation(430, 40);
        c3.setWidth(c3.getWidth()+80);
        c3.setHeight(c3.getHeight()+80);
        c3.setScale(0);
        c4 = new Sprite(ImageLoader.load("SpiderMan2.jpg"));
        c4.setLocation(430, 40);
        c4.setWidth(c4.getWidth()+80);
        c4.setHeight(c4.getHeight()+80);
        c4.setScale(0);
        readyL = new Sprite(ImageLoader.load("READYbigger.jpg"));
        readyL.setLocation(7,10);
        readyL.setScale(0);
        readyR = new Sprite(ImageLoader.load("READYbigger.jpg"));
        readyR.setLocation(400,10);
        readyR.setScale(0);
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
        character11 = new SpriteFont("Hulk", 15, 230, "Arial", 30, new Color(49, 207, 240));
        character11.setOutlineColor(Color.black);
        character11.setOutlineThickness(4);
        character21 = new SpriteFont("Iron Man", 195, 230, "Arial", 30, new Color(49, 207, 240));
        character21.setOutlineColor(Color.black);
        character21.setOutlineThickness(4);
        character31 = new SpriteFont("Cap Merica", 15, 490, "Arial", 30, new Color(49, 207, 240));
        character31.setOutlineColor(Color.black);
        character31.setOutlineThickness(4);
        character41 = new SpriteFont("Spider-Man", 195, 490, "Arial", 30, new Color(49, 207, 240));
        character41.setOutlineColor(Color.black);
        character41.setOutlineThickness(4);
        // Character Text Right
        character12 = new SpriteFont("Hulk", 405, 230, "Arial", 30, new Color(49, 207, 240));
        character12.setOutlineColor(Color.black);
        character12.setOutlineThickness(4);
        character22 = new SpriteFont("Iron Man", 585, 230, "Arial", 30, new Color(49, 207, 240));
        character22.setOutlineColor(Color.black);
        character22.setOutlineThickness(4);
        character32 = new SpriteFont("Cap Merica", 405, 490, "Arial", 30, new Color(49, 207, 240));
        character32.setOutlineColor(Color.black);
        character32.setOutlineThickness(4);
        character42 = new SpriteFont("Spider-Man", 585, 490, "Arial", 30, new Color(49, 207, 240));
        character42.setOutlineColor(Color.black);
        character42.setOutlineThickness(4);
        // Line
        line = new Sprite(ImageLoader.load("LINEb.jpg"));
        line.setHeight(line.getHeight()+100);
        line.setLocation(line.getX() + 372, line.getY());
        // Background
        background = new CharacterMap();
        background.setAdjustCamera(false);
        menuItemSelectedL = -1;
        menuItemSelectedR = -1;
        keyLocker.lockKey(Key.SPACE);
        playMusic(1);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // if keys are pressed, change menu item "hovered" over
        if(Keyboard.isKeyDown(Key.S)){
            if(currentMenuItemHoveredL == 0){
                currentMenuItemHoveredL = 2;
            }else if(currentMenuItemHoveredL == 1){
                currentMenuItemHoveredL = 3;
            }
        }else if(Keyboard.isKeyDown(Key.W)){
            if(currentMenuItemHoveredL == 2){
                currentMenuItemHoveredL = 0;
            }else if(currentMenuItemHoveredL == 3){
                currentMenuItemHoveredL = 1;
            }
        }else if(Keyboard.isKeyDown(Key.A)){
            if(currentMenuItemHoveredL == 1){
                currentMenuItemHoveredL = 0;
            }else if(currentMenuItemHoveredL == 3){
                currentMenuItemHoveredL = 2;
            }
        }else if(Keyboard.isKeyDown(Key.D)){
            if(currentMenuItemHoveredL == 0){
                currentMenuItemHoveredL = 1;
            }else if(currentMenuItemHoveredL == 2){
                currentMenuItemHoveredL = 3;
            }
        }

        if(Keyboard.isKeyDown(Key.DOWN)){
            if(currentMenuItemHoveredR == 0){
                currentMenuItemHoveredR = 2;
            }else if(currentMenuItemHoveredR == 1){
                currentMenuItemHoveredR = 3;
            }
        }else if(Keyboard.isKeyDown(Key.UP)){
            if(currentMenuItemHoveredR == 2){
                currentMenuItemHoveredR = 0;
            }else if(currentMenuItemHoveredR == 3){
                currentMenuItemHoveredR = 1;
            }
        }else if(Keyboard.isKeyDown(Key.LEFT)){
            if(currentMenuItemHoveredR == 1){
                currentMenuItemHoveredR = 0;
            }else if(currentMenuItemHoveredR == 3){
                currentMenuItemHoveredR = 2;
            }
        }else if(Keyboard.isKeyDown(Key.RIGHT)){
            if(currentMenuItemHoveredR == 0){
                currentMenuItemHoveredR = 1;
            }else if(currentMenuItemHoveredR == 2){
                currentMenuItemHoveredR = 3;
            }
        }

        // sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHoveredL == 0) {
            character11.setColor(new Color(250, 0, 0));
            character21.setColor(new Color(49, 207, 240));
            character31.setColor(new Color(49, 207, 240));
            character41.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHoveredL == 1) {
            character11.setColor(new Color(49, 207, 240));
            character21.setColor(new Color(250, 0, 0));
            character31.setColor(new Color(49, 207, 240));
            character41.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHoveredL == 2) {
            character11.setColor(new Color(49, 207, 240));
            character21.setColor(new Color(49, 207, 240));
            character31.setColor(new Color(250, 0, 0));
            character41.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHoveredL == 3) {
            character11.setColor(new Color(49, 207, 240));
            character21.setColor(new Color(49, 207, 240));
            character31.setColor(new Color(49, 207, 240));
            character41.setColor(new Color(250, 0, 0));
        }

        if (currentMenuItemHoveredR == 0) {
            character12.setColor(new Color(250, 0, 0));
            character22.setColor(new Color(49, 207, 240));
            character32.setColor(new Color(49, 207, 240));
            character42.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHoveredR == 1) {
            character12.setColor(new Color(49, 207, 240));
            character22.setColor(new Color(250, 0, 0));
            character32.setColor(new Color(49, 207, 240));
            character42.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHoveredR == 2) {
            character12.setColor(new Color(49, 207, 240));
            character22.setColor(new Color(49, 207, 240));
            character32.setColor(new Color(250, 0, 0));
            character42.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHoveredR == 3) {
            character12.setColor(new Color(49, 207, 240));
            character22.setColor(new Color(49, 207, 240));
            character32.setColor(new Color(49, 207, 240));
            character42.setColor(new Color(250, 0, 0));
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.CTRL)) {
            keyLocker.unlockKey(Key.CTRL);
        }
        if (!keyLocker.isKeyLocked(Key.CTRL) && Keyboard.isKeyDown(Key.CTRL)) {
            if(playerPressedStart2){
                menuItemSelectedL = currentMenuItemHoveredL;
                stopMusic();
                screenCoordinator.setGameState(GameState.LEVELSELECT);
            }else if(!player1Ready){
                playerPressedStart1 = true;
                menuItemSelectedL = currentMenuItemHoveredL;
                ReadyScreen(currentMenuItemHoveredL, 0);
                player1Ready = true;
            }
        }

        if (Keyboard.isKeyUp(Key.SHIFT)) {
            keyLocker.unlockKey(Key.SHIFT);
        }
        if (!keyLocker.isKeyLocked(Key.SHIFT) && Keyboard.isKeyDown(Key.SHIFT)) {
            if(playerPressedStart1){
                menuItemSelectedR = currentMenuItemHoveredR;
                stopMusic();
                screenCoordinator.setGameState(GameState.LEVELSELECT);
            }else if(!player2Ready){
                playerPressedStart2 = true;
                menuItemSelectedR = currentMenuItemHoveredR;
                // Make a screen show up saying player ready // or call a method to do it
                ReadyScreen(currentMenuItemHoveredR, 1);
                player2Ready = true;
            }
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
        readyL.draw(graphicsHandler);
        readyR.draw(graphicsHandler);
        c1.draw(graphicsHandler);
        c2.draw(graphicsHandler);
        c3.draw(graphicsHandler);
        c4.draw(graphicsHandler);
        }

        public void playMusic(int i){
            sound.setFile(i);
            sound.play();
            sound.loop();
        }
    
        public void stopMusic(){
            sound.stop();
        }
    
}