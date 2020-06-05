package Scene;

import Builders.MapTileBuilder;
import Engine.ImageLoader;
import Builders.FrameBuilder;
import GameObject.SpriteSheet;
import Utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Tileset extends SpriteSheet {

    protected float tileScale = 1f;
    protected HashMap<Integer, MapTileBuilder> tiles;
    protected MapTileBuilder defaultTile;

    public Tileset(BufferedImage image, int tileWidth, int tileHeight) {
        super(image, tileWidth, tileHeight);
        this.tiles = mapDefinedTilesToIndex();
        this.defaultTile = getDefaultTile();
    }

    public Tileset(BufferedImage image, int tileWidth, int tileHeight, int tileScale) {
        super(image, tileWidth, tileHeight);
        this.tileScale = tileScale;
        this.tiles = mapDefinedTilesToIndex();
        this.defaultTile = getDefaultTile();
    }

    public abstract ArrayList<MapTileBuilder> defineTiles();

    public MapTileBuilder getTile(int tileNumber) {
        return tiles.getOrDefault(tileNumber, defaultTile);
    }

    public float getTileScale() {
        return tileScale;
    }

    public int getScaledSpriteWidth() {
        return Math.round(spriteWidth * tileScale);
    }

    public int getScaledSpriteHeight() {
        return Math.round(spriteHeight * tileScale);
    }

    public HashMap<Integer, MapTileBuilder> mapDefinedTilesToIndex() {
        ArrayList<MapTileBuilder> mapTileBuilders = defineTiles();
        HashMap<Integer, MapTileBuilder> tilesToIndex = new HashMap<>();
        for (int i = 0; i < mapTileBuilders.size(); i++) {
            tilesToIndex.put(i, mapTileBuilders.get(i).withTileIndex(i));
        }
        return tilesToIndex;
    }

    public MapTileBuilder getDefaultTile() {
        BufferedImage defaultTileImage = ImageLoader.load("DefaultTile.png");
        return new MapTileBuilder(new FrameBuilder(ImageUtils.resizeImage(defaultTileImage, spriteWidth, spriteHeight), 0).withScale(tileScale).build());
    }
}
