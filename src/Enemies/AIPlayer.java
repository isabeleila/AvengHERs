package Enemies;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Keyboard;
import Engine.Key;
import Engine.KeyLocker;
import Engine.SoundEffect;
import GameObject.SpriteSheet;
import Level.Map;
import Level.MapTile;
import Level.NavigationNode;
import Level.NavigationSystem;
import Level.Player;
import Level.PlayerState;
import Level.LevelState;
import Level.TileType;
import Enemies.Cat.Direction;
import Utils.AirGroundState;
import Utils.Point;
import java.util.Random;

/**
 * AIPlayer - A computer-controlled player that uses AI decision making
 * to fight against a human opponent. Uses NavigationSystem for platform
 * pathfinding and integrates with the physics pipeline for collision-aware movement.
 */
public class AIPlayer extends Cat {

    public enum Difficulty {
        REGULAR,
        HARD,
        IMPOSSIBLE
    }

    private Player opponent;
    private Random random;
    private int decisionTimer;
    private Difficulty difficulty;
    private NavigationSystem navigationSystem;

    // AI intent - set each decision cycle, consumed by physics
    private int aiMoveDirection = 0;  // -1=left, 0=none, 1=right
    private boolean aiWantsToJump = false;

    // Smooth movement — interpolates toward aiMoveDirection * walkSpeed each frame
    private float aiSmoothSpeedX = 0f;
    private static final float AI_ACCEL = 0.25f; // fraction of gap closed per frame

    // Behavior parameters
    private int SHOOT_RANGE;
    private int RETREAT_HEALTH;
    private int DECISION_INTERVAL;
    private int JUMP_UP_THRESHOLD;
    private float HESITATION_CHANCE;
    private int PREFERRED_DISTANCE;
    private int TOO_CLOSE_DISTANCE;
    private int shootCooldownFrames;
    private int strafeSpeed;
    
    // Stuck detection
    private int stuckFrames;
    private int STUCK_THRESHOLD;
    private int reactionDelay;
    private int shootAnimationTimer = 0;
    private int currentShootCooldown = 0;

    public AIPlayer(float x, float y, int playerNumber, String character, Player opponent) {
        this(x, y, playerNumber, character, opponent, Difficulty.REGULAR);
    }

    public AIPlayer(float x, float y, int playerNumber, String character, Player opponent, Difficulty difficulty) {
        super(x, y, playerNumber, character);
        this.opponent = opponent;
        this.random = new Random();
        this.decisionTimer = 0;
        this.difficulty = difficulty;
        setDifficultyParameters();
        normalizeAIStats();
    }
    
    private void normalizeAIStats() {
        gravity = 1f;
        terminalVelocityY = 7f;
        jumpHeight = 28f;
        jumpDegrade = 2f;
        walkSpeed = 3.5f;
        momentumYIncrease = 0.6f;
    }

    private void setDifficultyParameters() {
        switch (difficulty) {
            case REGULAR:
                SHOOT_RANGE = 120;
                RETREAT_HEALTH = 50;
                DECISION_INTERVAL = 10;
                JUMP_UP_THRESHOLD = 180;
                HESITATION_CHANCE = 0.40f;
                PREFERRED_DISTANCE = 50;
                TOO_CLOSE_DISTANCE = 20;
                STUCK_THRESHOLD = 25;
                reactionDelay = 5;
                shootCooldownFrames = 45;
                strafeSpeed = 0;
                break;
            case HARD:
                SHOOT_RANGE = 130;
                RETREAT_HEALTH = 30;
                DECISION_INTERVAL = 3;
                JUMP_UP_THRESHOLD = 120;
                HESITATION_CHANCE = 0.15f;
                PREFERRED_DISTANCE = 55;
                TOO_CLOSE_DISTANCE = 25;
                STUCK_THRESHOLD = 12;
                reactionDelay = 2;
                shootCooldownFrames = 25;
                strafeSpeed = 1;
                break;
            case IMPOSSIBLE:
                SHOOT_RANGE = 200;
                RETREAT_HEALTH = 10;
                DECISION_INTERVAL = 1;
                JUMP_UP_THRESHOLD = 80;
                HESITATION_CHANCE = 0.0f;
                PREFERRED_DISTANCE = 30;
                TOO_CLOSE_DISTANCE = 15;
                STUCK_THRESHOLD = 5;
                reactionDelay = 0;
                shootCooldownFrames = 10;
                strafeSpeed = 2;
                break;
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void setMap(Map map) {
        super.setMap(map);
        if (map != null) {
            this.navigationSystem = map.getNavigationSystem();
        }
    }

    @Override
    public void update() {
        try {
            if (currentShootCooldown > 0) {
                currentShootCooldown--;
            }
            
            if (opponent != null && levelState == LevelState.RUNNING && getBounds() != null) {
                updateAIDecisions();
            }
        } catch (Exception e) {
        }
        
        // Smoothly interpolate horizontal speed toward the current intent each frame.
        // This prevents instant starts, stops, and direction reversals.
        float targetSpeedX = aiMoveDirection * walkSpeed;
        aiSmoothSpeedX += (targetSpeedX - aiSmoothSpeedX) * AI_ACCEL;
        if (Math.abs(aiSmoothSpeedX) < 0.05f) aiSmoothSpeedX = 0f;

        try {
            super.update();
        } catch (Exception e) {
        }
        
        if (shootAnimationTimer > 0) {
            shootAnimationTimer--;
            currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
        } else if (opponent != null && levelState == LevelState.RUNNING) {
            try {
                if (getBounds() != null && opponent.getBounds() != null) {
                    float myX = getBounds().getX1() + getBounds().getWidth() / 2f;
                    float oppX = opponent.getBounds().getX1() + opponent.getBounds().getWidth() / 2f;
                    facingDirection = oppX > myX ? Direction.RIGHT : Direction.LEFT;
                    
                    if (playerState == PlayerState.STANDING) {
                        currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
                    } else if (playerState == PlayerState.WALKING) {
                        currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
                    } else if (playerState == PlayerState.JUMPING) {
                        currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Run AI decisions before physics. Uses A* pathfinding to navigate platforms,
     * sets aiMoveDirection and aiWantsToJump for the physics step.
     */
    private void updateAIDecisions() {
        if (getBounds() == null || opponent == null || opponent.getBounds() == null) {
            return;
        }

        float myX = getBounds().getX1() + getBounds().getWidth() / 2f;
        float myY = getBounds().getY1() + getBounds().getHeight() / 2f;
        float oppX = opponent.getBounds().getX1() + opponent.getBounds().getWidth() / 2f;
        float oppY = opponent.getBounds().getY1() + opponent.getBounds().getHeight() / 2f;
        float dist = Math.abs(myX - oppX);
        boolean oppRight = oppX > myX;

        // Always face the opponent regardless of movement direction
        facingDirection = oppRight ? Direction.RIGHT : Direction.LEFT;

        // While airborne, maintain the committed jump arc — don't re-plan movement.
        // Only allow shooting mid-air.
        if (airGroundState == AirGroundState.AIR) {
            if (dist <= SHOOT_RANGE && shootTimer == 0 && currentShootCooldown <= 0) {
                aiShoot();
            }
            return;
        }

        // Stuck detection (only meaningful on ground)
        if (Math.abs(lastAmountMovedX) < 0.5f && aiMoveDirection != 0) {
            stuckFrames++;
        } else {
            stuckFrames = 0;
        }

        decisionTimer++;
        if (decisionTimer < DECISION_INTERVAL + reactionDelay) {
            if (dist <= SHOOT_RANGE && shootTimer == 0 && currentShootCooldown <= 0) {
                aiShoot();
            }
            return;
        }
        decisionTimer = 0;

        if (random.nextFloat() < HESITATION_CHANCE) {
            aiMoveDirection = 0;
            aiWantsToJump = false;
            return;
        }

        aiMoveDirection = 0;
        aiWantsToJump = false;

        // Stuck recovery: reverse and jump to break free
        if (stuckFrames >= STUCK_THRESHOLD) {
            aiMoveDirection = oppRight ? -1 : 1;
            aiWantsToJump = true;
            stuckFrames = 0;
            if (dist <= SHOOT_RANGE && shootTimer == 0 && currentShootCooldown <= 0) aiShoot();
            return;
        }

        // Retreat when low health
        if (getPlayerHealth() <= RETREAT_HEALTH) {
            aiMoveDirection = oppRight ? -1 : 1;
            if (dist <= SHOOT_RANGE && shootTimer == 0 && currentShootCooldown <= 0) aiShoot();
            return;
        }

        // Back off if overlapping
        if (dist < TOO_CLOSE_DISTANCE) {
            aiMoveDirection = oppRight ? -1 : 1;
            if (dist <= SHOOT_RANGE && shootTimer == 0 && currentShootCooldown <= 0) aiShoot();
            return;
        }

        float verticalDiff = myY - oppY; // positive means opponent is on a higher platform (smaller Y)

        // --- Platform-aware navigation via A* ---
        boolean navigated = false;
        if (navigationSystem != null) {
            NavigationNode nextNode = navigationSystem.findPathToTarget(myX, myY, oppX, oppY);
            if (nextNode != null) {
                float nodeX = nextNode.getCenterX();
                float nodeY = nextNode.getY();
                float distToNodeX = Math.abs(nodeX - myX);

                // If the next node is on a higher platform, we must reach its X
                // position first so the jump arc actually lands on it.
                boolean nodeIsAbove = nodeY < myY - 40;

                if (nodeIsAbove) {
                    // Walk toward the jump-off X position
                    aiMoveDirection = (nodeX > myX) ? 1 : -1;
                    // Jump once we are close enough horizontally to arc onto the platform
                    aiWantsToJump = distToNodeX < 80;
                } else {
                    // Same level or stepping down — use the opponent's actual position
                    // for close-range pursuit, and the node for longer-range guidance.
                    float targetX = (dist < 150) ? oppX : nodeX;
                    aiMoveDirection = (targetX > myX) ? 1 : -1;

                    // Jump over solid walls that block horizontal movement
                    if (checkForObstacle(aiMoveDirection > 0)) {
                        aiWantsToJump = true;
                    }
                }
                navigated = true;
            }
        }

        // Fallback when no nav path is available: direct pursuit
        if (!navigated) {
            aiMoveDirection = oppRight ? 1 : -1;
            if (verticalDiff > JUMP_UP_THRESHOLD) {
                aiWantsToJump = true;
            } else if (checkForObstacle(aiMoveDirection > 0) || checkForEdge(aiMoveDirection > 0)) {
                aiWantsToJump = true;
            }
        }

        if (dist <= SHOOT_RANGE && shootTimer == 0 && currentShootCooldown <= 0) {
            aiShoot();
        }
    }

    @Override
    protected void handlePlayerState() {
        switch (playerState) {
            case STANDING:
                aiPlayerStanding();
                break;
            case WALKING:
                aiPlayerWalking();
                break;
            case CROUCHING:
                aiPlayerStanding();
                break;
            case JUMPING:
                aiPlayerJumping();
                break;
        }
    }

    private void aiPlayerStanding() {
        // Use the facing direction already set by updateAIDecisions() based on opponent position
        // facingDirection is already set to face the opponent (LEFT or RIGHT)
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "STAND_RIGHT";
        } else {
            currentAnimationName = "STAND_LEFT";
        }
        
        if (aiWantsToJump && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.JUMPING;
        } else if (aiMoveDirection != 0) {
            playerState = PlayerState.WALKING;
        }
    }

    private void aiPlayerWalking() {
        moveAmountX = aiSmoothSpeedX;

        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else {
            currentAnimationName = "WALK_LEFT";
        }

        if (aiWantsToJump && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.JUMPING;
        } else if (aiMoveDirection == 0 && aiSmoothSpeedX == 0f && airGroundState == AirGroundState.GROUND) {
            // Only switch to STANDING once the deceleration has fully finished
            playerState = PlayerState.STANDING;
        }
    }

    private void aiPlayerJumping() {
        if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND) {
            // Use the facing direction already set by updateAIDecisions() based on opponent position
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            airGroundState = AirGroundState.AIR;
            jumpForce = jumpHeight;
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) jumpForce = 0;
            }
        } else if (airGroundState == AirGroundState.AIR) {
            // Continue using the facing direction to face the opponent while jumping
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) jumpForce = 0;
            }
            moveAmountX += aiSmoothSpeedX;
            if (moveAmountY > 0) {
                increaseMomentum();
            }
        } else if (previousAirGroundState == AirGroundState.AIR && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.STANDING;
        }
    }

    private boolean checkForObstacle(boolean movingRight) {
        if (map == null || airGroundState != AirGroundState.GROUND) return false;
        float checkX = movingRight ? getBounds().getX2() + 20 : getBounds().getX1() - 20;
        float checkY = getBounds().getY1() + getBounds().getHeight() / 2f;
        MapTile frontTile = map.getTileByPosition(checkX, checkY);
        if (frontTile != null && frontTile.getTileType() == TileType.NOT_PASSABLE) return true;
        MapTile frontTileHigh = map.getTileByPosition(checkX, getBounds().getY1() - 10);
        return frontTileHigh != null && frontTileHigh.getTileType() == TileType.NOT_PASSABLE;
    }

    private boolean checkForEdge(boolean movingRight) {
        if (map == null || airGroundState != AirGroundState.GROUND) return false;
        int tileHeight = map.getTileset() != null ? map.getTileset().getScaledSpriteHeight() : 48;
        float checkX = movingRight ? getBounds().getX2() + tileHeight : getBounds().getX1() - tileHeight;
        float checkY = getBounds().getY2() + tileHeight;
        MapTile belowTile = map.getTileByPosition(checkX, checkY);
        return belowTile == null || belowTile.getTileType() == TileType.PASSABLE;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return opponent;
    }

    @Override
    public void aiShoot() {
        if (shootTimer == 0 && currentShootCooldown <= 0) {
            catState = CatState.SHOOT;
            shootAnimationTimer = 20;
            currentShootCooldown = shootCooldownFrames;
        }
    }
    
    @Override
    protected void handlePlayerAnimation() {
        if (shootAnimationTimer > 0) {
            currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
        } else if (playerState == PlayerState.STANDING) {
            currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
        } else if (playerState == PlayerState.WALKING) {
            currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
        } else if (playerState == PlayerState.CROUCHING) {
            currentAnimationName = facingDirection == Direction.RIGHT ? "CROUCH_RIGHT" : "CROUCH_LEFT";
        } else if (playerState == PlayerState.JUMPING) {
            if (lastAmountMovedY <= 0) {
                currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            } else {
                currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
            }
        }
    }
}
