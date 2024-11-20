package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Tilesets.CommonTileset;

public class Level2 extends Map {
    public Level2(){
        super("level2_map.txt", new CommonTileset());
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
    
}
