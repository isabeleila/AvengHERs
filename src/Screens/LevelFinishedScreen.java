package Screens;

import Engine.*;
import GameObject.Sprite;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the level lose screen
public class LevelFinishedScreen extends Screen {
    protected SpriteFont finalMessage;
    protected SpriteFont winnerDecl;
    // protected SpriteFont instructionsP1;
    protected SpriteFont instructionsP2;
    protected SpriteFont instructionsP3;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;
    private Sprite finalBackground;

    public LevelFinishedScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        finalBackground = new Sprite(ImageLoader.load("DALLEBackground.jpg"));
        finalBackground.setWidth(90);
        finalBackground.setHeight(65);
        finalBackground.setScale(9);
        finalBackground.setLocation(finalBackground.getX()-25, finalBackground.getY());
        finalMessage = new SpriteFont("GAME OVER", 235, 20, "Times New Roman", 50, Color.white);
        finalMessage.setFontStyle(Font.BOLD);
        winnerDecl = new SpriteFont("Player One Wins.",250, 100, "Times New Roman", 40, Color.white);
        winnerDecl.setFontStyle(Font.BOLD);
        // instructionsP1 = new SpriteFont("Press Space to jump back in with the same characters", 140, 180,"Terminal", 20, Color.white);
        // instructionsP1.setFontStyle(Font.BOLD);
        // instructionsP2 = new SpriteFont("OR", 370, 220,"Fixedsys Regular", 20, Color.white);
        // instructionsP2.setFontStyle(Font.BOLD);
        instructionsP3 = new SpriteFont("Press Escape to return to Main Menu", 200, 220,"Fixedsys Regular", 20, Color.white);
        //y=260
        instructionsP3.setFontStyle(Font.BOLD);
        keyLocker.lockKey(Key.SPACE);
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // if space is pressed, reset level. if escape is pressed, go back to main menu
        // if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
        //     playLevelScreen.initialize();
        // } else if 
        if(Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            playLevelScreen.goBackToMenu();
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        finalBackground.draw(graphicsHandler);
        finalMessage.draw(graphicsHandler);
        winnerDecl.draw(graphicsHandler);
        //instructionsP1.draw(graphicsHandler);
        //instructionsP2.draw(graphicsHandler);
        instructionsP3.draw(graphicsHandler);
    }

    //adds a line of text telling users who won the game
    public void updateDecl(int player, int player2) {
        if(player2 <= 0){
            winnerDecl = new SpriteFont("Player One Wins.",250, 100, "Times New Roman", 40, Color.white);
            winnerDecl.setFontStyle(Font.BOLD);
        }else{
            winnerDecl = new SpriteFont("Player Two Wins.",250, 100, "Times New Roman", 40, Color.white);
            winnerDecl.setFontStyle(Font.BOLD);
        }
    }
}
