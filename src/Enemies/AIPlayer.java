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
import Utils.Direction;
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

    // Behavior parameters
    private int SHOOT_RANGE;
    private int RETREAT_HEALTH;
    private int DECISION_INTERVAL;
    private int JUMP_UP_THRESHOLD;
    private float HESITATION_CHANCE;
    private int PREFERRED_DISTANCE;   // Stay this far from opponent (avoid overlap)
    private int TOO_CLOSE_DISTANCE;   // When closer, back off immediately
    
    // Stuck detection
    private int stuckFrames;
    private static final int STUCK_THRESHOLD = 10;

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
    }

    private void setDifficultyParameters() {
        switch (difficulty) {
            case REGULAR:
                SHOOT_RANGE = 100;
                RETREAT_HEALTH = 30;
                DECISION_INTERVAL = 8;
                JUMP_UP_THRESHOLD = 90;
                HESITATION_CHANCE = 0.20f;
                PREFERRED_DISTANCE = 85;
                TOO_CLOSE_DISTANCE = 45;
                break;
            case HARD:
                SHOOT_RANGE = 150;
                RETREAT_HEALTH = 20;
                DECISION_INTERVAL = 4;
                JUMP_UP_THRESHOLD = 60;
                HESITATION_CHANCE = 0.08f;
                PREFERRED_DISTANCE = 75;
                TOO_CLOSE_DISTANCE = 40;
                break;
            case IMPOSSIBLE:
                SHOOT_RANGE = 220;
                RETREAT_HEALTH = 10;
                DECISION_INTERVAL = 1;
                JUMP_UP_THRESHOLD = 40;
                HESITATION_CHANCE = 0.0f;
                PREFERRED_DISTANCE = 65;
                TOO_CLOSE_DISTANCE = 35;
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
        if (opponent != null) {
            updateAIDecisions();
        }
        super.update();
    }

    /**
     * Run AI decisions before physics. Uses pathfinding when available,
     * sets aiMoveDirection and aiWantsToJump for the physics step.
     * Avoids overlapping with opponent by maintaining preferred distance.
     */
    private void updateAIDecisions() {
        float myX = getBounds().getX1() + getBounds().getWidth() / 2f;
        float myY = getBounds().getY1() + getBounds().getHeight() / 2f;
        float oppX = opponent.getBounds().getX1() + opponent.getBounds().getWidth() / 2f;
        float oppY = opponent.getBounds().getY1() + opponent.getBounds().getHeight() / 2f;
        float dist = Math.abs(myX - oppX);
        boolean oppRight = oppX > myX;

        // Stuck detection every frame: haven't moved horizontally while trying to move
        if (Math.abs(lastAmountMovedX) < 0.5f && aiMoveDirection != 0) {
            stuckFrames++;
        } else {
            stuckFrames = 0;
        }

        decisionTimer++;
        if (decisionTimer < DECISION_INTERVAL) {
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

        // Stuck recovery: reverse direction and/or jump to get unstuck
        if (stuckFrames >= STUCK_THRESHOLD && airGroundState == AirGroundState.GROUND) {
            int prevDir = (aiMoveDirection != 0) ? aiMoveDirection : (oppRight ? 1 : -1);
            aiMoveDirection = -Integer.signum(prevDir);
            aiWantsToJump = random.nextFloat() < 0.5f;
            stuckFrames = 0;
            facingDirection = aiMoveDirection > 0 ? Direction.RIGHT : Direction.LEFT;
            if (dist <= SHOOT_RANGE && shootTimer == 0) aiShoot();
            return;
        }

        if (getPlayerHealth() <= RETREAT_HEALTH) {
            aiMoveDirection = oppRight ? -1 : 1;
            facingDirection = oppRight ? Direction.LEFT : Direction.RIGHT;
            if (dist <= SHOOT_RANGE && shootTimer == 0) aiShoot();
            return;
        }

        facingDirection = oppRight ? Direction.RIGHT : Direction.LEFT;

        // Overlap prevention: when too close, back off instead of chasing
        if (dist < TOO_CLOSE_DISTANCE) {
            aiMoveDirection = oppRight ? -1 : 1;
            facingDirection = oppRight ? Direction.LEFT : Direction.RIGHT;
            if (dist <= SHOOT_RANGE && shootTimer == 0) aiShoot();
            return;
        }

        // At preferred distance: stand off, sometimes strafe for variety (don't overlap)
        if (dist >= PREFERRED_DISTANCE - 15 && dist <= PREFERRED_DISTANCE + 40) {
            if (dist <= SHOOT_RANGE && shootTimer == 0) {
                aiShoot();
            }
            if (random.nextFloat() < 0.35f) {
                aiMoveDirection = 0;
                return;
            }
            if (random.nextFloat() < 0.25f) {
                aiMoveDirection = random.nextBoolean() ? 1 : -1;
                return;
            }
        }

        // Target position offset from opponent - aim for preferred distance, not exact overlap
        float targetX = oppX + (oppRight ? -PREFERRED_DISTANCE : PREFERRED_DISTANCE);
        if (Math.abs(targetX - oppX) > 400) targetX = oppX;

        // Deadzone: already at good position, don't jitter
        if (Math.abs(myX - targetX) < 25 && dist >= PREFERRED_DISTANCE - 20) {
            aiMoveDirection = 0;
            if (dist <= SHOOT_RANGE && shootTimer == 0) aiShoot();
            return;
        }

        if (navigationSystem != null && navigationSystem.getNodes().size() > 0) {
            NavigationNode pathNode = navigationSystem.findPathToTarget(myX, myY, targetX, oppY);
            if (pathNode != null) {
                float nodeX = pathNode.getCenterX();
                float nodeY = pathNode.getY();
                if (myX < nodeX - 20) {
                    aiMoveDirection = 1;
                } else if (myX > nodeX + 20) {
                    aiMoveDirection = -1;
                } else {
                    aiMoveDirection = (targetX > myX) ? 1 : -1;
                }
                if (airGroundState == AirGroundState.GROUND && nodeY < myY - 20) {
                    aiWantsToJump = true;
                }
            } else {
                aiMoveDirection = (targetX > myX) ? 1 : -1;
                if (airGroundState == AirGroundState.GROUND && myY - oppY > JUMP_UP_THRESHOLD) {
                    aiWantsToJump = true;
                }
            }
        } else {
            aiMoveDirection = (targetX > myX) ? 1 : -1;
            if (airGroundState == AirGroundState.GROUND) {
                if (checkForObstacle(aiMoveDirection > 0) || checkForEdge(aiMoveDirection > 0)) {
                    aiWantsToJump = true;
                } else if (myY - oppY > JUMP_UP_THRESHOLD) {
                    aiWantsToJump = true;
                }
            }
        }

        if (dist <= SHOOT_RANGE && shootTimer == 0) {
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
        if (aiWantsToJump && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.JUMPING;
        } else if (aiMoveDirection != 0) {
            playerState = PlayerState.WALKING;
        }
    }

    private void aiPlayerWalking() {
        moveAmountX = aiMoveDirection * walkSpeed;
        if (aiMoveDirection < 0) {
            facingDirection = Direction.LEFT;
        } else if (aiMoveDirection > 0) {
            facingDirection = Direction.RIGHT;
        }
        if (aiWantsToJump && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.JUMPING;
        } else if (aiMoveDirection == 0 && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.STANDING;
        }
    }

    private void aiPlayerJumping() {
        if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND) {
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            airGroundState = AirGroundState.AIR;
            jumpForce = jumpHeight;
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) jumpForce = 0;
            }
        } else if (airGroundState == AirGroundState.AIR) {
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) jumpForce = 0;
            }
            moveAmountX += aiMoveDirection * walkSpeed;
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
}
