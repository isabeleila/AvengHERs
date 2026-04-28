package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Level.MapTile;
import Level.TileType;
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
            player.setPlayerHealth(0);
        }

        // water kills instantly
        float centerX = player.getX() + player.getWidth() / 2f;
        float feetY = player.getY() + player.getHeight() - 4f;
        MapTile tile = getTileByPosition(centerX, feetY);
        if (tile != null && tile.getTileType() == TileType.WATER) {
            player.setLevelState(LevelState.PLAYER_DEAD);
            player.setPlayerHealth(0);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
