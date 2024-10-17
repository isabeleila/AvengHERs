package Screens;

import java.awt.Color;

import Enemies.Cat;
import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.Sound;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Maps.TestMap;
import SpriteFont.SpriteFont;


// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected Player player2;
    protected PlayLevelScreenState playLevelScreenState = PlayLevelScreenState.RUNNING;  // Initialize to a default value
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelLoseScreen levelLoseScreen;
    protected boolean levelCompletedStateChangeStart;

    Sound sound = new Sound();

    private SpriteFont playerOneText;
    private SpriteFont playerTwoText;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        playMusic(0);
    }

    public void initialize() {
        // define/setup map
        this.map = new TestMap();

        // setup player
        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y, 1);
        this.player.setMap(map);
        this.player.addListener(this);

        // setup player2
        this.player2 = new Cat(map.getPlayerStartPosition().x+100, map.getPlayerStartPosition().y, 2);
        this.player2.setMap(map);
        this.player2.addListener(this);

        levelClearedScreen = new LevelClearedScreen();
        levelLoseScreen = new LevelLoseScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;

        playerOneText = new SpriteFont("Player 1", 135, 10, "Arial", 20, new Color(0, 0, 0));
        playerOneText.setOutlineColor(Color.black);
        playerTwoText = new SpriteFont("Player 2", 510, 10, "Arial", 20, new Color(0, 0, 0));
        playerTwoText.setOutlineColor(Color.black);
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
                levelLoseScreen.update();
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
                playMusic(0);
                break;
            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE:
                levelLoseScreen.draw(graphicsHandler);
                break;
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
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
