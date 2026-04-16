package Level;

import java.util.ArrayList;
import java.util.List;
import Level.NavigationNode.EdgeType;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.ImageEffect;

/**
 * Manages navigation nodes for AI pathfinding.
 * Generates platform nodes from the map's tile layout.
 */
public class NavigationSystem {
    
    private Map map;
    private ArrayList<NavigationNode> nodes;
    private ArrayList<ArrayList<NavigationNode>> platformNodes; // Nodes grouped by platform Y level
    
    // Tile size from the map's tileset
    private int tileWidth;
    private int tileHeight;
    
    public NavigationSystem(Map map) {
        this.map = map;
        this.nodes = new ArrayList<>();
        this.platformNodes = new ArrayList<>();
        
        if (map.getTileset() != null) {
            this.tileWidth = map.getTileset().getScaledSpriteWidth();
            this.tileHeight = map.getTileset().getScaledSpriteHeight();
        } else {
            this.tileWidth = 32;
            this.tileHeight = 32;
        }
        
        generateNodes();
    }
    
    /**
     * Generate navigation nodes from the map's tile layout
     */
    private void generateNodes() {
        nodes.clear();
        platformNodes.clear();
        
        if (map.getMapTiles() == null) {
            return;
        }
        
        MapTile[] mapTiles = map.getMapTiles();
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        
        boolean[][] nodeCreated = new boolean[mapWidth][mapHeight];
        
        for (int y = 0; y < mapHeight - 1; y++) {
            for (int x = 0; x < mapWidth; x++) {
                MapTile currentTile = map.getMapTile(x, y);
                MapTile tileAbove = map.getMapTile(x, y - 1);
                
                boolean isSolid = currentTile != null && 
                    (currentTile.getTileType() == TileType.NOT_PASSABLE ||
                     currentTile.getTileType() == TileType.JUMP_THROUGH_PLATFORM ||
                     currentTile.getTileType() == TileType.SLOPE);
                
                boolean aboveIsPassable = tileAbove == null || tileAbove.getTileType() == TileType.PASSABLE;
                
                if (isSolid && aboveIsPassable) {
                    if (!nodeCreated[x][y]) {
                        createNodeAt(x, y);
                        nodeCreated[x][y] = true;
                    }
                }
            }
        }
        
        identifyPlatformEdges(mapWidth, mapHeight);
        connectNodes();
        createJumpConnections();
    }
    
    /**
     * Identify and create nodes at the edges (ends) of platforms
     */
    private void identifyPlatformEdges(int mapWidth, int mapHeight) {
        // Group all surface tiles by their Y level (platform level)
        
        for (int y = 0; y < mapHeight - 1; y++) {
            // Find all X tiles at this Y level that form a continuous platform
            ArrayList<Integer> platformXs = new ArrayList<>();
            for (int x = 0; x < mapWidth; x++) {
                MapTile tile = map.getMapTile(x, y);
                MapTile above = map.getMapTile(x, y - 1);
                if (tile != null && tile.getTileType() != TileType.PASSABLE) {
                    if (above == null || above.getTileType() == TileType.PASSABLE) {
                        platformXs.add(x);
                    }
                }
            }
            
            // If we have a continuous platform segment, create edge nodes
            if (!platformXs.isEmpty()) {
                // Create left edge node (first tile in segment)
                int leftX = platformXs.get(0);
                float leftWorldX = leftX * tileWidth;
                float worldY = y * tileHeight;
                
                NavigationNode leftEdgeNode = new NavigationNode(leftWorldX, worldY, findOrCreatePlatform(worldY), tileWidth);
                leftEdgeNode.setIsEdge(true);
                leftEdgeNode.setEdgeType(EdgeType.LEFT_EDGE);
                nodes.add(leftEdgeNode);
                
                int platformId = findOrCreatePlatform(worldY);
                while (platformNodes.size() <= platformId) {
                    platformNodes.add(new ArrayList<>());
                }
                platformNodes.get(platformId).add(leftEdgeNode);
                
                // Create right edge node (last tile in segment)
                int rightX = platformXs.get(platformXs.size() - 1);
                float rightWorldX = rightX * tileWidth;
                
                NavigationNode rightEdgeNode = new NavigationNode(rightWorldX, worldY, findOrCreatePlatform(worldY), tileWidth);
                rightEdgeNode.setIsEdge(true);
                rightEdgeNode.setEdgeType(EdgeType.RIGHT_EDGE);
                nodes.add(rightEdgeNode);
                
                while (platformNodes.size() <= platformId) {
                    platformNodes.add(new ArrayList<>());
                }
                platformNodes.get(platformId).add(rightEdgeNode);
            }
        }
    }
    
    /**
     * Create a navigation node at the specified tile position
     */
    private void createNodeAt(int tileX, int tileY) {
        float worldX = tileX * tileWidth;
        float worldY = tileY * tileHeight;
        
        // Group nodes by approximate Y level (platform)
        int platformId = findOrCreatePlatform(worldY);
        
        NavigationNode node = new NavigationNode(worldX, worldY, platformId, tileWidth);
        nodes.add(node);
        
        // Add to platform group
        while (platformNodes.size() <= platformId) {
            platformNodes.add(new ArrayList<>());
        }
        platformNodes.get(platformId).add(node);
    }
    
    /**
     * Find or create a platform ID for the given Y level
     */
    private int findOrCreatePlatform(float y) {
        // Nodes within 20 pixels are considered on the same platform
        float tolerance = 20;
        
        for (int i = 0; i < platformNodes.size(); i++) {
            if (!platformNodes.get(i).isEmpty()) {
                float platformY = platformNodes.get(i).get(0).getY();
                if (Math.abs(platformY - y) < tolerance) {
                    return i;
                }
            }
        }
        
        // Create new platform
        return platformNodes.size();
    }
    
    /**
     * Connect adjacent nodes on the same platform
     */
    private void connectNodes() {
        for (ArrayList<NavigationNode> platform : platformNodes) {
            // Sort nodes by X position
            platform.sort((a, b) -> Float.compare(a.getX(), b.getX()));
            
            // Connect adjacent nodes
            for (int i = 0; i < platform.size() - 1; i++) {
                NavigationNode current = platform.get(i);
                NavigationNode next = platform.get(i + 1);
                
                // Only connect if they're close enough (within a few tiles)
                if (next.getX() - current.getX() < tileWidth * 4) {
                    current.setRightNode(next);
                    next.setLeftNode(current);
                }
            }
        }
    }
    
    /**
     * Create jump connections between different platforms
     */
    private void createJumpConnections() {
        if (nodes == null || nodes.isEmpty()) return;
        
        int nodeCount = nodes.size();
        int maxIterations = Math.min(nodeCount, 100);
        
        for (int i = 0; i < maxIterations; i++) {
            NavigationNode node = nodes.get(i);
            if (node == null) continue;
            
            NavigationNode bestJumpTarget = null;
            float bestScore = Float.MAX_VALUE;
            
            for (int j = 0; j < maxIterations; j++) {
                if (i == j) continue;
                NavigationNode other = nodes.get(j);
                if (other == null) continue;
                if (node.isSameLevel(other)) continue;
                
                float horizontalDist = node.distanceTo(other);
                float verticalDist = node.verticalDistanceTo(other);
                
                if (verticalDist > 0 && verticalDist < 150 && horizontalDist < 200) {
                    float score = horizontalDist + verticalDist * 2;
                    if (score < bestScore) {
                        bestScore = score;
                        bestJumpTarget = other;
                    }
                }
            }
            
            node.setJumpTarget(bestJumpTarget);
        }
    }
    
    /**
     * Get the navigation nodes
     */
    public ArrayList<NavigationNode> getNodes() {
        return nodes;
    }
    
    /**
     * Get only the edge nodes (platform endpoints)
     */
    public ArrayList<NavigationNode> getEdgeNodes() {
        ArrayList<NavigationNode> edgeNodes = new ArrayList<>();
        for (NavigationNode node : nodes) {
            if (node.isEdge()) {
                edgeNodes.add(node);
            }
        }
        return edgeNodes;
    }
    
    /**
     * Get the left edge nodes (nodes on the left side of platforms)
     */
    public ArrayList<NavigationNode> getLeftEdgeNodes() {
        ArrayList<NavigationNode> leftEdges = new ArrayList<>();
        for (NavigationNode node : nodes) {
            if (node.isEdge() && node.getEdgeType() == EdgeType.LEFT_EDGE) {
                leftEdges.add(node);
            }
        }
        return leftEdges;
    }
    
    /**
     * Get the right edge nodes (nodes on the right side of platforms)
     */
    public ArrayList<NavigationNode> getRightEdgeNodes() {
        ArrayList<NavigationNode> rightEdges = new ArrayList<>();
        for (NavigationNode node : nodes) {
            if (node.isEdge() && node.getEdgeType() == EdgeType.RIGHT_EDGE) {
                rightEdges.add(node);
            }
        }
        return rightEdges;
    }
    
    /**
     * Find the best edge node to jump from to reach the target.
     * Considers current position and direction to target.
     */
    public NavigationNode findBestJumpEdge(float currentX, float currentY, float targetX, float targetY) {
        boolean movingRight = targetX > currentX;
        
        // Get appropriate edge nodes based on direction
        ArrayList<NavigationNode> candidateEdges = movingRight ? getRightEdgeNodes() : getLeftEdgeNodes();
        
        NavigationNode bestEdge = null;
        float bestScore = Float.MAX_VALUE;
        
        for (NavigationNode edge : candidateEdges) {
            // Skip edges that are lower than current position
            if (edge.getY() >= currentY) continue;
            
            // Check horizontal distance from current position to edge
            float distToEdge = Math.abs(edge.getCenterX() - currentX);
            
            // Check if this edge can reach the target
            float horizontalDist = Math.abs(edge.getCenterX() - targetX);
            float verticalDist = Math.abs(edge.getY() - targetY);
            
            // Valid jump if within range
            if (distToEdge < 300 && horizontalDist < 250 && verticalDist < 150) {
                // Score based on proximity to both current position and target
                float score = distToEdge + horizontalDist * 0.5f + verticalDist;
                if (score < bestScore) {
                    bestScore = score;
                    bestEdge = edge;
                }
            }
        }
        
        return bestEdge;
    }
    
    /**
     * Find the nearest platform node to the target position.
     * Used when AI is in the air and needs to land on a platform near the player.
     */
    public NavigationNode findNearestPlatformToTarget(float currentX, float currentY, float targetX, float targetY) {
        NavigationNode bestNode = null;
        float bestScore = Float.MAX_VALUE;
        
        // Look for nodes that are below or at the current Y level (platforms we can land on)
        for (NavigationNode node : nodes) {
            // Skip edge nodes - prefer landing on platform surfaces
            if (node.isEdge()) continue;
            
            // We want platforms that are:
            // 1. Below or at current height (we can land on them)
            // 2. Close to the target (player's area)
            float verticalDist = Math.abs(node.getY() - currentY);
            float horizontalDistToTarget = Math.abs(node.getCenterX() - targetX);
            
            // Prefer platforms close to the player's X position
            if (horizontalDistToTarget < 300 && verticalDist < 200) {
                float score = horizontalDistToTarget + verticalDist * 0.5f;
                if (score < bestScore) {
                    bestScore = score;
                    bestNode = node;
                }
            }
        }
        
        return bestNode;
    }
    
    /**
     * Find the closest node to a given position
     */
    public NavigationNode getClosestNode(float x, float y) {
        if (nodes == null || nodes.isEmpty()) return null;
        
        NavigationNode closest = null;
        float closestDist = Float.MAX_VALUE;
        
        for (NavigationNode node : nodes) {
            if (node == null) continue;
            float nodeX = node.getX();
            float nodeY = node.getY();
            if (Float.isNaN(nodeX) || Float.isInfinite(nodeX)) continue;
            float dist = (float) Math.sqrt(Math.pow(nodeX - x, 2) + Math.pow(nodeY - y, 2));
            if (dist < closestDist) {
                closestDist = dist;
                closest = node;
            }
        }
        
        return closest;
    }
    
    /**
     * Find the closest node to a given position, preferring ground nodes
     */
    public NavigationNode getClosestGroundNode(float x, float y) {
        if (nodes == null || nodes.isEmpty()) return null;
        
        NavigationNode closest = null;
        float closestDist = Float.MAX_VALUE;
        
        for (NavigationNode node : nodes) {
            if (node == null) continue;
            float nodeX = node.getX();
            float nodeY = node.getY();
            if (Float.isNaN(nodeX) || Float.isInfinite(nodeX)) continue;
            
            float dist = (float) Math.sqrt(Math.pow(nodeX - x, 2) + Math.pow(nodeY - y, 2));
            
            if (!node.isEdge()) {
                dist *= 0.5f;
            }
            
            if (dist < closestDist) {
                closestDist = dist;
                closest = node;
            }
        }
        
        return closest;
    }
    
    /**
     * Find the closest node to the target position (player location)
     */
    public NavigationNode getClosestNodeToTarget(float targetX, float targetY) {
        return getClosestNode(targetX, targetY);
    }
    
    /**
     * Find path from current position to target position using A* pathfinding.
     * Returns the next node to move towards.
     */
    public NavigationNode findPathToTarget(float currentX, float currentY, float targetX, float targetY) {
        // Use A* pathfinding for shortest path
        return findShortestPath(currentX, currentY, targetX, targetY);
    }
    
    /**
     * A* pathfinding to find the shortest path through navigation nodes.
     */
    private NavigationNode findShortestPath(float currentX, float currentY, float targetX, float targetY) {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        
        NavigationNode startNode = getClosestGroundNode(currentX, currentY);
        NavigationNode goalNode = getClosestNodeToTarget(targetX, targetY);
        
        if (startNode == null || goalNode == null) {
            return null;
        }
        
        if (startNode == goalNode) {
            return null;
        }
        
        java.util.PriorityQueue<AStarNode> openSet = new java.util.PriorityQueue<>(
            (a, b) -> Float.compare(a.fScore, b.fScore));
        java.util.HashSet<NavigationNode> closedSet = new java.util.HashSet<>();
        
        java.util.HashMap<NavigationNode, Float> gScore = new java.util.HashMap<>();
        gScore.put(startNode, 0f);
        
        float hStart = heuristic(startNode, goalNode);
        openSet.add(new AStarNode(startNode, 0f, hStart, null));
        
        int iterations = 0;
        int maxIterations = 500;
        
        while (!openSet.isEmpty() && iterations < maxIterations) {
            iterations++;
            AStarNode current = openSet.poll();
            NavigationNode currentNode = current.node;
            
            if (currentNode == goalNode) {
                return reconstructPath(current);
            }
            
            closedSet.add(currentNode);
            
            java.util.ArrayList<NavigationNode> neighbors = new java.util.ArrayList<>();
            if (currentNode.getLeftNode() != null) neighbors.add(currentNode.getLeftNode());
            if (currentNode.getRightNode() != null) neighbors.add(currentNode.getRightNode());
            if (currentNode.getJumpTarget() != null) neighbors.add(currentNode.getJumpTarget());
            
            for (NavigationNode neighbor : neighbors) {
                if (neighbor == null || closedSet.contains(neighbor)) continue;
                
                float moveCost = currentNode.distanceTo(neighbor);
                float tentativeGScore = gScore.get(currentNode) + moveCost;
                
                Float existingGScore = gScore.get(neighbor);
                if (existingGScore == null || tentativeGScore < existingGScore) {
                    gScore.put(neighbor, tentativeGScore);
                    float fScore = tentativeGScore + heuristic(neighbor, goalNode);
                    openSet.add(new AStarNode(neighbor, tentativeGScore, fScore, current));
                }
            }
        }
        
        return goalNode;
    }
    
    /**
     * Reconstruct the path and return the first node to move to
     */
    private NavigationNode reconstructPath(AStarNode goalNode) {
        java.util.ArrayList<NavigationNode> path = new java.util.ArrayList<>();
        AStarNode current = goalNode;
        
        int iterations = 0;
        int maxIterations = 100;
        while (current != null && iterations < maxIterations) {
            if (current.node == null) return null;
            path.add(0, current.node);
            current = current.parent;
            iterations++;
        }
        
        return path.size() > 1 ? path.get(1) : null;
    }
    
    /**
     * Heuristic function for A* (Euclidean distance)
     */
    private float heuristic(NavigationNode a, NavigationNode b) {
        return a.distanceTo(b);
    }
    
    /**
     * Helper class for A* priority queue
     */
    private static class AStarNode {
        NavigationNode node;
        float gScore;
        float fScore;
        AStarNode parent;
        
        AStarNode(NavigationNode node, float gScore, float fScore, AStarNode parent) {
            this.node = node;
            this.gScore = gScore;
            this.fScore = fScore;
            this.parent = parent;
        }
    }
    
    /**
     * Check if there's ground at the given position (for landing)
     */
    public boolean hasGroundAt(float x, float y) {
        return hasGroundBelow(x, y);
    }
    
    /**
     * Check if the AI is about to walk off a platform edge
     * Returns true if there's no ground ahead
     */
    public boolean isAboutToFallOff(float x, float y, boolean movingRight) {
        // Look ahead by a small amount
        float lookAheadX = movingRight ? x + 20 : x - 20;
        
        // Check if there's ground at current position (slightly below)
        boolean hasGroundBelowNow = hasGroundBelow(x, y + 5);
        
        // Check if there's ground ahead
        boolean hasGroundBelowAhead = hasGroundBelow(lookAheadX, y + 5);
        
        // We're about to fall off if we have ground now but not ahead
        return hasGroundBelowNow && !hasGroundBelowAhead;
    }
    
    /**
     * Find the best platform to land on when falling, that's also close to the target
     */
    public NavigationNode findBestLandingPlatform(float currentX, float currentY, float targetX, float targetY) {
        NavigationNode bestLanding = null;
        float bestScore = Float.MAX_VALUE;
        
        // Find a node that's below current position and close to target
        for (NavigationNode node : nodes) {
            // Must be below or at current level
            if (node.getY() < currentY - 10) continue;
            
            // Prefer nodes in the direction of the target
            boolean nodeTowardTarget = (targetX > currentX && node.getCenterX() > currentX) ||
                                        (targetX < currentX && node.getCenterX() < currentX);
            
            // Calculate score - prefer nodes closer to target horizontally
            float horizontalDist = Math.abs(node.getCenterX() - targetX);
            float verticalDist = Math.abs(node.getY() - currentY);
            
            // Prioritize horizontal alignment with target
            float score = horizontalDist + verticalDist * 2;
            
            if (score < bestScore && (nodeTowardTarget || horizontalDist < 200)) {
                bestScore = score;
                bestLanding = node;
            }
        }
        
        return bestLanding;
    }
    
    /**
     * Find a jump target to reach a higher platform
     */
    public NavigationNode findJumpUpTarget(float currentX, float currentY, float targetX, float targetY) {
        boolean movingRight = targetX > currentX;
        
        // Look for edge nodes that can be used to jump up to higher platforms
        ArrayList<NavigationNode> candidates = movingRight ? getRightEdgeNodes() : getLeftEdgeNodes();
        
        NavigationNode bestJump = null;
        float bestScore = Float.MAX_VALUE;
        
        for (NavigationNode edge : candidates) {
            // Must be higher than current (negative vertical distance means above)
            float verticalDist = edge.getY() - currentY;
            if (verticalDist > -30) continue; // Skip if not above or too high
            
            // Horizontal distance to the edge
            float horizontalDist = Math.abs(edge.getCenterX() - currentX);
            
            // Distance from edge to target (we need to land somewhere near target)
            float distToTarget = Math.abs(edge.getCenterX() - targetX);
            
            // Score: prefer edges that are close and lead toward target
            float score = horizontalDist + distToTarget * 0.5f;
            
            if (score < bestScore) {
                bestScore = score;
                bestJump = edge;
            }
        }
        
        return bestJump;
    }
    
    /**
     * Find the best node to move towards when on the same platform level as target
     */
    public NavigationNode findNextMoveNode(float currentX, float currentY, float targetX, float targetY) {
        boolean movingRight = targetX > currentX;
        
        NavigationNode bestNode = null;
        float bestScore = Float.MAX_VALUE;
        
        for (NavigationNode node : nodes) {
            // Skip nodes that are significantly different in height
            if (Math.abs(node.getY() - currentY) > 50) continue;
            
            // Check if this node is in the right direction
            boolean nodeIsRight = node.getCenterX() > currentX;
            if (nodeIsRight != movingRight) continue;
            
            // Calculate distance to target
            float distToTarget = Math.abs(node.getCenterX() - targetX);
            float distFromCurrent = Math.abs(node.getCenterX() - currentX);
            
            // Score: prefer nodes closer to target
            float score = distToTarget + distFromCurrent * 0.3f;
            
            if (score < bestScore) {
                bestScore = score;
                bestNode = node;
            }
        }
        
        return bestNode;
    }
    
    /**
     * Find the best node to jump to from current position to reach target position
     * Prefers edge nodes (nodes at platform ends) for better jumping
     */
    public NavigationNode findBestJumpNode(float currentX, float currentY, float targetX, float targetY) {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        
        NavigationNode currentNode = getClosestNode(currentX, currentY);
        if (currentNode == null) return null;
        
        if (targetY >= currentY) {
            return null;
        }
        
        NavigationNode bestEdgeNode = null;
        float bestEdgeScore = Float.MAX_VALUE;
        
        for (NavigationNode node : nodes) {
            if (node == null || !node.isEdge()) continue;
            if (node.getY() >= currentY) continue;
            
            float nodeCenterX = node.getCenterX();
            if (Float.isNaN(nodeCenterX) || Float.isInfinite(nodeCenterX)) continue;
            
            float horizontalDist = Math.abs(nodeCenterX - targetX);
            float verticalDist = Math.abs(node.getY() - targetY);
            
            if (horizontalDist < 250 && verticalDist < 150) {
                float score = horizontalDist + verticalDist * 0.5f;
                if (score < bestEdgeScore) {
                    bestEdgeScore = score;
                    bestEdgeNode = node;
                }
            }
        }
        
        // If we found a good edge node, use it
        if (bestEdgeNode != null) {
            return bestEdgeNode;
        }
        
        // Fallback: find any node that can reach the target area
        NavigationNode bestNode = null;
        float bestScore = Float.MAX_VALUE;
        
        for (NavigationNode node : nodes) {
            // Skip nodes that are lower than our current position
            if (node.getY() >= currentY) continue;
            
            // Check if this node can reach the target X position
            float horizontalDist = Math.abs(node.getCenterX() - targetX);
            
            // Check vertical - can we reach from this node to target?
            float verticalDist = Math.abs(node.getY() - targetY);
            
            if (horizontalDist < 200 && verticalDist < 150) {
                float score = horizontalDist + verticalDist;
                if (score < bestScore) {
                    bestScore = score;
                    bestNode = node;
                }
            }
        }
        
        return bestNode;
    }
    
    /**
     * Check if there's a platform above at the given position (for obstacle detection)
     */
    public boolean hasPlatformAbove(float x, float y, float height) {
        MapTile tile = map.getTileByPosition(x, y - height);
        return tile != null && tile.getTileType() != TileType.PASSABLE;
    }
    
    /**
     * Check if there's ground below at the given position
     */
    public boolean hasGroundBelow(float x, float y) {
        MapTile tile = map.getTileByPosition(x, y + 10);
        if (tile == null) return false;
        
        // Ground includes NOT_PASSABLE, JUMP_THROUGH_PLATFORM, and SLOPE
        TileType type = tile.getTileType();
        return type != TileType.PASSABLE && type != TileType.WATER;
    }
    
    /**
     * Regenerate nodes (call this when map changes)
     */
    public void regenerate() {
        generateNodes();
    }
    
    /**
     * Draw all navigation nodes for debugging
     */
    public void draw(GraphicsHandler graphicsHandler) {
        if (nodes == null) return;
        
        // Only draw edge nodes (jump points at platform edges)
        for (NavigationNode node : nodes) {
            if (node.isEdge()) {
                // Draw edge nodes in red (left edges) or blue (right edges)
                if (node.getEdgeType() == EdgeType.LEFT_EDGE) {
                    graphicsHandler.drawFilledRectangle(
                        (int) node.getX(),
                        (int) node.getY(),
                        (int) node.getWidth(),
                        10,
                        new java.awt.Color(255, 0, 0, 180) // Red for left edges
                    );
                } else if (node.getEdgeType() == EdgeType.RIGHT_EDGE) {
                    graphicsHandler.drawFilledRectangle(
                        (int) node.getX(),
                        (int) node.getY(),
                        (int) node.getWidth(),
                        10,
                        new java.awt.Color(0, 0, 255, 180) // Blue for right edges
                    );
                }
            }
        }
    }
}
