package Screens;

import java.awt.Color;
import java.awt.Font;

import Enemies.Cat;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.SpriteSheet;
import Level.HealthBarSprite;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Maps.TestMap;
import SpriteFont.SpriteFont;
import Utils.Direction;


// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected Player player2;
    protected PlayLevelScreenState playLevelScreenState = PlayLevelScreenState.RUNNING;  // Initialize to a default value
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelFinishedScreen levelFinishedScreen;
    protected boolean levelCompletedStateChangeStart;

    private SpriteFont playerOneText;
    private SpriteFont playerTwoText;

    protected HealthBarSprite healthBar1;
    protected HealthBarSprite healthBar2;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        // define/setup map
        this.map = new TestMap();

        // setup player
        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y, 1);
        this.player.setMap(map);
        this.player.addListener(this);

        // setup player2
        this.player2 = new Cat(map.getPlayerStartPosition().x+500, map.getPlayerStartPosition().y, 2);
        this.player2.setMap(map);
        this.player2.addListener(this);
        this.player2.setFacingDirection(Direction.LEFT);

        levelClearedScreen = new LevelClearedScreen();
        levelFinishedScreen = new LevelFinishedScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;

        playerOneText = new SpriteFont("Player 1", 135, 10, "Arial", 20, new Color(0, 0, 0));
        playerOneText.setFontStyle(Font.BOLD);
        playerOneText.setOutlineColor(Color.black);
        playerTwoText = new SpriteFont("Player 2", 510, 10, "Arial", 20, new Color(0, 0, 0));
        playerTwoText.setFontStyle(Font.BOLD);
        playerTwoText.setOutlineColor(Color.black);

        //New Healthbar work:
        this.healthBar1 = new HealthBarSprite(new SpriteSheet(ImageLoader.load("HealthSheet.png", Color.black), 32, 8), 225, 10, "DEFAULT", player.getPlayerHealth(), player);
        this.healthBar2 = new HealthBarSprite(new SpriteSheet(ImageLoader.load("HealthSheet.png", Color.black), 32, 8), 400, 10, "DEFAULT", player2.getPlayerHealth(), player2);
    }

    public void update() {
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                player.update();
                player2.update();
                map.update(player);
                map.update(player2);

                //Call to update healthbars every tick
                updateHealthBarGraphic(player, 1);
                updateHealthBarGraphic(player2, 2);

                //Once one player dies, the other is set to invincible
                if(player.getPlayerHealth() <= 0){
                    player2.setInvincible();
                }else if(player2.getPlayerHealth() <= 0){
                    player.setInvincible();
                }

                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                if (levelCompletedStateChangeStart) {
                    screenTimer = 130;
                    levelCompletedStateChangeStart = false;
                } else {
                    levelClearedScreen.update();
                    screenTimer--;
                    if (screenTimer == 0) {
                        goBackToMenu();
                    }
                }
                break;
            // wait on level lose screen to make a decision (either resets level or sends player back to main menu)
            case LEVEL_LOSE:
                levelFinishedScreen.update();
                //Logic that sends endLevel Screen which player has no more health
                levelFinishedScreen.updateDecl(player.getPlayerHealth(), player2.getPlayerHealth());
                break;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                playerOneText.draw(graphicsHandler);
                playerTwoText.draw(graphicsHandler);
                player2.draw(graphicsHandler);
                healthBar1.draw(graphicsHandler);
                healthBar2.draw(graphicsHandler);
                break;
            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE:
                levelFinishedScreen.draw(graphicsHandler);
                break;
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    //logic used to decide which health bar gets updated, when, and to what frame.
    public void updateHealthBarGraphic(Player player, int i){
        //changes the healthbar
        if (player.getPlayerHealth() == 100) {
            if(i == 1)
                healthBar1.updateSpecific(0);
            else{
                healthBar2.updateSpecific(0);
            }
        }else if(player.getPlayerHealth() < 100 && player.getPlayerHealth() > 64){
            if(i == 1)
                healthBar1.updateSpecific(1);
            else{
                healthBar2.updateSpecific(1);
            }
        }else if(player.getPlayerHealth() < 64 && player.getPlayerHealth() > 1){
            if(i == 1)
                healthBar1.updateSpecific(2);
            else{
                healthBar2.updateSpecific(2);
            }
        }else{
            if(i == 1)
                healthBar1.updateSpecific(3);
            else{
                healthBar2.updateSpecific(3);
            }
        }
    }


    @Override
    public void onLevelCompleted() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_COMPLETED) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
            levelCompletedStateChangeStart = true;
        }
    }

    @Override
    public void onDeath() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_LOSE) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
        }
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, LEVEL_LOSE
    }
}
