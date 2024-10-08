package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;
// import Utils.Colors;
// import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMap extends Map {

    // private Sprite cat, cat2;
    private Sprite background;

    public TitleScreenMap() {
        super("title_screen_map.txt", new CommonTileset());

        background = new Sprite(ImageLoader.load("StartingScreenBackground.jpg"));
        background.setWidth(90);
        background.setHeight(65);
        background.setScale(9);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        background.draw(graphicsHandler);
    }

}
