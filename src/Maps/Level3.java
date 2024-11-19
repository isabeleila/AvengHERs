package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Tilesets.CommonTileset;

public class Level3 extends Map {
    public Level3(){
        super("level3_map.txt", new CommonTileset());
    }

    

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
