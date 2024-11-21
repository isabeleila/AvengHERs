package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Level.Map;
import Screens.CharacterSelectScreen;
import Screens.CreditsScreen;
import Screens.LevelSelectScreen;
import Screens.MenuScreen;
import Screens.PlayLevelScreen;
import Screens.TutorialScreen;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */
public class ScreenCoordinator extends Screen {

	protected Map levelMapStorage;
	// currently shown Screen
	protected Screen currentScreen = new DefaultScreen();

	// keep track of gameState so ScreenCoordinator knows which Screen to show
	protected GameState gameState;
	protected GameState previousGameState;

	public GameState getGameState() {
		return gameState;
	}

	// Other Screens can set the gameState of this class to force it to change the currentScreen
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public void SetLevel(Map levelMap) {
		levelMapStorage = levelMap;
		System.out.println("Set Level");
	}

	@Override
	public void initialize() {
		// start game off with Menu Screen
		gameState = GameState.MENU;
	}

	@Override
	public void update() {
		do {
			// if previousGameState does not equal gameState, it means there was a change in gameState
			// this triggers ScreenCoordinator to bring up a new Screen based on what the gameState is
			if (previousGameState != gameState) {
				switch(gameState) {
					case MENU:
						currentScreen = new MenuScreen(this);
						break;
					case LEVEL:
						currentScreen = new PlayLevelScreen(this);
						break;
					case CREDITS:
						currentScreen = new CreditsScreen(this);
						break;
					case TUTORIAL:
						currentScreen = new TutorialScreen(this);
						break;
					case CHARACTER:
						currentScreen = new CharacterSelectScreen(this);
						break;
					case LEVELSELECT:
						currentScreen = new LevelSelectScreen(this);
						break;
				}
				currentScreen.initialize();
				if(currentScreen instanceof PlayLevelScreen){ ((PlayLevelScreen)(currentScreen)).setMap(levelMapStorage); }
			}
			previousGameState = gameState;

			// call the update method for the currentScreen
			currentScreen.update();
		} while (previousGameState != gameState);
	}

	@Override
	public void draw(GraphicsHandler graphicsHandler) {
		// call the draw method for the currentScreen
		currentScreen.draw(graphicsHandler);
	}
}