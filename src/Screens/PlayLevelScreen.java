package Screens;

import java.awt.Color;
import java.awt.Font;

import Enemies.Cat;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Engine.Sound;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.SpriteSheet;
import Level.HealthBarSprite;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Level.ShootBarSprite;
import Maps.PlayLevelMap;
import Maps.TestMap;
import SpriteFont.SpriteFont;
import Utils.Direction;


// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected CharacterSelectScreen selctScreen;
    protected Player player;
    protected Player player2;
    protected PlayLevelScreenState playLevelScreenState = PlayLevelScreenState.RUNNING;  // Initialize to a default value
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelFinishedScreen levelFinishedScreen;
    protected boolean levelCompletedStateChangeStart;

    Sound sound = new Sound();

    private SpriteFont playerOneText;
    private SpriteFont playerTwoText;

    protected HealthBarSprite healthBar1;
    protected HealthBarSprite healthBar2;

    protected ShootBarSprite shootBar1;
    protected ShootBarSprite shootBar2;

    private int select1;
    private int select2;
    private String player1Selected;
    private String player2Selected;

    protected PlayLevelMap background;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        playMusic(0);
    }

    public void initialize() {
        playMusic(0);
        // define/setup map
        this.map = new TestMap();

        //HulkSpriteSheet.png
        //spidermanSpriteSheet.png
        //CAPTAMERICAsheet.png
        //IRONMANsheet.png

        // Use these methods to get which character is selected
    // -1 is none selected yet
    // 0 is Character1 (Hulk)
    // 1 is Character2 (IronMan)
    // 2 is Character3 (CaptainAmerica)
    // 3 is Character4 (Spiderman)

        // setup player
        if(CharacterSelectScreen.getFlag()){
            select1 = CharacterSelectScreen.getCharacterL();
            if(select1 == 0){
                player1Selected = "GirlHulkSpriteSheetPlayer1.png";
            } else if(select1 == 1){
                player1Selected = "GirlIRONMANsheetPlayer1.png";
            } else if(select1 == 2){
                player1Selected = "GirlCAPTAMERICAsheetPlayer1.png";
            } else if(select1 == 3){
                player1Selected = "GirlspidermanSpriteSheetPlayer1.png";
            } else if(select1 == -1){
                player1Selected = "GirlHulkSpriteSheetPlayer1.png";
            }
    
            select2 = CharacterSelectScreen.getCharacterR();
            if(select2 == 0){
                player2Selected = "GirlHulkSpriteSheetPlayer2.png";
            } else if(select2 == 1){
                player2Selected = "GirlIRONMANsheetPlayer2.png";
            } else if(select2 == 2){
                player2Selected = "GirlCAPTAMERICAsheetPlayer2.png";
            } else if(select2 == 3){
                player2Selected = "GirlspidermanSpriteSheetPlayer2.png";
            } else if(select2 == -1){
                player2Selected = "GirlHulkSpriteSheetPlayer2.png";
            }
        }else{
            select1 = CharacterSelectScreen.getCharacterL();
            if(select1 == 0){
                player1Selected = "HulkSpriteSheetPlayer1.png";
            } else if(select1 == 1){
                player1Selected = "IRONMANsheetPlayer1.png";
            } else if(select1 == 2){
                player1Selected = "CAPTAMERICAsheetPlayer1.png";
            } else if(select1 == 3){
                player1Selected = "spidermanSpriteSheetPlayer1.png";
            } else if(select1 == -1){
                player1Selected = "HulkSpriteSheetPlayer1.png";
            }

            select2 = CharacterSelectScreen.getCharacterR();
            if(select2 == 0){
                player2Selected = "HulkSpriteSheetPlayer2.png";
            } else if(select2 == 1){
                player2Selected = "IRONMANsheetPlayer2.png";
            } else if(select2 == 2){
                player2Selected = "CAPTAMERICAsheetPlayer2.png";
            } else if(select2 == 3){
                player2Selected = "spidermanSpriteSheetPlayer2.png";
            } else if(select2 == -1){
                player2Selected = "HulkSpriteSheetPlayer2.png";
            }
        }


        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y-200, 1,player1Selected);
        this.player.setMap(map);
        this.player.addListener(this);

        // setup player2
        this.player2 = new Cat(map.getPlayerStartPosition().x+410, map.getPlayerStartPosition().y-200, 2, player2Selected);
        this.player2.setMap(map);
        this.player2.addListener(this);
        this.player2.setFacingDirection(Direction.LEFT);

        levelClearedScreen = new LevelClearedScreen();
        levelFinishedScreen = new LevelFinishedScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;

        playerOneText = new SpriteFont("Player 1", 65, 10, "Arial", 20, new Color(255, 0, 0));
        playerOneText.setFontStyle(Font.BOLD);
        playerOneText.setOutlineColor(Color.black);
        playerTwoText = new SpriteFont("Player 2", 620, 10, "Arial", 20, new Color(0, 0, 255));
        playerTwoText.setFontStyle(Font.BOLD);
        playerTwoText.setOutlineColor(Color.black);

        //Making the backgroun picture for the play level screen.
        //PlayLevelMap class holds the actaul image we are using here.
        background = new PlayLevelMap("backgroundDark.jpeg");
        background.setAdjustCamera(false);        

        //New Healthbar work:
        this.healthBar1 = new HealthBarSprite(new SpriteSheet(ImageLoader.load("HealthBarFINALfinal.png", Color.black), 68, 8), 158, 10, "DEFAULT", player.getPlayerHealth(), player);
        this.healthBar2 = new HealthBarSprite(new SpriteSheet(ImageLoader.load("HealthBarFINALfinal.png", Color.black), 68, 8), 400, 10, "DEFAULT", player2.getPlayerHealth(), player2);

        //New bars that show if plaer can shoot
        this.shootBar1 = new ShootBarSprite(new SpriteSheet(ImageLoader.load("ShootBarReal.png", Color.black), 32, 20), 25, 13, "DEFAULT", 3);
        this.shootBar2 = new ShootBarSprite(new SpriteSheet(ImageLoader.load("ShootBarReal.png", Color.black), 32, 20), 707, 13, "DEFAULT", 3);
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

                //Call to make healthbars update besed on a player's shootTimer
                shootBar1.updateSpecific(player.getShootTimer());
                shootBar2.updateSpecific(player2.getShootTimer());

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
                CharacterSelectScreen.setFlag(false);
                break;
            // wait on level lose screen to make a decision (either resets level or sends player back to main menu)
            case LEVEL_LOSE:
                levelFinishedScreen.update();
                //Logic that sends endLevel Screen which player has no more health
                levelFinishedScreen.updateDecl(player.getPlayerHealth(), player2.getPlayerHealth());
                CharacterSelectScreen.setFlag(false);
                break;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                background.draw(graphicsHandler);
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                playerOneText.draw(graphicsHandler);
                playerTwoText.draw(graphicsHandler);
                player2.draw(graphicsHandler);
                healthBar1.draw(graphicsHandler);
                healthBar2.draw(graphicsHandler);
                shootBar1.draw(graphicsHandler);
                shootBar2.draw(graphicsHandler);
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
        switch (player.getPlayerHealth()) {
            case 100:
                if(i == 1)
                    healthBar1.updateSpecific(0);
                else{
                    healthBar2.updateSpecific(0);
                    stopMusic();
                }
                break;
            case 90:
                if(i == 1)
                    healthBar1.updateSpecific(1);
                else{
                    healthBar2.updateSpecific(1);
                }
                break;
            case 80:
                if(i == 1)
                    healthBar1.updateSpecific(2);
                else{
                    healthBar2.updateSpecific(2);
                }
                break;
            case 70:
                if(i == 1)
                    healthBar1.updateSpecific(3);
                else{
                    healthBar2.updateSpecific(3);
                }
                break;
            case 60:
                if(i == 1)
                    healthBar1.updateSpecific(4);
                else{
                    healthBar2.updateSpecific(4);
                }
                break;
            case 50:
                if(i == 1)
                    healthBar1.updateSpecific(5);
                else{
                    healthBar2.updateSpecific(5);
                }
                break;
            case 40:
                if(i == 1)
                    healthBar1.updateSpecific(6);
                else{
                    healthBar2.updateSpecific(6);
                }
                break;
            case 30:
                if(i == 1)
                    healthBar1.updateSpecific(7);
                else{
                    healthBar2.updateSpecific(7);
                }
                break;
            case 20:
                if(i == 1)
                    healthBar1.updateSpecific(8);
                else{
                    healthBar2.updateSpecific(8);
                }
                break;
            case 10:
                if(i == 1)
                    healthBar1.updateSpecific(9);
                else{
                    healthBar2.updateSpecific(9);
                }
                break;
            case 0:
                if(i == 1)
                    healthBar1.updateSpecific(10);
                else{
                    healthBar2.updateSpecific(10);
                }
                break;
                
        }

        /* 
        if (player.getPlayerHealth() == 100) {
            if(i == 1)
                healthBar1.updateSpecific(0);
            else{
                healthBar2.updateSpecific(0);
                stopMusic();
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
        */
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

    public void setMap(Map map) {
        this.map=map;
        this.player.setMap(map);
        this.player2.setMap(map);
        this.map.reset();
    }
}
