package GameObject;

import Engine.ImageLoader;
import Engine.GraphicsHandler;

public class Wall {
    private int x, y, width, height;
    private SpriteSheet spriteSheet;

    public Wall(int x, int y, int width, int height) {
        this.x = 300;
        this.y = 180;
        this.width = width;
        this.height = height;
        this.spriteSheet = new SpriteSheet(ImageLoader.load("wall.jpg"), width, height); // Load the wall image
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawImage(spriteSheet.getSprite(0, 0), x, y, width, height);
    }
}
