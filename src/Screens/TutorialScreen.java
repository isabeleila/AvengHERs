package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
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
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont returnInstructionsLabel;

    public TutorialScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new BlankMap();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Tutorial", 15, 7, "Times New Roman", 30, Color.ORANGE);
        creditsLabel.setOutlineColor(Color.BLACK);
        creditsLabel.setOutlineThickness(2);
        createdByLabel = new SpriteFont("Step 1: START THE GAME duh", 130, 121, "Times New Roman", 20, Color.black);
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 532, "Times New Roman", 30, Color.blue);
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
        createdByLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
    }
}
