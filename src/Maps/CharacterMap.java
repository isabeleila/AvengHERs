package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Tilesets.CommonTileset;

// Represents the map that is used as a background for the main menu and credits menu screen
public class CharacterMap extends Map {

    // private Sprite cat, cat2;

    public CharacterMap() {
        super("title_screen_map.txt", new CommonTileset());
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

}
