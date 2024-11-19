package Maps;

import Enemies.DinosaurEnemy;
//import Engine.ImageLoader;
//import EnhancedMapTiles.HorizontalMovingPlatform;
//import GameObject.Rectangle;
import Level.*;
import NPCs.Walrus;
import Tilesets.CommonTileset;
import Utils.Direction;

import java.util.ArrayList;

// Represents a test map to be used in a level
public class TestMap extends Map {

    public TestMap() {
        super("test_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(2, 10).getLocation();
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        DinosaurEnemy dinosaurEnemy = new DinosaurEnemy(getMapTile(19, 1).getLocation().addY(2), getMapTile(22, 1).getLocation().addY(2), Direction.RIGHT);
        enemies.add(dinosaurEnemy);

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
    ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        Walrus firstAid = new Walrus(getMapTile(7, 4).getLocation().addY(20));
        npcs.add(firstAid);

        return npcs;
    }
}
