package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import Engine.Key; //USED FOR WASD KEYS
import Engine.Keyboard;
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

public class BugEnemy extends Enemy {

    private float gravity = 1f;
    private float movementSpeed = 4f;
    private Direction startFacingDirection;
    private Direction facingDirection;
    private float jumpHeight = 40f;
    private float terminalVelocityY = 6f;
private AirGroundState airGroundState;

    public BugEnemy(Point location, Direction facingDirection) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Cat.png"), 24,24 ), "WALK_LEFT");
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        facingDirection = startFacingDirection;
        airGroundState = AirGroundState.GROUND;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
        }
         // airGroundState = AirGroundState.GROUND;
    }

    @Override
    public void update(Player player) {
        float moveAmountX = 0;
        float moveAmountY = 0;

        //gravity
        if(airGroundState == AirGroundState.AIR){
            moveAmountY += gravity;
            if(moveAmountY > terminalVelocityY){
                moveAmountY = terminalVelocityY;
            }
        }

        //WASD input 
        if(Keyboard.isKeyDown(Key.W)){
            if (airGroundState == AirGroundState.GROUND){
                moveAmountY -= jumpHeight;
                airGroundState = AirGroundState.AIR;
            }
        }
        if(Keyboard.isKeyDown(Key.S)){
            //no special movement (no crouching just falling)
        }
        if(Keyboard.isKeyDown(Key.A)){
            moveAmountX -= movementSpeed;
            facingDirection = Direction.LEFT;
            currentAnimationName = "WALK_LEFT";
        }
        if(Keyboard.isKeyDown(Key.D)){
            moveAmountX += movementSpeed;
            facingDirection = Direction.RIGHT;
            currentAnimationName = "WALK_RIGHT";
        }

        //move
        moveYHandleCollision(moveAmountY);
        moveXHandleCollision(moveAmountX);

        super.update(player);
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        //remove automove & turning 
        // if(hasCollided){
        //     if (direction == Direction.DOWN && hasCollided){
        //         facingDirection = Direction.LEFT;
        //         currentAnimationName = "WALK_LEFT";
        //     } else {
        //         facingDirection = Direction.RIGHT;
        //         currentAnimationName = "WALK_RIGHT";
        //     }
        // }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        if(direction ==Direction.DOWN){
            if(hasCollided){
                airGroundState = AirGroundState.GROUND;
            } else {
                airGroundState = AirGroundState.AIR;
            }
        }
    }

    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame []> () {{
            put ("WALK_LEFT", new Frame[]{
                new FrameBuilder (spriteSheet.getSprite(0,0),8)
                .withScale(3)
                .withBounds(6,6,12,7)
                .build(),
                
                new FrameBuilder(spriteSheet.getSprite(0,1),8)
                .withScale(2)
                .withBounds(6,6,12,7)
                .build()
            });
            
            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 6, 12, 7)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                    .withScale(2)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(6, 6, 12, 7)
                    .build()
            });


        }};
    }
    
}

    
    


//         // if on ground, walk forward based on facing direction
//         if (airGroundState == AirGroundState.GROUND) {
//             if (facingDirection == Direction.RIGHT) {
//                 moveAmountX += movementSpeed;
//             } else {
//                 moveAmountX -= movementSpeed;
//             }
//         }

//         // move bug
//         moveYHandleCollision(moveAmountY);
//         moveXHandleCollision(moveAmountX);

//         super.update(player);
//     }

//     @Override
//     public void onEndCollisionCheckX(boolean hasCollided, Direction direction,  MapEntity entityCollidedWith) {
//         // if bug has collided into something while walking forward,
//         // it turns around (changes facing direction)
//         if (hasCollided) {
//             if (direction == Direction.RIGHT) {
//                 facingDirection = Direction.LEFT;
//                 currentAnimationName = "WALK_LEFT";
//             } else {
//                 facingDirection = Direction.RIGHT;
//                 currentAnimationName = "WALK_RIGHT";
//             }
//         }
//     }

//     @Override
//     public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
//         // if bug is colliding with the ground, change its air ground state to GROUND
//         // if it is not colliding with the ground, it means that it's currently in the air, so its air ground state is changed to AIR
//         if (direction == Direction.DOWN) {
//             if (hasCollided) {
//                 airGroundState = AirGroundState.GROUND;
//             } else {
//                 airGroundState = AirGroundState.AIR;
//             }
//         }
//     }

//     @Override
//     public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
//         return new HashMap<String, Frame[]>() {{
//             put("WALK_LEFT", new Frame[] {
//                     new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
//                             .withScale(2)
//                             .withBounds(6, 6, 12, 7)
//                             .build(),
//                     new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
//                             .withScale(2)
//                             .withBounds(6, 6, 12, 7)
//                             .build()
//             });

//             put("WALK_RIGHT", new Frame[] {
//                     new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
//                             .withScale(2)
//                             .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
//                             .withBounds(6, 6, 12, 7)
//                             .build(),
//                     new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
//                             .withScale(2)
//                             .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
//                             .withBounds(6, 6, 12, 7)
//                             .build()
//             });
//         }};
//     }
// }
