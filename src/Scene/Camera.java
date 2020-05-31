package Scene;

import Engine.ScreenManager;
import GameObject.GameObject;
import GameObject.IntersectableRectangle;
import GameObject.Rectangle;

import Engine.GraphicsHandler;
import com.sun.org.apache.regexp.internal.RE;

import java.awt.*;
import java.util.ArrayList;

public class Camera extends Rectangle {

    private float startPositionX, startPositionY;
    private Map map;
    private int tileWidth, tileHeight;
    private int leftoverSpaceX, leftoverSpaceY;
    private float amountMovedX, amountMovedY;

    public Camera(int startX, int startY, int tileWidth, int tileHeight, Map map) {
        super(startX, startY, ScreenManager.getScreenWidth() / tileWidth, ScreenManager.getScreenHeight() / tileHeight);
        this.map = map;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.leftoverSpaceX = ScreenManager.getScreenWidth() % tileWidth;
        this.leftoverSpaceY = ScreenManager.getScreenHeight() % tileHeight;
        this.startPositionX = startX;
        this.startPositionY = startY;
    }

    public Point getTileIndexByCameraPosition() {
        int xIndex = getX() / tileWidth;
        int yIndex = getY() / tileHeight;
        return new Point(xIndex, yIndex);
    }

    public void update() {
        Point tileIndex = getTileIndexByCameraPosition();
        for (int i = tileIndex.y - 1; i <= tileIndex.y + height + 1; i++) {
            for (int j = tileIndex.x - 1; j <= tileIndex.x + width + 1; j++) {
                MapTile tile = map.getMapTile(j, i);
                if (tile != null) {
                    int tileStartX = j * tile.getScaledWidth();
                    int tileStartY = i * tile.getScaledHeight();
                    tile.setX(tileStartX - getAmountMovedX());
                    tile.setY(tileStartY - getAmountMovedY());
                    tile.update();
                }
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        Point tileIndex = getTileIndexByCameraPosition();
        for (int i = tileIndex.y - 1; i <= tileIndex.y + height + 1; i++) {
            for (int j = tileIndex.x - 1; j <= tileIndex.x + width + 1; j++) {
                MapTile tile = map.getMapTile(j, i);
                if (tile != null) {
                    tile.draw(graphicsHandler);
                }
            }
        }
    }

    public boolean contains(MapEntity mapEntity) {
        return getX1() < mapEntity.getX() + mapEntity.getScaledWidth() + (startPositionX + amountMovedX) && getEndBoundX() >  mapEntity.getX() + (startPositionX + amountMovedX) &&
                getY1() <  mapEntity.getY() + (startPositionY + amountMovedY) + mapEntity.getScaledHeight() && getEndBoundY() >  mapEntity.getY() + (startPositionY + amountMovedY);
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

    public int getStartPositionX() {
        return (int)startPositionX;
    }

    public int getStartPositionY() {
        return (int)startPositionY;
    }

    public int getAmountMovedX() {
        return (int)amountMovedX;
    }

    public int getAmountMovedY() {
        return (int)amountMovedY;
    }

    @Override
    public void moveX(float dx) {
        this.amountMovedX += dx;
        super.moveX(dx);
    }

    @Override
    public void moveY(float dy) {
        this.amountMovedY += dy;
        super.moveY(dy);
    }
}
