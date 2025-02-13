package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntity;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the green dinosaur enemy that shoots fireballs
// It walks back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will shoot a Fireball enemy
public class DinosaurEnemy extends Enemy {

    // start and end location defines the two points that it walks between
    // is only made to walk along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long dinosaur freezes in place before shooting fireball
    protected int shootWaitTimer;

    // timer is used to determine when a fireball is to be shot out
    protected int shootTimer;

    // can be either WALK or SHOOT based on what the enemy is currently set to do
    protected DinosaurState dinosaurState;
    protected DinosaurState previousDinosaurState;

    public DinosaurEnemy(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("DinosaurEnemy.png"), 14, 17), "WALK_RIGHT", 1);
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        dinosaurState = DinosaurState.WALK;
        previousDinosaurState = dinosaurState;
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
        }
        airGroundState = AirGroundState.GROUND;

        // every certain number of frames, the fireball will be shot out
        shootWaitTimer = 30;
    }

    @Override
    public void update(Player player) {
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // if shoot timer is up and dinosaur is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && dinosaurState != DinosaurState.SHOOT_WAIT) {
            dinosaurState = DinosaurState.SHOOT_WAIT;
        }
        else {
            shootWaitTimer--;
        }

        // if dinosaur is walking, determine which direction to walk in based on facing direction
        if (dinosaurState == DinosaurState.WALK) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "WALK_RIGHT";
                moveXHandleCollision(movementSpeed);
            } else {
                currentAnimationName = "WALK_LEFT";
                moveXHandleCollision(-movementSpeed);
            }

            // if dinosaur reaches the start or end location, it turns around
            // dinosaur may end up going a bit past the start or end location depending on movement speed
            // this calculates the difference and pushes the enemy back a bit so it ends up right on the start or end location
            if (getX1() + getWidth() >= endBound) {
                float difference = endBound - (getX2());
                moveXHandleCollision(-difference);
                facingDirection = Direction.LEFT;
            } else if (getX1() <= startBound) {
                float difference = startBound - getX1();
                moveXHandleCollision(difference);
                facingDirection = Direction.RIGHT;
            }
        }

        // if dinosaur is waiting to shoot, it first turns read for a set number of frames
        // after this waiting period is over, the fireball is actually shot out
        if (dinosaurState == DinosaurState.SHOOT_WAIT) {
            if (previousDinosaurState == DinosaurState.WALK) {
                shootTimer = 65;
                currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
            } else if (shootTimer == 0) {
                dinosaurState = DinosaurState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }

        // this is for actually having the dinosaur spit out the fireball
        if (dinosaurState == DinosaurState.SHOOT) {
            // define where fireball will spawn on map (x location) relative to dinosaur enemy's location
            // and define its movement speed
            int fireballX;
            float movementSpeed;
            if (facingDirection == Direction.RIGHT) {
                fireballX = Math.round(getX()) + getWidth();
                movementSpeed = 1.5f;
            } else {
                fireballX = Math.round(getX() - 21);
                movementSpeed = -1.5f;
            }

            // define where fireball will spawn on the map (y location) relative to dinosaur enemy's location
            int fireballY = Math.round(getY()) + 4;

            // create Fireball enemy
            Fireball fireball = new Fireball(new Point(fireballX, fireballY), movementSpeed, 60, "Fireball.png");

            // add fireball enemy to the map for it to spawn in the level
            map.addEnemy(fireball);

            // change dinosaur back to its WALK state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            dinosaurState = DinosaurState.WALK;

            // reset shoot wait timer so the process can happen again (dino walks around, then waits, then shoots)
            shootWaitTimer = 130;
        }

        super.update(player);

        previousDinosaurState = dinosaurState;
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if dinosaur enemy collides with something on the x axis, it turns around and walks the other way
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "WALK_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "WALK_RIGHT";
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("WALK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3)
                            .withBounds(4, 2, 5, 13)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(3)
                            .withBounds(4, 2, 5, 13)
                            .build()
            });

            put("WALK_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 2, 5, 13)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 2, 5, 13)
                            .build()
            });

            put("SHOOT_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0))
                            .withScale(3)
                            .withBounds(4, 2, 5, 13)
                            .build(),
            });

            put("SHOOT_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 2, 5, 13)
                            .build(),
            });
        }};
    }

    public enum DinosaurState {
        WALK, SHOOT_WAIT, SHOOT
    }
}
