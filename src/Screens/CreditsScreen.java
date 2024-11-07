package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.Map;
import Maps.BlankMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected Sprite backgroundImage;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont name1;
    protected SpriteFont name2;
    protected SpriteFont name3;
    protected SpriteFont name4;
    protected SpriteFont name5;
    protected Sprite avenghers;
    // protected SpriteFont helpedByLabel;
    protected SpriteFont returnInstructionsLabel;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new BlankMap();
        background.setAdjustCamera(false);
        backgroundImage = new Sprite(ImageLoader.load("WhiteBackground.jpeg"));
        backgroundImage.setScale(4);
        creditsLabel = new SpriteFont("Credits", 300, 9, "Arial", 50, Color.WHITE);
        creditsLabel.setOutlineColor(Color.BLACK);
        creditsLabel.setOutlineThickness(3);
        createdByLabel = new SpriteFont("Created By:", 65, 140, "Arial", 30, new Color(102, 204, 0));
        createdByLabel.setOutlineColor(new Color(0, 102, 0));
        createdByLabel.setOutlineThickness(3);
        avenghers = new Sprite(ImageLoader.load("TheAvengHERss.jpg", Color.WHITE));
        avenghers.setLocation(avenghers.getX() + 300, avenghers.getY() + 90);
        // helpedByLabel = new SpriteFont("Also Contributed:", 65, 320, "Arial", 30, Color.BLACK);
        // helpedByLabel.setOutlineColor(Color.GREEN);
        // helpedByLabel.setOutlineThickness(3);
        name1 = new SpriteFont("Tyler Rinko", 180, 290, "Comic Sans", 25, new Color(51, 51, 255));
        name2 = new SpriteFont("Tripp Menhall", 180, 325, "Comic Sans", 25, new Color(0, 0, 102));
        name3 = new SpriteFont("Isabela Ayers", 180, 185, "Comic Sans", 25, new Color(0, 0, 102));
        name4 = new SpriteFont("Reeya Patel", 180, 255, "Comic Sans", 25, new Color(0, 0, 102));
        name5 = new SpriteFont("Megan Mohr", 180, 220, "Comic Sans", 25, new Color(51, 51, 255));
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 115, 515, "Times New Roman", 40, Color.RED);
        returnInstructionsLabel.setOutlineColor(Color.BLACK);
        returnInstructionsLabel.setOutlineThickness(2);
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        backgroundImage.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        name1.draw(graphicsHandler);
        name2.draw(graphicsHandler);
        name3.draw(graphicsHandler);
        name4.draw(graphicsHandler);
        name5.draw(graphicsHandler);
        avenghers.draw(graphicsHandler);
        // helpedByLabel.draw(graphicsHandler);
    }
}
