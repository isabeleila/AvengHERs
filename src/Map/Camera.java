package Map;

import GameObject.Rectangle;

import Engine.Graphics;

import java.awt.*;

public class Camera extends Rectangle {

    private Map map;
    private int tileWidth, tileHeight;
    private int leftoverSpaceX, leftoverSpaceY;

    public Camera(int startX, int startY, Rectangle screenBounds, int tileWidth, int tileHeight, Map map) {
        super(startX, startY, screenBounds.getWidth() / tileWidth, screenBounds.getHeight() / tileHeight);
        this.map = map;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.leftoverSpaceX = screenBounds.getWidth() % tileWidth;
        this.leftoverSpaceY = screenBounds.getHeight() % tileHeight;
    }

    public Point getTileIndexByCameraPosition() {
        int xIndex = getX() / Math.round(tileWidth);
        int yIndex = getY() / Math.round(tileHeight);
        return new Point(xIndex, yIndex);
    }

    public void update() {
        Point tileIndex = getTileIndexByCameraPosition();
        for (int i = tileIndex.y - 1; i <= tileIndex.y + height + 1; i++) {
            for (int j = tileIndex.x - 1; j <= tileIndex.x + width + 1; j++) {
                MapTile tile = map.getTile(j, i);
                if (tile != null) {
                    int tileStartX = j * tile.getScaledWidth();
                    int tileStartY = i * tile.getScaledHeight();
                    tile.setX(tileStartX - getX());
                    tile.setY(tileStartY - getY());
                    tile.update();
                }
            }
        }
    }

    public void draw(Graphics graphics) {
        Point tileIndex = getTileIndexByCameraPosition();
        for (int i = tileIndex.y - 1; i <= tileIndex.y + height + 1; i++) {
            for (int j = tileIndex.x - 1; j <= tileIndex.x + width + 1; j++) {
                MapTile tile = map.getTile(j, i);
                if (tile != null) {
                    tile.draw(graphics);
                }
            }
        }
    }

    public int getStartBoundX() {
        return getX();
    }

    public int getStartBoundY() {
        return getY();
    }

    public int getEndBoundX() {
        return getX1() + (width * tileWidth) + leftoverSpaceX;
    }

    public int getEndBoundY() {
        return getY1() + (height * tileHeight) + leftoverSpaceY;
    }
}
