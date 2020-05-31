package Scene;

import Engine.Config;
import Engine.GraphicsHandler;
import Engine.Keyboard;
import Engine.ScreenManager;
import Game.Kirby;
import MapEntities.Enemy;
import MapEntities.EnhancedMapTile;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Map {
    protected MapTile[] mapTiles;
    protected int width;
    protected int height;
    protected Tileset tileset;
    protected Camera camera;
    protected Point playerStartTile;
    protected int xMidPoint, yMidPoint;
    protected int startBoundX, startBoundY, endBoundX, endBoundY;
    protected String mapFileName;

    protected ArrayList<Enemy> enemies;
    protected ArrayList<Enemy> activeEnemies;
    protected ArrayList<EnhancedMapTile> enhancedMapTiles;
    protected ArrayList<EnhancedMapTile> activeEnhancedMapTiles;

    public Map(String mapFileName, Tileset tileset, Point playerStartTile) {
        this.mapFileName = mapFileName;
        this.tileset = tileset;
        loadMapFile();
        this.camera = new Camera(0, 0, tileset.getScaledSpriteWidth(), tileset.getScaledSpriteHeight(), this);
        this.startBoundX = 0;
        this.startBoundY = 0;
        this.endBoundX = width * tileset.getScaledSpriteWidth();
        this.endBoundY = height * tileset.getScaledSpriteHeight();
        this.xMidPoint = ScreenManager.getScreenWidth() / 2;
        this.yMidPoint = (ScreenManager.getScreenHeight() / 2);
        this.playerStartTile = playerStartTile;

        this.enemies = loadEnemies();
        this.activeEnemies = this.enemies;
        this.enhancedMapTiles = loadEnhancedMapTiles();
        this.activeEnhancedMapTiles = this.enhancedMapTiles;
    }

    private void loadMapFile() {
        Scanner fileInput;
        try {
            fileInput = new Scanner(new File(Config.MAP_FILES_PATH + this.mapFileName));
        } catch(FileNotFoundException ex) {
            System.out.println("Map file " + Config.MAP_FILES_PATH + this.mapFileName + " not found! Creating empty map file...");

            try {
                createEmptyMapFile();
                fileInput = new Scanner(new File(Config.MAP_FILES_PATH + this.mapFileName));
            } catch(IOException ex2) {
                ex2.printStackTrace();
                System.out.println("Failed to create an empty map file!");
                throw new RuntimeException();
            }
        }

        this.width = fileInput.nextInt();
        this.height = fileInput.nextInt();
        this.mapTiles = new MapTile[this.height * this.width];
        fileInput.nextLine();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int tileIndex = fileInput.nextInt();
                MapTile tile = tileset.getTile(tileIndex)
                        .build(j * tileset.getScaledSpriteWidth(), i * tileset.getScaledSpriteHeight());
                setMapTile(j, i, tile);
            }
        }

        fileInput.close();
    }

    private void createEmptyMapFile() throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(Config.MAP_FILES_PATH + this.mapFileName);
        fileWriter.write("0 0\n");
        fileWriter.close();
    }

    public Point getPlayerStartPosition() {
        MapTile tile = getMapTile(playerStartTile.x, playerStartTile.y);
        return new Point(tile.getX(), tile.getY());
    }

    public Point getPositionByTileIndex(int xIndex, int yIndex) {
        MapTile tile = getMapTile(xIndex, yIndex);
        return new Point(tile.getX(), tile.getY());
    }

    public Tileset getTileset() {
        return tileset;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidthPixels() {
        return width * tileset.getScaledSpriteWidth();
    }

    public int getHeightPixels() {
        return height * tileset.getScaledSpriteHeight();
    }

    public MapTile[] getMapTiles() {
        return mapTiles;
    }

    public void setMapTiles(MapTile[] mapTiles) {
        this.mapTiles = mapTiles;
    }

    public MapTile getMapTile(int x, int y) {
        if (isInBounds(x, y)) {
            return mapTiles[getConvertedIndex(x, y)];
        } else {
            return null;
        }
    }

    public void setMapTile(int x, int y, MapTile tile) {
        mapTiles[getConvertedIndex(x, y)] = tile;
    }

    public MapTile getTileByPosition(int xPosition, int yPosition) {
        Point tileIndex = getTileIndexByPosition(xPosition, yPosition);
        if (isInBounds(tileIndex.x, tileIndex.y)) {
            return getMapTile(tileIndex.x, tileIndex.y);
        } else {
            return null;
        }
    }

    public Point getTileIndexByPosition(int xPosition, int yPosition) {
        int xIndex = (xPosition + camera.getX()) / tileset.getScaledSpriteWidth();
        int yIndex = (yPosition + camera.getY()) / tileset.getScaledSpriteHeight();
        return new Point(xIndex, yIndex);
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    private int getConvertedIndex(int x, int y) {
        return x + width * y;
    }

    protected ArrayList<Enemy> loadEnemies() {
        return new ArrayList<>();
    }
    protected ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<EnhancedMapTile> getEnhancedMapTiles() {
        return enhancedMapTiles;
    }

    public Camera getCamera() {
        return camera;
    }

    public void update(Keyboard keyboard, Kirby player) {
        activeEnemies = getActiveEnemies();
        activeEnhancedMapTiles = getActiveEnhancedMapTiles();

        for (Enemy enemy: activeEnemies) {
            enemy.update(keyboard, this, player);
        }
        for (EnhancedMapTile enhancedMapTile: activeEnhancedMapTiles) {
            enhancedMapTile.update(keyboard, this, player);
        }

        adjustMovementY(player);
        adjustMovementX(player);

        camera.update();
    }

    private ArrayList<Enemy> getActiveEnemies() {
        ArrayList<Enemy> activeEnemies = new ArrayList<>();
        for (Enemy enemy: enemies) {
            int amountMovedX = enemy.getStartPositionX() + enemy.getAmountMovedX() - camera.getAmountMovedX();
            int amountMovedY = enemy.getStartPositionY() + enemy.getAmountMovedY() - camera.getAmountMovedY();
            enemy.setX(amountMovedX + Math.abs(enemy.getXRaw() - (int)enemy.getXRaw()));
            enemy.setY(amountMovedY + Math.abs(enemy.getYRaw() - (int)enemy.getYRaw()));
            if (enemy.exists() && (camera.contains(enemy) || enemy.isUpdateWhileOffScreen())) {
                activeEnemies.add(enemy);
            }
        }
        return activeEnemies;
    }

    private ArrayList<EnhancedMapTile> getActiveEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> activeEnhancedMapTiles = new ArrayList<>();
        for (EnhancedMapTile enhancedMapTile: enhancedMapTiles) {
            int amountMovedX = enhancedMapTile.getStartPositionX() + enhancedMapTile.getAmountMovedX() - camera.getAmountMovedX();
            int amountMovedY = enhancedMapTile.getStartPositionY() + enhancedMapTile.getAmountMovedY() - camera.getAmountMovedY();
            enhancedMapTile.setX(amountMovedX);
            enhancedMapTile.setY(amountMovedY);
            if (enhancedMapTile.exists() && (camera.contains(enhancedMapTile) || enhancedMapTile.isUpdateWhileOffScreen())) {
                activeEnhancedMapTiles.add(enhancedMapTile);
            }
        }
        return activeEnhancedMapTiles;
    }

    private void adjustMovementX(Kirby player) {
        int xMidPointDifference = 0;
        if (player.getX() > xMidPoint && camera.getEndBoundX() < endBoundX) {
            xMidPointDifference = xMidPoint - player.getX();

            for (Enemy enemy : activeEnemies) {
                enemy.moveX(xMidPointDifference);
            }
            for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                enhancedMapTile.moveX(xMidPointDifference);
            }

            player.moveX(xMidPointDifference);
            camera.moveX(-xMidPointDifference);
            if (camera.getEndBoundX() > endBoundX) {
                int cameraDifference = camera.getEndBoundX() - endBoundX;

                for (Enemy enemy : activeEnemies) {
                    enemy.moveX(cameraDifference);
                }
                for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                    enhancedMapTile.moveX(cameraDifference);
                }

                player.moveX(cameraDifference);
                camera.moveX(-cameraDifference);
            }
        } else if (player.getX() < xMidPoint && camera.getX() > startBoundX) {
            xMidPointDifference = xMidPoint - player.getX();

            for (Enemy enemy : activeEnemies) {
                enemy.moveX(xMidPointDifference);
            }
            for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                enhancedMapTile.moveX(xMidPointDifference);
            }

            player.moveX(xMidPointDifference);
            camera.moveX(-xMidPointDifference);
            if (camera.getX() < startBoundX) {
                int cameraDifference = startBoundX - camera.getX();

                for (Enemy enemy : activeEnemies) {
                    enemy.moveX(-cameraDifference);
                }
                for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                    enhancedMapTile.moveX(-cameraDifference);
                }

                player.moveX(-cameraDifference);
                camera.moveX(cameraDifference);
            }
        }
    }

    private void adjustMovementY(Player player) {
        int yMidPointDifference = 0;
        if (player.getY() > yMidPoint && camera.getEndBoundY() < endBoundY) {
            yMidPointDifference = yMidPoint - player.getY();

            for (Enemy enemy : activeEnemies) {
                enemy.moveY(yMidPointDifference);
            }
            for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                enhancedMapTile.moveY(yMidPointDifference);
            }

            player.moveY(yMidPointDifference);
            camera.moveY(-yMidPointDifference);
            if (camera.getEndBoundY() > endBoundY) {
                int cameraDifference = camera.getEndBoundY() - endBoundY;

                for (Enemy enemy : activeEnemies) {
                    enemy.moveY(cameraDifference);
                }
                for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                    enhancedMapTile.moveX(cameraDifference);
                }

                player.moveY(cameraDifference);
                camera.moveY(-cameraDifference);
            }
        } else if (player.getY() < yMidPoint && camera.getY() > startBoundY) {
            yMidPointDifference = yMidPoint - player.getY();

            for (Enemy enemy : activeEnemies) {
                enemy.moveY(yMidPointDifference);
            }
            for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                enhancedMapTile.moveY(yMidPointDifference);
            }

            player.moveY(yMidPointDifference);
            camera.moveY(-yMidPointDifference);
            if (camera.getY() < startBoundY) {
                int cameraDifference = startBoundY - camera.getY();

                for (Enemy enemy : activeEnemies) {
                    enemy.moveY(-cameraDifference);
                }
                for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
                    enhancedMapTile.moveY(-cameraDifference);
                }

                player.moveY(-cameraDifference);
                camera.moveY(cameraDifference);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        camera.draw(graphicsHandler);

        for (Enemy enemy : activeEnemies) {
            if (camera.contains(enemy)) {
                enemy.draw(graphicsHandler);
            }
        }
        for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
            if (camera.contains(enhancedMapTile)) {
                enhancedMapTile.draw(graphicsHandler);
            }
        }
    }
}
