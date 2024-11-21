package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Tilesets.CommonTileset;

public class Level1 extends Map {
    public Level1() {
        super("level1_map.txt", new CommonTileset());
    }

    @Override
    public void update(Level.Player player) {
        super.update(player);

    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
