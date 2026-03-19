package Level;

import Utils.Point;

/**
 * Represents a navigation node on a platform that the AI can use for pathfinding.
 * Each node represents a walkable position on a platform.
 */
public class NavigationNode {
    
    // Edge types for platform jump targets
    public enum EdgeType {
        LEFT_EDGE,
        RIGHT_EDGE,
        MIDDLE,
        JUMP_TARGET
    }
    
    // Position of this node on the map
    private float x;
    private float y;
    
    // The platform this node belongs to (can be used to identify the platform level)
    private int platformId;
    
    // Connection to other nodes (for pathfinding)
    private NavigationNode leftNode;
    private NavigationNode rightNode;
    private NavigationNode jumpTarget; // Node reachable by jumping from this node
    
    // Width of the platform segment this node represents
    private float width;
    
    // Edge properties
    private boolean isEdge;
    private EdgeType edgeType;
    
    public NavigationNode(float x, float y, int platformId) {
        this.x = x;
        this.y = y;
        this.platformId = platformId;
        this.width = 32; // Default tile width
        this.isEdge = false;
        this.edgeType = EdgeType.MIDDLE;
    }
    
    public NavigationNode(float x, float y, int platformId, float width) {
        this.x = x;
        this.y = y;
        this.platformId = platformId;
        this.width = width;
        this.isEdge = false;
        this.edgeType = EdgeType.MIDDLE;
    }
    
    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getPlatformId() { return platformId; }
    public float getWidth() { return width; }
    public NavigationNode getLeftNode() { return leftNode; }
    public NavigationNode getRightNode() { return rightNode; }
    public NavigationNode getJumpTarget() { return jumpTarget; }
    public boolean isEdge() { return isEdge; }
    public EdgeType getEdgeType() { return edgeType; }
    
    // Setters
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setLeftNode(NavigationNode leftNode) { this.leftNode = leftNode; }
    public void setRightNode(NavigationNode rightNode) { this.rightNode = rightNode; }
    public void setJumpTarget(NavigationNode jumpTarget) { this.jumpTarget = jumpTarget; }
    public void setIsEdge(boolean isEdge) { this.isEdge = isEdge; }
    public void setEdgeType(EdgeType edgeType) { this.edgeType = edgeType; }
    
    /**
     * Get the center X position of this node
     */
    public float getCenterX() {
        return x + width / 2;
    }
    
    /**
     * Check if this node is at the same vertical level as another node
     */
    public boolean isSameLevel(NavigationNode other) {
        return Math.abs(this.y - other.y) < 10; // Within 10 pixels
    }
    
    /**
     * Check if this node is above another node (can jump down to it)
     */
    public boolean isAbove(NavigationNode other) {
        return this.y < other.y - 10;
    }
    
    /**
     * Check if this node is below another node (can jump up to it)
     */
    public boolean isBelow(NavigationNode other) {
        return this.y > other.y + 10;
    }
    
    /**
     * Calculate the horizontal distance to another node
     */
    public float distanceTo(NavigationNode other) {
        return Math.abs(this.x - other.x);
    }
    
    /**
     * Calculate the vertical distance to another node
     */
    public float verticalDistanceTo(NavigationNode other) {
        return Math.abs(this.y - other.y);
    }
    
    @Override
    public String toString() {
        return String.format("NavigationNode[x=%.1f, y=%.1f, platform=%d]", x, y, platformId);
    }
}
