package Enemies;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Keyboard;
import Engine.SoundEffect;
import Engine.Key;
import Engine.KeyLocker;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;
import Utils.Point;
import Engine.GamePanel;

import java.util.HashMap;

public class Cat extends Player {

    protected int shootWaitTimer;
    protected int shootTimer;
    protected CatState catState;
    protected CatState previousCatState;
    protected Direction facingDirection;
    protected KeyLocker keyLocker;
    protected int playerNumberOut;
    protected int catHealth;
    protected String character;
    GamePanel gp;
    SoundEffect soundEffect = new SoundEffect();

 // Increase the y coordinate (starting lower)
    public Cat(float x, float y, int playerNumber, String character) {
        super(new SpriteSheet(ImageLoader.load(character), 24, 24), x, y, "STAND_RIGHT", 100);
        gravity = 1f;
        terminalVelocityY = 6f;
        jumpHeight = 25f;
        jumpDegrade = 2f;
        walkSpeed = 1.9f;
        momentumYIncrease = .5f;

        catState = CatState.WALK;
        previousCatState = catState;
        shootWaitTimer = 0;
        facingDirection = Direction.RIGHT;
        keyLocker = new KeyLocker();
        facingDirection = Direction.RIGHT;  // Set default direction

        if(playerNumber == 1){
            JUMP_KEY = Key.W;
            MOVE_LEFT_KEY = Key.A;
            MOVE_RIGHT_KEY = Key.D;
            CROUCH_KEY = Key.S;
        }
        playerNumberOut = playerNumber;
        this.character = character;
    }

    @Override
    public void update() {
        super.update();

        if (currentAnimationName.contains("LEFT")) {
            facingDirection = Direction.LEFT;
        } else if (currentAnimationName.contains("RIGHT")) {
            facingDirection = Direction.RIGHT;
        }
        if(playerNumberOut == 1){
            if (Keyboard.isKeyDown(Key.CTRL) && !keyLocker.isKeyLocked(Key.CTRL)) {
                catState = CatState.SHOOT_WAIT;
                keyLocker.lockKey(Key.CTRL);
            }

            if (Keyboard.isKeyUp(Key.CTRL)) {
                keyLocker.unlockKey(Key.CTRL);
            }
        }else{

            if (Keyboard.isKeyDown(Key.SHIFT) && !keyLocker.isKeyLocked(Key.SHIFT)) {
                catState = CatState.SHOOT_WAIT;
                keyLocker.lockKey(Key.SHIFT);
            }

            if (Keyboard.isKeyUp(Key.SHIFT)) {
                keyLocker.unlockKey(Key.SHIFT);
            }
        }

        if (catState == CatState.SHOOT_WAIT) {
            if (previousCatState == CatState.WALK) {
                shootTimer = 0;
                currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
            } else if (shootTimer == 0) {
                playMusic(1);
                catState = CatState.SHOOT;
                //stopMusic();
                //System.out.println("stopped");
            } else {
                shootTimer--;
                
            }
        }

        if (catState == CatState.SHOOT) {
            int ballX;
            float movementSpeed;
            if (facingDirection == Direction.RIGHT) {
                ballX = Math.round(getX()) + getWidth();
                movementSpeed = 2.0f;
            } else {
                ballX = Math.round(getX()) - 5;
                movementSpeed = -2.0f;
            }

            int ballY = Math.round(getY()) + 20;

            //Setting the right sprite for the Projectiles
            //Dependant on Direction of the player
            if(facingDirection == Direction.LEFT){
                if(character.equals("HulkSpriteSheet.png")){
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "HulkBall.png");
                    map.addEnemy(ball);

                }else if(character.equals("IRONMANsheet.png")){
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "IronBall.png");
                    map.addEnemy(ball);

                }else if(character.equals("CAPTAMERICAsheet.png")){
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "CaptBall.png");
                    map.addEnemy(ball);

                }else{
                    //Spiderman sprite sheet
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "SpiderBall.png");
                    map.addEnemy(ball);

                }
            }else{
                if(character.equals("HulkSpriteSheet.png")){
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "HulkBallR.png");
                    map.addEnemy(ball);

                }else if(character.equals("IRONMANsheet.png")){
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "IronBall.png");
                    map.addEnemy(ball);

                }else if(character.equals("CAPTAMERICAsheet.png")){
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "CaptBall.png");
                    map.addEnemy(ball);

                }else{
                    //Spiderman sprite sheet
                    Ball ball = new Ball(new Point(ballX, ballY), movementSpeed, 400, this, "SpiderBallR.png");
                    map.addEnemy(ball);

                }
            }

            catState = CatState.WALK;
            shootWaitTimer = 100;
        }

        previousCatState = catState;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        int spriteWidth = 15;
        int spriteHeight = 19;

        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
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
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
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
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
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
                new FrameBuilder(spriteSheet.getSprite(2, 0))
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("JUMP_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(2, 0))
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("FALL_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(3, 0))
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("FALL_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(3, 0))
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("CROUCH_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(4, 0))
                    .withScale(3)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("CROUCH_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(4, 0))
                    .withScale(3)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0, 0, spriteWidth, spriteHeight)
                    .build()
            });

            put("DEATH_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(5, 0), 8)
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
                new FrameBuilder(spriteSheet.getSprite(5, 0), 8)
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

    public enum Direction {
        LEFT, RIGHT
    }

    public enum CatState {
        WALK, SHOOT_WAIT, SHOOT
    }

    public void playMusic(int i){
		soundEffect.setFile(i);
		soundEffect.play();
		//soundEffect.loop();
	}

	public void stopMusic(){
		soundEffect.stop();
	}

	public void playSE(int i){
		soundEffect.setFile(i);
		soundEffect.play();
	}
}
