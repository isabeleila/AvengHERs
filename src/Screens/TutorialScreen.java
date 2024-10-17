package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.Map;
import Maps.BlankMap;
import SpriteFont.SpriteFont;

import java.awt.*;

//                                                                                                //
// This is 99% an exact copy of the credit screen. Eventually we will make this into the tutorial //
//                                                                                                //

// This class is for the tutorial screen
public class TutorialScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Sprite player1;
    protected Sprite player2;
    protected SpriteFont creditsLabel;
    protected SpriteFont returnInstructionsLabel;

    public TutorialScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new BlankMap();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Tutorial", 293, 9, "Arial", 50, Color.BLACK);
        creditsLabel.setOutlineColor(Color.WHITE);
        creditsLabel.setOutlineThickness(3);
        player1 = new Sprite(ImageLoader.load("TutorialPlayer1.jpg"));
        player1.setLocation(20, 65);
        player1.setWidth(player1.getWidth() - 340);
        player1.setHeight(player1.getHeight() - 430);
        player2 = new Sprite(ImageLoader.load("TutorialPlayer2.jpg"));
        player2.setLocation(400, 65);
        player2.setWidth(player2.getWidth() - 340);
        player2.setHeight(player2.getHeight() - 430);
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 115, 515, "Times New Roman", 40, Color.BLACK);
        returnInstructionsLabel.setOutlineColor(Color.WHITE);
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
        creditsLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        player1.draw(graphicsHandler);
        player2.draw(graphicsHandler);
    }
}
