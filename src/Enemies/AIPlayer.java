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
 * to fight against a human opponent
 */
public class AIPlayer extends Cat {
    
    // Difficulty levels
    public enum Difficulty {
        REGULAR,
        HARD,
        IMPOSSIBLE
    }
    
    private Player opponent;  // Reference to the human player
    private Random random;
    private int decisionTimer;
    private int currentAction;
    private Difficulty difficulty;
    
    // AI Behavior parameters - will be set based on difficulty
    private int AGGRO_RANGE;
    private int SHOOT_RANGE;
    private int RETREAT_HEALTH;
    private int DECISION_INTERVAL;
    private float RANDOM_JUMP_CHANCE;
    private float HESITATION_CHANCE;
    private float RANDOM_ACTION_CHANCE;
    
    // AI Actions
    private static final int ACTION_NONE = 0;
    private static final int ACTION_MOVE_LEFT = 1;
    private static final int ACTION_MOVE_RIGHT = 2;
    private static final int ACTION_JUMP = 3;
    private static final int ACTION_SHOOT = 4;
    private static final int ACTION_JUMP_SHOOT = 5;
    
    public AIPlayer(float x, float y, int playerNumber, String character, Player opponent) {
        this(x, y, playerNumber, character, opponent, Difficulty.REGULAR);
    }
    
    public AIPlayer(float x, float y, int playerNumber, String character, Player opponent, Difficulty difficulty) {
        super(x, y, playerNumber, character);
        this.opponent = opponent;
        this.random = new Random();
        this.decisionTimer = 0;
        this.currentAction = ACTION_NONE;
        this.difficulty = difficulty;
        
        // Set behavior parameters based on difficulty
        setDifficultyParameters();
    }
    
    /**
     * Set AI behavior parameters based on selected difficulty
     */
    private void setDifficultyParameters() {
        switch (difficulty) {
            case REGULAR:
                AGGRO_RANGE = 2000; // Always chase - huge range
                SHOOT_RANGE = 100;   // Reduced - AI must get closer to shoot
                RETREAT_HEALTH = 30;
                DECISION_INTERVAL = 6;
                RANDOM_JUMP_CHANCE = 0.5f; // Much higher - frequent platform jumping
                HESITATION_CHANCE = 0.0f; // No hesitation
                RANDOM_ACTION_CHANCE = 0.0f;
                break;
            case HARD:
                AGGRO_RANGE = 2000; // Always chase
                SHOOT_RANGE = 150;   // Slightly further for hard
                RETREAT_HEALTH = 25;
                DECISION_INTERVAL = 4;
                RANDOM_JUMP_CHANCE = 0.8f; // Even higher
                HESITATION_CHANCE = 0.0f;
                RANDOM_ACTION_CHANCE = 0.0f;
                break;
            case IMPOSSIBLE:
                AGGRO_RANGE = 2000; // Always chase - no limit
                SHOOT_RANGE = 200;   // Further for impossible
                RETREAT_HEALTH = 10; // Only retreat when almost dead
                DECISION_INTERVAL = 3;
                RANDOM_JUMP_CHANCE = 1f; // Highest - constantly exploring platforms
                HESITATION_CHANCE = 0.0f;
                RANDOM_ACTION_CHANCE = 0.0f;
                break;
        }
    }
    
    /**
     * Get current difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * Update AI behavior - called each frame
     */
    @Override
    public void update() {
        // Call parent update for physics and animations
        super.update();
        
        // Make AI decisions and apply movement EVERY FRAME
        if (opponent != null) {
            // Get opponent's CURRENT position (updated every frame)
            float myX = this.getX();
            float myY = this.getY();
            float opponentX = opponent.getX();
            float opponentY = opponent.getY();
            
            // Calculate distance
            float distance = Math.abs(myX - opponentX);
            
            // Determine direction to opponent
            boolean opponentIsRight = opponentX > myX;
            boolean opponentIsAbove = opponentY < myY - 20;
            boolean opponentIsBelow = opponentY > myY + 20;
            
            // ALWAYS jump when on ground to use platforms
            if (airGroundState == AirGroundState.GROUND) {
                // 90% chance to jump - prioritize platform jumping
                if (Math.random() < 0.9) {
                    playerState = PlayerState.JUMPING;
                    jumpForce = jumpHeight;
                }
            }
            
            // Also move toward opponent
            if (opponentIsRight) {
                this.moveX(walkSpeed);
            } else {
                this.moveX(-walkSpeed);
            }
            
            // Shoot at opponent if in range
            if (distance <= SHOOT_RANGE && shootTimer == 0) {
                if (opponentIsRight) {
                    facingDirection = Direction.RIGHT;
                } else {
                    facingDirection = Direction.LEFT;
                }
                catState = CatState.SHOOT;
            }
        }
    }
    
    /**
     * Make AI decision based on game state
     */
    private void makeAIDecision() {
        // Make decisions every frame for consistent movement
        // The AI keeps its current action until changed
        
        // Get opponent position info - UPDATED EVERY FRAME
        float myX = this.getX();
        float myY = this.getY();
        float opponentX = opponent.getX();
        float opponentY = opponent.getY();
        
        // Calculate distance and direction
        float distance = Math.abs(myX - opponentX);
        float verticalDistance = myY - opponentY;
        int myHealth = this.getPlayerHealth();
        
        // Determine if opponent is to our right or left
        boolean opponentIsRight = opponentX > myX;
        boolean opponentIsAbove = opponentY < myY - 20;
        boolean opponentIsBelow = opponentY > myY + 50; // Opponent is significantly below
        
        // Check if opponent is shooting (for dodging)
        boolean opponentIsShooting = false;
        try {
            // Check if opponent is in shoot state
            if (opponent instanceof Cat) {
                Cat opponentCat = (Cat) opponent;
                // Would need to check catState but it's private, so we use distance heuristic
                // If very close, opponent might be about to shoot
            }
        } catch (Exception e) {
            // Ignore
        }
        
        // Decision tree based on health and distance
        // AI ALWAYS knows where opponent is and goes to attack them
        if (myHealth <= RETREAT_HEALTH && distance < AGGRO_RANGE) {
            // Low health - try to retreat
            currentAction = opponentIsRight ? ACTION_MOVE_LEFT : ACTION_MOVE_RIGHT;
            
            // If opponent is above, jump to escape
            if (opponentIsAbove && airGroundState == AirGroundState.GROUND) {
                currentAction = ACTION_JUMP;
            }
        } else {
            // Chase and attack the opponent - ALWAYS go toward opponent
            // If in shooting range, shoot!
            if (distance <= SHOOT_RANGE) {
                // In shooting range - ALWAYS attack when in range!
                if (opponentIsAbove) {
                    // Opponent above - jump and shoot
                    currentAction = ACTION_JUMP_SHOOT;
                } else {
                    // Opponent at same level - shoot
                    currentAction = ACTION_SHOOT;
                }
            } else {
                // Not in shooting range - ALWAYS chase the opponent
                currentAction = opponentIsRight ? ACTION_MOVE_RIGHT : ACTION_MOVE_LEFT;
                
                // Only add jump occasionally - but keep moving!
                // Don't overwrite movement with jump, just add jump to the movement
            }
        }
        
        // Fallback: if somehow no action was set, chase the opponent
        if (currentAction == ACTION_NONE) {
            currentAction = opponentIsRight ? ACTION_MOVE_RIGHT : ACTION_MOVE_LEFT;
        }
    }
    
    /**
     * Override handlePlayerState to use AI movement instead of keyboard
     */
    @Override
    protected void handlePlayerState() {
        // For AI player, we handle movement in executeAIAction(), not keyboard
        // But we need to handle state transitions based on our AI actions
        
        // If AI set a jump action, enter JUMPING state
        if (currentAction == ACTION_JUMP || currentAction == ACTION_JUMP_SHOOT) {
            if (airGroundState == AirGroundState.GROUND && playerState != PlayerState.JUMPING) {
                playerState = PlayerState.JUMPING;
            }
        }
        
        // If AI is moving, enter WALKING state
        if (currentAction == ACTION_MOVE_LEFT || currentAction == ACTION_MOVE_RIGHT) {
            playerState = PlayerState.WALKING;
        }
        
        // If no movement action and on ground, enter STANDING
        if (currentAction == ACTION_NONE || currentAction == ACTION_SHOOT) {
            if (airGroundState == AirGroundState.GROUND) {
                playerState = PlayerState.STANDING;
            }
        }
    }
    
    /**
     * Execute the current AI action
     */
    private void executeAIAction() {
        if (opponent == null) return;
        
        // Continuously track opponent position for better aiming
        float myX = this.getX();
        float myY = this.getY();
        float opponentX = opponent.getX();
        float opponentY = opponent.getY();
        
        float distance = Math.abs(myX - opponentX);
        float verticalDistance = myY - opponentY;
        boolean opponentIsRight = opponentX > myX;
        
        // Platform detection - check for obstacles
        boolean shouldJump = checkForObstacle(opponentIsRight);
        
        // If opponent is on higher ground, try to jump up to them
        // More aggressive jumping to reach opponent on platforms
        if (verticalDistance > 20 && airGroundState == AirGroundState.GROUND) {
            // Opponent is above us, ALWAYS jump to reach them
            shouldJump = true;
        }
        
        // If there's a gap/edge ahead, jump to cross it
        if (airGroundState == AirGroundState.GROUND) {
            boolean edgeAhead = checkForEdge(opponentIsRight);
            if (edgeAhead) {
                shouldJump = true;
            }
        }
        
        // Always face the opponent
        if (opponentIsRight) {
            facingDirection = Direction.RIGHT;
        } else {
            facingDirection = Direction.LEFT;
        }
        
        // Execute action based on current decision
        switch (currentAction) {
            case ACTION_MOVE_LEFT:
                this.aiMoveLeft();
                if (shouldJump) this.aiJump();
                break;
                
            case ACTION_MOVE_RIGHT:
                this.aiMoveRight();
                if (shouldJump) this.aiJump();
                break;
                
            case ACTION_JUMP:
                this.aiJump();
                break;
                
            case ACTION_SHOOT:
                // Face opponent and shoot when in range
                if (distance <= SHOOT_RANGE && shootTimer == 0) {
                    this.aiShoot();
                }
                break;
                
            case ACTION_JUMP_SHOOT:
                // Jump and shoot simultaneously
                if (airGroundState == AirGroundState.GROUND) {
                    this.aiJump();
                }
                if (distance <= SHOOT_RANGE && shootTimer == 0) {
                    this.aiShoot();
                }
                break;
                
            case ACTION_NONE:
            default:
                // Stand still, but still face opponent and shoot if in range
                if (distance <= SHOOT_RANGE && shootTimer == 0) {
                    this.aiShoot();
                }
                // Random jump for platform exploration
                if (random.nextFloat() < RANDOM_JUMP_CHANCE && airGroundState == AirGroundState.GROUND) {
                    this.aiJump();
                }
                break;
        }
        
        // Random jump for unpredictability - chance based on difficulty
        if (random.nextFloat() < RANDOM_JUMP_CHANCE && airGroundState == AirGroundState.GROUND) {
            this.aiJump();
        }
    }
    
    /**
     * Check if there's an obstacle in the direction we're moving
     * Returns true if AI should jump to get over it
     */
    private boolean checkForObstacle(boolean movingRight) {
        if (map == null || airGroundState != AirGroundState.GROUND) {
            return false;
        }
        
        // Check tiles in front of and above the AI
        float checkX = movingRight ? this.getX() + 30 : this.getX() - 30;
        float checkY = this.getY();
        
        // Get tile in front of us
        MapTile frontTile = map.getTileByPosition(checkX, checkY);
        
        // Check if there's a solid tile in front
        if (frontTile != null && frontTile.getTileType() != TileType.PASSABLE) {
            // There's an obstacle, we should jump!
            return true;
        }
        
        // Check a bit higher as well (for taller obstacles)
        MapTile frontTileHigh = map.getTileByPosition(checkX, checkY - 20);
        if (frontTileHigh != null && frontTileHigh.getTileType() != TileType.PASSABLE) {
            return true;
        }
        
        // Random platform jumping based on difficulty
        // If we're on ground and random chance, jump to explore higher platforms
        if (random.nextFloat() < RANDOM_JUMP_CHANCE) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if there's a gap/edge ahead that needs to be jumped
     */
    private boolean checkForEdge(boolean movingRight) {
        if (map == null || airGroundState != AirGroundState.GROUND) {
            return false;
        }
        
        // Check tiles in front of and below the AI
        float checkX = movingRight ? this.getX() + 20 : this.getX() - 20;
        float checkY = this.getY() + 50; // Check below feet level
        
        // Get tile below where we're about to step
        MapTile belowTile = map.getTileByPosition(checkX, checkY);
        
        // If there's no tile below (gap/edge), we should jump!
        if (belowTile == null || belowTile.getTileType() == TileType.PASSABLE) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Set the opponent for the AI to fight
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    
    /**
     * Get the current opponent
     */
    public Player getOpponent() {
        return opponent;
    }
}
