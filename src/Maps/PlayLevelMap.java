package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;

// Represents the map that is used as a background for the playable game screen.  To change background
//simply change the image that corresponds to 'map' in the PlayLevelScreen class (line 126 'background object')
public class PlayLevelMap extends Map {

    // private Sprite cat, cat2;
    private Sprite background;

    public PlayLevelMap(String map) {
        super("title_screen_map.txt", new CommonTileset());

        background = new Sprite(ImageLoader.load(map));
        background.setWidth(90);
        background.setHeight(65);
        background.setScale(9);
        background.setLocation(background.getX()-25, background.getY());
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        background.draw(graphicsHandler);
    }

}
