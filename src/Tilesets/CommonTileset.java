package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.TileType;
import Level.Tileset;
import Utils.SlopeTileLayoutUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

// This class represents a "common" tileset of standard tiles defined in the CommonTileset.png file
public class CommonTileset extends Tileset {

    public CommonTileset() {
        super(ImageLoader.load("CommonTileset(3).png"), 16, 16, 3);
        //super(ImageLoader.load("White.jpg"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Scaffolding base block
        Frame scaffoldingFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder scaffoldingTile = new MapTileBuilder(scaffoldingFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(scaffoldingTile);

        // sky
        Frame skyFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder skyTile = new MapTileBuilder(skyFrame);

        mapTiles.add(skyTile);

        // right support
        Frame rightSupportFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightSupportTile = new MapTileBuilder(rightSupportFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(rightSupportTile);

        // sun
        Frame[] sunFrames = new Frame[]{
                new FrameBuilder(getSubImage(2, 0), 50)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(2, 1), 50)
                        .withScale(tileScale)
                        .build()
        };

        MapTileBuilder sunTile = new MapTileBuilder(sunFrames);

        mapTiles.add(sunTile);

        // tree trunk with full hole
        Frame treeTrunkWithFullHoleFrame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTrunkWithFullHoleTile = new MapTileBuilder(treeTrunkWithFullHoleFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTrunkWithFullHoleTile);

        // left end island
        Frame leftEndIslandFrame = new FrameBuilder(getSubImage(1, 5))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder leftEndIslandTile = new MapTileBuilder(leftEndIslandFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(leftEndIslandTile);

        // right end branch
        Frame rightEndIslandFrame = new FrameBuilder(getSubImage(1, 5))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .build();

        MapTileBuilder rightEndIslandTile = new MapTileBuilder(rightEndIslandFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(rightEndIslandTile);

        // tree trunk
        Frame treeTrunkFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTrunkTile = new MapTileBuilder(treeTrunkFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTrunkTile);

        // tree top leaves
        Frame treeTopLeavesFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTopLeavesTile = new MapTileBuilder(treeTopLeavesFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTopLeavesTile);

        // debris asset
        Frame[] debrisFrames = new Frame[] {
                new FrameBuilder(getSubImage(1, 2), 65)
                        .withScale(tileScale)
                        .build()
                //used for animation (not nessesary for this asset)
                // new FrameBuilder(getSubImage(1, 3), 65)
                //         .withScale(tileScale)
                //         .build(),
                // new FrameBuilder(getSubImage(1, 2), 65)
                //         .withScale(tileScale)
                //         .build(),
                // new FrameBuilder(getSubImage(1, 4), 65)
                //         .withScale(tileScale)
                //         .build()
        };

        MapTileBuilder debrisTile = new MapTileBuilder(debrisFrames);

        mapTiles.add(debrisTile);

        // purple flower
        Frame[] purpleFlowerFrames = new Frame[] {
                new FrameBuilder(getSubImage(0, 3), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(0, 4), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(0, 3), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(0, 5), 65)
                        .withScale(tileScale)
                        .build()
        };

        MapTileBuilder purpleFlowerTile = new MapTileBuilder(purpleFlowerFrames);

        mapTiles.add(purpleFlowerTile);

        // middle island
        Frame middleIslandFrame = new FrameBuilder(getSubImage(2, 3))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder middleIslandTile = new MapTileBuilder(middleIslandFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(middleIslandTile);

        // tree trunk hole top
        Frame treeTrunkHoleTopFrame = new FrameBuilder(getSubImage(2, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTrunkHoleTopTile = new MapTileBuilder(treeTrunkHoleTopFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTrunkHoleTopTile);

        // tree trunk hole bottom
        Frame treeTrunkHoleBottomFrame = new FrameBuilder(getSubImage(2, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTrunkHoleBottomTile = new MapTileBuilder(treeTrunkHoleBottomFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTrunkHoleBottomTile);

        // top water (animated surface)
        BufferedImage[] topWaterImgs = createTopWaterFrames();
        Frame[] topWaterFrames = new Frame[topWaterImgs.length];
        for (int i = 0; i < topWaterImgs.length; i++) {
            topWaterFrames[i] = new FrameBuilder(topWaterImgs[i], 25)
                    .withScale(tileScale)
                    .build();
        }
        MapTileBuilder topWaterTile = new MapTileBuilder(topWaterFrames);
        mapTiles.add(topWaterTile);

        // water (animated fill)
        BufferedImage[] waterImgs = createWaterFillFrames();
        Frame[] waterFrames = new Frame[waterImgs.length];
        for (int i = 0; i < waterImgs.length; i++) {
            waterFrames[i] = new FrameBuilder(waterImgs[i], 25)
                    .withScale(tileScale)
                    .build();
        }
        MapTileBuilder waterTile = new MapTileBuilder(waterFrames)
                .withTileType(TileType.WATER);
        mapTiles.add(waterTile);

        // grey rock
        Frame greyRockFrame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder greyRockTile = new MapTileBuilder(greyRockFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(greyRockTile);

        // left 45 degree slope
        Frame leftSlopeFrame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftSlopeTile = new MapTileBuilder(leftSlopeFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createLeft45SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(leftSlopeTile);

        // right 45 degree slope
        Frame rightSlopeFrame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightSlopeTile = new MapTileBuilder(rightSlopeFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createRight45SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(rightSlopeTile);

        // left 30 degree slope bottom
        Frame leftStairsBottomFrame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftStairsBottomTile = new MapTileBuilder(leftStairsBottomFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createBottomLeft30SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(leftStairsBottomTile);

        // left 30 degree slope top
        Frame leftStairsTopFrame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftStairsTopTile = new MapTileBuilder(leftStairsTopFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createTopLeft30SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(leftStairsTopTile);

        Frame leftSupportFrame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftSupportTile = new MapTileBuilder(leftSupportFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createTopLeft30SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(leftSupportTile);

        return mapTiles;
    }

    // Generates 4 frames of animated underwater fill — horizontal ripples scroll upward
    private BufferedImage[] createWaterFillFrames() {
        int w = spriteWidth;
        int h = spriteHeight;
        int[] pal = {
            0xFF0A3560,  // 0 deep
            0xFF155B9E,  // 1 medium
            0xFF2E83C4,  // 2 light
            0xFF5BAEE0,  // 3 highlight
            0xFF8FD0F0   // 4 shimmer
        };
        BufferedImage[] frames = new BufferedImage[4];
        for (int f = 0; f < 4; f++) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < h; y++) {
                int rowPhase = (y + f) % 4;
                for (int x = 0; x < w; x++) {
                    int color;
                    if (rowPhase == 0) {
                        int xp = (x + y / 4) % 8;
                        if (xp == 0)      color = pal[4];
                        else if (xp < 2)  color = pal[3];
                        else if (xp < 4)  color = pal[2];
                        else              color = pal[1];
                    } else if (rowPhase == 1) {
                        color = ((x + 2) % 8 < 3) ? pal[2] : pal[1];
                    } else if (rowPhase == 2) {
                        color = pal[1];
                    } else {
                        color = pal[0];
                    }
                    img.setRGB(x, y, color);
                }
            }
            frames[f] = img;
        }
        return frames;
    }

    // Generates 4 frames for the top water tile — surface waves with foam crests
    private BufferedImage[] createTopWaterFrames() {
        int w = spriteWidth;
        int h = spriteHeight;
        int[] pal = {
            0xFF0A3560,  // 0 deep
            0xFF155B9E,  // 1 medium
            0xFF2E83C4,  // 2 light
            0xFF5BAEE0,  // 3 highlight
            0xFF8FD0F0   // 4 shimmer/foam
        };
        BufferedImage[] frames = new BufferedImage[4];
        for (int f = 0; f < 4; f++) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int color;
                    if (y < 3) {
                        // Surface rows — moving foam/wave crests
                        int xp = (x + f * 4) % 16;
                        if (y == 1) {
                            if (xp < 2)       color = pal[4];
                            else if (xp < 4)  color = pal[3];
                            else if (xp < 7)  color = pal[2];
                            else if (xp == 8) color = pal[4];
                            else              color = pal[1];
                        } else if (y == 2) {
                            color = (xp < 5) ? pal[2] : pal[1];
                        } else {
                            color = pal[1];
                        }
                    } else {
                        // Below surface — same ripple fill as water tile
                        int rowPhase = (y + f) % 4;
                        if (rowPhase == 0) {
                            int xp = (x + y / 4) % 8;
                            color = (xp == 0) ? pal[4] : (xp < 2) ? pal[3] : (xp < 4) ? pal[2] : pal[1];
                        } else if (rowPhase == 1) {
                            color = ((x + 2) % 8 < 3) ? pal[2] : pal[1];
                        } else if (rowPhase == 2) {
                            color = pal[1];
                        } else {
                            color = pal[0];
                        }
                    }
                    img.setRGB(x, y, color);
                }
            }
            frames[f] = img;
        }
        return frames;
    }
}
