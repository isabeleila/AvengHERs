package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.BlankMap;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

//                                                                                                //
// This is 99% an exact copy of the credit screen. Eventually we will make this into the options  //
//                                                                                                //

// This class is for the option screen
public class OptionScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel, createdByLabel2, createdByLabel3, createdByLabel4, createdByLabel5, createdByLabel6, createdByLabel7, createdByLabel8, createdByLabel9, createdByLabel10, createdByLabel11, createdByLabel12;
    protected SpriteFont returnInstructionsLabel;

    public OptionScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new BlankMap();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Options", 15, 7, "Times New Roman", 30, Color.red);
        createdByLabel = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 121, "Times New Roman", 20, Color.black);
        createdByLabel2 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 141, "Times New Roman", 20, Color.black);
        createdByLabel3 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 161, "Times New Roman", 20, Color.black);
        createdByLabel4 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 181, "Times New Roman", 20, Color.black);
        createdByLabel5 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 201, "Times New Roman", 20, Color.black);
        createdByLabel6 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 221, "Times New Roman", 20, Color.black);
        createdByLabel7 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 241, "Times New Roman", 20, Color.black);
        createdByLabel8 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 261, "Times New Roman", 20, Color.black);
        createdByLabel9 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 281, "Times New Roman", 20, Color.black);
        createdByLabel10 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 301, "Times New Roman", 20, Color.black);
        createdByLabel11 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 321, "Times New Roman", 20, Color.black);
        createdByLabel12 = new SpriteFont("You have NO OPTIONS but to start the game!", 130, 341, "Times New Roman", 20, Color.black);
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 532, "Times New Roman", 30, Color.lightGray);
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
        createdByLabel2.draw(graphicsHandler);
        createdByLabel3.draw(graphicsHandler);
        createdByLabel4.draw(graphicsHandler);
        createdByLabel5.draw(graphicsHandler);
        createdByLabel6.draw(graphicsHandler);
        createdByLabel7.draw(graphicsHandler);
        createdByLabel8.draw(graphicsHandler);
        createdByLabel9.draw(graphicsHandler);
        createdByLabel10.draw(graphicsHandler);
        createdByLabel11.draw(graphicsHandler);
        createdByLabel12.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
    }
}
