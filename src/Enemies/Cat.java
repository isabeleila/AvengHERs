package Enemies;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;
import Utils.Point;

import java.util.HashMap;

// This is the class for the Cat player character
// It now has the ability to shoot balls
public class Cat extends Player {

    // timer is used to determine how long the cat waits before shooting
    protected int shootWaitTimer;

    // timer is used to determine when a ball is to be shot out
    protected int shootTimer;

    // can be either WALK or SHOOT based on what the cat is currently set to do
    protected CatState catState;
    protected CatState previousCatState;

    // facing direction of the cat
    protected Direction facingDirection;

 // Increase the y coordinate (starting lower)
    public Cat(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Cat.png"), 24, 24), x, y, "STAND_RIGHT");
        gravity = 1f;
        terminalVelocityY = 6f;
        jumpHeight = 25f;
        jumpDegrade = 2f;
        walkSpeed = 1.9f;
        momentumYIncrease = .5f;

        catState = CatState.WALK;
        previousCatState = catState;
        shootWaitTimer = 100;
        facingDirection = Direction.RIGHT;  // Set default direction
    }


    @Override
    public void update() {
        super.update();

        // if shoot timer is up and cat is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && catState != CatState.SHOOT_WAIT) {
            catState = CatState.SHOOT_WAIT;
        }
        else {
            shootWaitTimer--;
        }

        // if cat is waiting to shoot, it waits for a certain number of frames
        if (catState == CatState.SHOOT_WAIT) {
            if (previousCatState == CatState.WALK) {
                shootTimer = 65;
                currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
            } else if (shootTimer == 0) {
                catState = CatState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }

        // this is for actually having the cat spit out the ball
        if (catState == CatState.SHOOT) {
            // define where the ball will spawn on map (x location) relative to cat's location
            // and define its movement speed
            int ballX;
            float movementSpeed;
            if (facingDirection == Direction.RIGHT) {
                ballX = Math.round(getX()) + getWidth();
                movementSpeed = 2.0f;
            } else {
                ballX = Math.round(getX() - 20);
                movementSpeed = -2.0f;
            }

            // define where the ball will spawn on the map (y location) relative to cat's location
            int ballY = Math.round(getY()) + 5;

            // create Ball enemy (similar to Fireball but with different properties)
            Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 50, this);

            // add ball enemy to the map for it to spawn in the level
            map.addEnemy(ball);

            // change cat back to its WALK state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            catState = CatState.WALK;

            // reset shoot wait timer so the process can happen again (cat walks around, then waits, then shoots)
            shootWaitTimer = 120;
        }

        previousCatState = catState;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        // drawBounds(graphicsHandler, new Color(255, 0, 0, 170));
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        int spriteWidth = 15;
        int spriteHeight = 24;

        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))  // (0, 0) is valid within 128x128
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)  // Bounds set to 24x24
                    .build()
            });

            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)  // Valid (1, 0)
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("WALK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)  // Valid within bounds
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("JUMP_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(2, 0))  // Second row, first column (0, 1)
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("JUMP_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(2, 0))  // Same as JUMP_RIGHT, flipped
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("FALL_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(3, 0))  // Third row, first column
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("FALL_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(3, 0))  // Same sprite as FALL_RIGHT, flipped
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("CROUCH_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(4, 0))  // Fourth row
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("CROUCH_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(4, 0))  // Same as CROUCH_RIGHT, flipped
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("DEATH_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(5, 0), 8)  // Fifth row
                    .withScale(3)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(5, 1), 8)
                    .withScale(3)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(5, 2), -1)
                    .withScale(3)
                    .build()
            });

            put("DEATH_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(5, 0), 8)  // Same as DEATH_RIGHT, flipped
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(5, 1), 8)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(5, 2), -1)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });

            // Add animations for shooting
            put("SHOOT_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(6, 0))
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("SHOOT_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(6, 0))
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });
        }};
    }

    // Direction enum for the cat's facing direction
    public enum Direction {
        LEFT, RIGHT
    }

    public enum CatState {
        WALK, SHOOT_WAIT, SHOOT
    }
}