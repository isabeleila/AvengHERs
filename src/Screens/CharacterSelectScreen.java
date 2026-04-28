package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;

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
    protected Sprite background;
    protected int pointerLocationX, pointerLocationY;
    protected boolean playerPressedStart1 = false;
    protected boolean playerPressedStart2 = false;
    protected boolean player1Ready = false;
    protected boolean player2Ready = false;
    protected KeyLocker keyLocker = new KeyLocker();
    Sound sound= new Sound();
    protected static boolean hiddenFlag = false;
    protected static boolean vsComputerMode = false;
    protected static int aiDifficulty = 0; // 0=Regular, 1=Hard, 2=Impossible
    protected SpriteFont difficultyText;
    protected SpriteFont difficultyRegular;
    protected SpriteFont difficultyHard;
    protected SpriteFont difficultyImpossible;
    protected SpriteFont p2PanelTitle;
    protected SpriteFont modePlayer2Option;
    protected SpriteFont modeSeparator;
    protected SpriteFont modeVsCpuOption;
    protected SpriteFont toggleHint;
    protected SpriteFont confirmHint;
    protected SpriteFont player2Label;
    protected SpriteFont cpuSelectHint;
    protected boolean p2SetupModalOpen = true;
    private static final int P2_CARD_WIDTH = 352;
    private static final int P2_CARD_HEIGHT_LOCAL = 124;
    private static final int P2_CARD_HEIGHT_VS_CPU = 200;

    public CharacterSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        
        // Only reset vsComputerMode if we're coming from the menu (not from level select)
        // This preserves the selection when going back from level select
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
        c4 = new Sprite(ImageLoader.load("Spiderman2.jpg"));
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
        hulkL = new Sprite(ImageLoader.load("HulkWithStats2.jpg"));
        hulkL.setWidth(hulkL.getWidth()-30);
        hulkL.setHeight(hulkL.getHeight()-30);
        hulkL.setLocation(hulkL.getX() + 10, hulkL.getY() + 10);
        ironmanL = new Sprite(ImageLoader.load("IronmanWithStats2.jpg"));
        ironmanL.setWidth(ironmanL.getWidth()-30);
        ironmanL.setHeight(ironmanL.getHeight()-30);
        ironmanL.setLocation(ironmanL.getX() + 190, ironmanL.getY() + 10);
        captainAmericaL = new Sprite(ImageLoader.load("CapWithStats2.jpg"));
        captainAmericaL.setWidth(captainAmericaL.getWidth()-30);
        captainAmericaL.setHeight(captainAmericaL.getHeight()-30);
        captainAmericaL.setLocation(captainAmericaL.getX() + 10, captainAmericaL.getY() + 270);
        spidermanL = new Sprite(ImageLoader.load("SpidermanWithStats2.jpg"));
        spidermanL.setWidth(spidermanL.getWidth()-30);
        spidermanL.setHeight(spidermanL.getHeight()-30);
        spidermanL.setLocation(spidermanL.getX() + 190, spidermanL.getY() + 270);
        // Characters Right
        hulkR = new Sprite(ImageLoader.load("HulkWithStats2.jpg"));
        hulkR.setWidth(hulkR.getWidth()-30);
        hulkR.setHeight(hulkR.getHeight()-30);
        hulkR.setLocation(hulkR.getX() + 400, hulkR.getY() + 10);
        ironmanR = new Sprite(ImageLoader.load("IronmanWithStats2.jpg"));
        ironmanR.setWidth(ironmanR.getWidth()-30);
        ironmanR.setHeight(ironmanR.getHeight()-30);
        ironmanR.setLocation(ironmanR.getX() + 580, ironmanR.getY() + 10);
        captainAmericaR = new Sprite(ImageLoader.load("CapWithStats2.jpg"));
        captainAmericaR.setWidth(captainAmericaR.getWidth()-30);
        captainAmericaR.setHeight(captainAmericaR.getHeight()-30);
        captainAmericaR.setLocation(captainAmericaR.getX() + 400, captainAmericaR.getY() + 270);
        spidermanR = new Sprite(ImageLoader.load("SpidermanWithStats2.jpg"));
        spidermanR.setWidth(spidermanR.getWidth()-30);
        spidermanR.setHeight(spidermanR.getHeight()-30);
        spidermanR.setLocation(spidermanR.getX() + 580, spidermanR.getY() + 270);
        // Player 1
        player1 = new SpriteFont("Player 1", 130, 533, "Arial", 30, new Color(255,255,255));
        player1.setOutlineColor(Color.black);
        player1.setOutlineThickness(4);
        // Player 2 / CPU label (right side)
        player2Label = new SpriteFont("Player 2", 530, 533, "Arial", 30, new Color(255,255,255));
        player2Label.setOutlineColor(Color.black);
        player2Label.setOutlineThickness(4);
        // Hint shown after P1 confirms, prompting CPU character selection
        cpuSelectHint = new SpriteFont("Pick CPU: arrows  |  SHIFT to confirm", 400, 555, "Arial", 14, new Color(255, 220, 0));
        cpuSelectHint.setOutlineColor(Color.black);
        cpuSelectHint.setOutlineThickness(2);
        cpuSelectHint.setFontStyle(Font.BOLD);
        // P2 / CPU setup
        p2PanelTitle = new SpriteFont("2nd player & CPU", 0, 0, "Arial", 18, new Color(45, 55, 75));
        p2PanelTitle.setOutlineColor(new Color(255, 255, 255));
        p2PanelTitle.setOutlineThickness(2);
        p2PanelTitle.setFontStyle(Font.BOLD);
        modePlayer2Option = new SpriteFont("Player 2", 0, 0, "Arial", 17, new Color(150, 150, 150));
        modePlayer2Option.setOutlineColor(Color.black);
        modePlayer2Option.setOutlineThickness(2);
        modeSeparator = new SpriteFont("|", 0, 0, "Arial", 16, new Color(160, 160, 160));
        modeSeparator.setOutlineColor(Color.black);
        modeSeparator.setOutlineThickness(1);
        modeVsCpuOption = new SpriteFont("vs CPU", 0, 0, "Arial", 17, new Color(150, 150, 150));
        modeVsCpuOption.setOutlineColor(Color.black);
        modeVsCpuOption.setOutlineThickness(2);
        toggleHint = new SpriteFont("Press C to switch mode", 0, 0, "Arial", 15, new Color(90, 90, 110));
        toggleHint.setOutlineColor(Color.white);
        toggleHint.setOutlineThickness(1);
        confirmHint = new SpriteFont("Press ENTER to confirm", 0, 0, "Arial", 16, new Color(25, 80, 140));
        confirmHint.setOutlineColor(new Color(255, 255, 255));
        confirmHint.setOutlineThickness(2);
        confirmHint.setFontStyle(Font.BOLD);
        difficultyText = new SpriteFont("CPU difficulty:", 0, 0, "Arial", 15, new Color(70, 70, 90));
        difficultyText.setOutlineColor(Color.white);
        difficultyText.setOutlineThickness(1);
        difficultyRegular = new SpriteFont("[1] Regular", 0, 0, "Arial", 15, new Color(0, 200, 0));
        difficultyRegular.setOutlineColor(Color.black);
        difficultyRegular.setOutlineThickness(2);
        difficultyHard = new SpriteFont("[2] Hard", 0, 0, "Arial", 15, new Color(150, 150, 150));
        difficultyHard.setOutlineColor(Color.black);
        difficultyHard.setOutlineThickness(2);
        difficultyImpossible = new SpriteFont("[3] Impossible", 0, 0, "Arial", 15, new Color(150, 150, 150));
        difficultyImpossible.setOutlineColor(Color.black);
        difficultyImpossible.setOutlineThickness(2);
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
        background = new Sprite(ImageLoader.load("White.jpg"));
        menuItemSelectedL = -1;
        menuItemSelectedR = -1;
        aiDifficulty = 0;  // Reset each time - P1 must select difficulty
        p2SetupModalOpen = true;
        keyLocker.lockKey(Key.SPACE);
        keyLocker.lockKey(Key.C);
        keyLocker.lockKey(Key.ENTER);
        keyLocker.lockKey(Key.ONE);
        keyLocker.lockKey(Key.TWO);
        keyLocker.lockKey(Key.THREE);
        playMusic(1);
    }

    public void update() {
        // update background map (to play tile animations)
        //background.update(null);

        // --- P2 / CPU setup modal (must confirm with ENTER before character select) ---
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (p2SetupModalOpen && !keyLocker.isKeyLocked(Key.ENTER) && Keyboard.isKeyDown(Key.ENTER)) {
            p2SetupModalOpen = false;
            keyLocker.lockKey(Key.ENTER);
        }

        // if keys are pressed, change menu item "hovered" over (blocked until modal dismissed)
        if (!p2SetupModalOpen && Keyboard.isKeyDown(Key.S)){
            if(currentMenuItemHoveredL == 0){
                currentMenuItemHoveredL = 2;
            }else if(currentMenuItemHoveredL == 1){
                currentMenuItemHoveredL = 3;
            }
        }else if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.W)){
            if(currentMenuItemHoveredL == 2){
                currentMenuItemHoveredL = 0;
            }else if(currentMenuItemHoveredL == 3){
                currentMenuItemHoveredL = 1;
            }
        }else if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.A)){
            if(currentMenuItemHoveredL == 1){
                currentMenuItemHoveredL = 0;
            }else if(currentMenuItemHoveredL == 3){
                currentMenuItemHoveredL = 2;
            }
        }else if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.D)){
            if(currentMenuItemHoveredL == 0){
                currentMenuItemHoveredL = 1;
            }else if(currentMenuItemHoveredL == 2){
                currentMenuItemHoveredL = 3;
            }
        }

        if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.DOWN)){
            if(currentMenuItemHoveredR == 0){
                currentMenuItemHoveredR = 2;
            }else if(currentMenuItemHoveredR == 1){
                currentMenuItemHoveredR = 3;
            }
        }else if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.UP)){
            if(currentMenuItemHoveredR == 2){
                currentMenuItemHoveredR = 0;
            }else if(currentMenuItemHoveredR == 3){
                currentMenuItemHoveredR = 1;
            }
        }else if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.LEFT)){
            if(currentMenuItemHoveredR == 1){
                currentMenuItemHoveredR = 0;
            }else if(currentMenuItemHoveredR == 3){
                currentMenuItemHoveredR = 2;
            }
        }else if(!p2SetupModalOpen && Keyboard.isKeyDown(Key.RIGHT)){
            if(currentMenuItemHoveredR == 0){
                currentMenuItemHoveredR = 1;
            }else if(currentMenuItemHoveredR == 2){
                currentMenuItemHoveredR = 3;
            }
        }

        if(Keyboard.isKeyDown(Key.H) && Keyboard.isKeyDown(Key.E) && Keyboard.isKeyDown(Key.R)){
            hiddenFlag = true;
        }
        
        // Toggle vs Computer mode with C key (on press, not release)
        // Allow toggling as long as player 1 hasn't confirmed their selection
        if (Keyboard.isKeyUp(Key.C)) {
            keyLocker.unlockKey(Key.C);
        }
        if (!keyLocker.isKeyLocked(Key.C) && Keyboard.isKeyDown(Key.C) && p2SetupModalOpen && !player1Ready) {
            vsComputerMode = !vsComputerMode;
            keyLocker.lockKey(Key.C);
        }
        
        // Difficulty selection with 1, 2, 3 keys (only on modal + vs CPU)
        if (Keyboard.isKeyUp(Key.ONE)) keyLocker.unlockKey(Key.ONE);
        if (Keyboard.isKeyUp(Key.TWO)) keyLocker.unlockKey(Key.TWO);
        if (Keyboard.isKeyUp(Key.THREE)) keyLocker.unlockKey(Key.THREE);
        if (p2SetupModalOpen && vsComputerMode && !player1Ready && !player2Ready) {
            if (!keyLocker.isKeyLocked(Key.ONE) && Keyboard.isKeyDown(Key.ONE)) {
                aiDifficulty = 0;
                keyLocker.lockKey(Key.ONE);
            } else if (!keyLocker.isKeyLocked(Key.TWO) && Keyboard.isKeyDown(Key.TWO)) {
                aiDifficulty = 1;
                keyLocker.lockKey(Key.TWO);
            } else if (!keyLocker.isKeyLocked(Key.THREE) && Keyboard.isKeyDown(Key.THREE)) {
                aiDifficulty = 2;
                keyLocker.lockKey(Key.THREE);
            }
        }
        
        // Update difficulty text colors based on selection
        if (aiDifficulty == 0) {
            difficultyRegular.setColor(new Color(0, 255, 0));
            difficultyHard.setColor(new Color(150, 150, 150));
            difficultyImpossible.setColor(new Color(150, 150, 150));
        } else if (aiDifficulty == 1) {
            difficultyRegular.setColor(new Color(150, 150, 150));
            difficultyHard.setColor(new Color(255, 165, 0));  // Orange
            difficultyImpossible.setColor(new Color(150, 150, 150));
        } else if (aiDifficulty == 2) {
            difficultyRegular.setColor(new Color(150, 150, 150));
            difficultyHard.setColor(new Color(150, 150, 150));
            difficultyImpossible.setColor(new Color(255, 0, 0));  // Red
        }
        
        // Update P2 mode indicator colors - highlight active option
        if (vsComputerMode) {
            modePlayer2Option.setColor(new Color(150, 150, 150));
            modeVsCpuOption.setColor(new Color(0, 200, 0));
        } else {
            modePlayer2Option.setColor(new Color(0, 200, 0));
            modeVsCpuOption.setColor(new Color(150, 150, 150));
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
        if (Keyboard.isKeyUp(Key.Q)) {
            keyLocker.unlockKey(Key.Q);
        }
        if (!p2SetupModalOpen && !keyLocker.isKeyLocked(Key.Q) && Keyboard.isKeyDown(Key.Q)) {
            if (!vsComputerMode && playerPressedStart2) {
                menuItemSelectedL = currentMenuItemHoveredL;
                stopMusic();
                screenCoordinator.setGameState(GameState.LEVELSELECT);
            } else if (!player1Ready) {
                playerPressedStart1 = true;
                menuItemSelectedL = currentMenuItemHoveredL;
                ReadyScreen(currentMenuItemHoveredL, 0);
                player1Ready = true;
                // In vs CPU mode the player now picks the CPU character on the right
                // side with arrow keys and confirms with SHIFT — no auto-assignment.
            }
        }

        if (Keyboard.isKeyUp(Key.SHIFT)) {
            keyLocker.unlockKey(Key.SHIFT);
        }
        if (!p2SetupModalOpen && !keyLocker.isKeyLocked(Key.SHIFT) && Keyboard.isKeyDown(Key.SHIFT)) {
            if (vsComputerMode) {
                // SHIFT confirms the CPU character — only allowed after P1 has picked theirs
                if (player1Ready) {
                    menuItemSelectedR = currentMenuItemHoveredR;
                    ReadyScreen(currentMenuItemHoveredR, 1);
                    stopMusic();
                    screenCoordinator.setGameState(GameState.LEVELSELECT);
                }
            } else {
                if (playerPressedStart1) {
                    menuItemSelectedR = currentMenuItemHoveredR;
                    stopMusic();
                    screenCoordinator.setGameState(GameState.LEVELSELECT);
                } else if (!player2Ready) {
                    playerPressedStart2 = true;
                    menuItemSelectedR = currentMenuItemHoveredR;
                    ReadyScreen(currentMenuItemHoveredR, 1);
                    player2Ready = true;
                }
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
        if (vsComputerMode) {
            if (player1Ready) {
                cpuSelectHint.draw(graphicsHandler);
            } else {
                player2Label.setText("CPU");
                player2Label.draw(graphicsHandler);
            }
        } else {
            player2Label.setText("Player 2");
            player2Label.draw(graphicsHandler);
        }
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

        // Centered setup modal — only until ENTER; dim rest of screen
        if (p2SetupModalOpen) {
            int sw = ScreenManager.getScreenWidth();
            int sh = ScreenManager.getScreenHeight();
            graphicsHandler.drawFilledRectangle(0, 0, sw, sh, new Color(0, 0, 0, 110));
            int cardH = vsComputerMode ? P2_CARD_HEIGHT_VS_CPU : P2_CARD_HEIGHT_LOCAL;
            int cardW = Math.min(P2_CARD_WIDTH, Math.max(200, sw - 8));
            int px = Math.max(4, (sw - cardW) / 2);
            int py = Math.max(4, (sh - cardH) / 2);
            layoutP2SetupCard(px, py, vsComputerMode);
            graphicsHandler.drawFilledRectangle(px + 4, py + 4, cardW, cardH, new Color(0, 0, 0, 45));
            graphicsHandler.drawFilledRectangleWithBorder(px, py, cardW, cardH,
                    new Color(248, 249, 252), new Color(55, 70, 105), 2);
            p2PanelTitle.draw(graphicsHandler);
            modePlayer2Option.draw(graphicsHandler);
            modeSeparator.draw(graphicsHandler);
            modeVsCpuOption.draw(graphicsHandler);
            toggleHint.draw(graphicsHandler);
            if (vsComputerMode) {
                difficultyText.draw(graphicsHandler);
                difficultyRegular.draw(graphicsHandler);
                difficultyHard.draw(graphicsHandler);
                difficultyImpossible.draw(graphicsHandler);
            }
            confirmHint.draw(graphicsHandler);
        }
    }

    /** Places all P2/CPU labels inside the centered card (coordinates = card top-left). */
    private void layoutP2SetupCard(int px, int py, boolean vsCpu) {
        p2PanelTitle.setLocation(px + 14, py + 8);
        modePlayer2Option.setLocation(px + 14, py + 36);
        modeSeparator.setLocation(px + 100, py + 36);
        modeVsCpuOption.setLocation(px + 118, py + 36);
        toggleHint.setLocation(px + 14, py + 60);
        if (vsCpu) {
            difficultyText.setLocation(px + 14, py + 84);
            difficultyRegular.setLocation(px + 18, py + 106);
            difficultyHard.setLocation(px + 18, py + 126);
            difficultyImpossible.setLocation(px + 18, py + 146);
            confirmHint.setLocation(px + 14, py + 172);
        } else {
            confirmHint.setLocation(px + 14, py + 88);
        }
    }

        public void playMusic(int i){
            sound.setFile(i);
            sound.play();
            sound.loop();
        }
    
        public void stopMusic(){
            sound.stop();
        }

        public static boolean getFlag(){
            return hiddenFlag;
        }

        public static void setFlag(boolean value){
            hiddenFlag = value;
        }
        
        public static boolean isVsComputerMode() {
            return vsComputerMode;
        }
        
        public static void setVsComputerMode(boolean value) {
            vsComputerMode = value;
        }
        
        public static int getAIDifficulty() {
            return aiDifficulty;
        }
}