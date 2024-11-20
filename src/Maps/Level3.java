package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Tilesets.CommonTileset;
import Level.LevelState;

public class Level3 extends Map {
    public Level3() {
        super("level3_map.txt", new CommonTileset());
    }

    @Override
    public void update(Level.Player player) {
        super.update(player);

        // check if player if out of the window 
        if (player.getY() > getCamera().getEndBoundY()) {
            player.setLevelState(LevelState.PLAYER_DEAD);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
