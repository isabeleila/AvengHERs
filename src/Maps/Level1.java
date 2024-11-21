package Maps;

import Engine.GraphicsHandler;
import Level.LevelState;
import Level.Map;
import Tilesets.CommonTileset;

public class Level1 extends Map {
    public Level1() {
        super("level1_map.txt", new CommonTileset());
    }

    @Override
    public void update(Level.Player player) {
        super.update(player);
        if (player.getY() > getCamera().getEndBoundY()) {
            player.setLevelState(LevelState.PLAYER_DEAD);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
