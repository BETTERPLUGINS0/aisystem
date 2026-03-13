/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.betterstructures.modules.Direction;
import com.magmaguy.betterstructures.modules.ModulesContainer;
import com.magmaguy.betterstructures.modules.WFCGenerator;
import com.magmaguy.betterstructures.modules.WFCNode;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import org.bukkit.World;
import org.joml.Vector3i;

public class WFCLattice {
    private static final Map<Direction, Vector3i> DIRECTION_OFFSETS = new EnumMap<Direction, Vector3i>(Direction.class);
    private final int latticeRadius;
    private final int nodeSizeXZ;
    private final int nodeSizeY;
    private final int minYLevel;
    private final int maxYLevel;
    private final Map<Vector3i, WFCNode> nodeMap;
    private final PriorityQueue<WFCNode> entropyQueue;
    private final Deque<CollapseDecision> decisionStack = new ArrayDeque<CollapseDecision>();

    private static void initializeDirectionOffsets() {
        DIRECTION_OFFSETS.put(Direction.NORTH, new Vector3i(0, 0, -1));
        DIRECTION_OFFSETS.put(Direction.SOUTH, new Vector3i(0, 0, 1));
        DIRECTION_OFFSETS.put(Direction.EAST, new Vector3i(1, 0, 0));
        DIRECTION_OFFSETS.put(Direction.WEST, new Vector3i(-1, 0, 0));
        DIRECTION_OFFSETS.put(Direction.UP, new Vector3i(0, 1, 0));
        DIRECTION_OFFSETS.put(Direction.DOWN, new Vector3i(0, -1, 0));
    }

    public WFCLattice(int latticeRadius, int nodeSizeXZ, int nodeSizeY, int minYLevel, int maxYLevel) {
        this.latticeRadius = latticeRadius;
        this.nodeSizeXZ = nodeSizeXZ;
        this.nodeSizeY = nodeSizeY;
        this.minYLevel = minYLevel;
        this.maxYLevel = maxYLevel;
        this.nodeMap = new HashMap<Vector3i, WFCNode>();
        Comparator<WFCNode> entropyComparator = this.createEntropyComparator();
        this.entropyQueue = new PriorityQueue<WFCNode>(entropyComparator);
    }

    public void initializeLattice(World world, WFCGenerator wfcGenerator) {
        for (int x = -this.latticeRadius; x <= this.latticeRadius; ++x) {
            for (int z = -this.latticeRadius; z <= this.latticeRadius; ++z) {
                for (int y = this.minYLevel - 1; y <= this.maxYLevel + 1; ++y) {
                    Vector3i gridCoord = new Vector3i(x, y, z);
                    WFCNode node = new WFCNode(gridCoord, world, this, this.nodeMap, wfcGenerator);
                    this.nodeMap.put(gridCoord, node);
                }
            }
        }
        for (WFCNode node : this.nodeMap.values()) {
            node.initializeNeighbors();
        }
    }

    private Comparator<WFCNode> createEntropyComparator() {
        return Comparator.comparingInt(WFCNode::getValidOptionCount).thenComparingInt(WFCNode::getMagnitudeSquared);
    }

    public static Vector3i getDirectionOffset(Direction direction) {
        return new Vector3i(DIRECTION_OFFSETS.get((Object)direction));
    }

    public void updateNodeEntropy(WFCNode node) {
        if (node == null || node.isCollapsed() || node.isBoundary()) {
            return;
        }
        this.entropyQueue.remove(node);
        boolean hasCollapsedNonEmptyNeighbors = false;
        for (WFCNode neighbor : node.getOrientedNeighbors().values()) {
            if (neighbor == null || !neighbor.isCollapsed() || neighbor.isNothing()) continue;
            hasCollapsedNonEmptyNeighbors = true;
        }
        if (!hasCollapsedNonEmptyNeighbors) {
            return;
        }
        node.updatePossibleStates();
        this.entropyQueue.add(node);
    }

    public void recordCollapseDecision(WFCNode node, ModulesContainer chosenModule) {
        HashSet<ModulesContainer> previousStates = node.getValidOptions() != null ? new HashSet<ModulesContainer>(node.getValidOptions()) : new HashSet();
        HashSet<Vector3i> affectedNeighbors = new HashSet<Vector3i>();
        for (WFCNode neighbor : node.getOrientedNeighbors().values()) {
            if (neighbor == null) continue;
            affectedNeighbors.add(neighbor.getCellLocation());
        }
        this.decisionStack.push(new CollapseDecision(node.getCellLocation(), chosenModule, previousStates, affectedNeighbors));
    }

    public boolean backtrack() {
        if (this.decisionStack.isEmpty()) {
            return false;
        }
        CollapseDecision decision = this.decisionStack.pop();
        WFCNode node = this.nodeMap.get(decision.nodePosition);
        if (node == null) {
            Logger.warn("Node not found for backtracking at " + String.valueOf(decision.nodePosition));
            return false;
        }
        node.setModulesContainer(null);
        node.updatePossibleStates();
        if (node.getValidOptions() != null && node.getValidOptions().contains(decision.chosenModule)) {
            node.getValidOptions().remove(decision.chosenModule);
        }
        for (Vector3i neighborPos : decision.affectedNeighbors) {
            WFCNode neighbor = this.nodeMap.get(neighborPos);
            if (neighbor == null) continue;
            this.updateNodeEntropy(neighbor);
        }
        this.updateNodeEntropy(node);
        return true;
    }

    public int getBacktrackDepth() {
        return this.decisionStack.size();
    }

    public void clearBacktrackHistory() {
        this.decisionStack.clear();
    }

    private boolean isEmpty(WFCNode node) {
        return node.getModulesContainer() != null && node.getModulesContainer().isNothing();
    }

    private boolean isWorldBoundary(WFCNode node) {
        return node.getModulesContainer() != null && node.getModulesContainer().getClipboardFilename().equals("world_border");
    }

    public void clearGenerationData() {
        this.nodeMap.values().forEach(WFCNode::clearGenerationData);
        this.entropyQueue.clear();
        this.clearBacktrackHistory();
    }

    public void clearAllData() {
        this.nodeMap.clear();
        this.entropyQueue.clear();
        this.clearBacktrackHistory();
    }

    public boolean isWithinBounds(Vector3i location) {
        return Math.abs(location.x) <= this.latticeRadius && location.y >= this.minYLevel && location.y <= this.maxYLevel && Math.abs(location.z) <= this.latticeRadius;
    }

    public boolean isBoundary(Vector3i location) {
        return location.x == -this.latticeRadius || location.x == this.latticeRadius || location.z == -this.latticeRadius || location.z == this.latticeRadius;
    }

    public WFCNode getLowestEntropyNode() {
        WFCNode next = this.entropyQueue.poll();
        block0: while (next != null && this.isEmpty(next)) {
            next = this.entropyQueue.poll();
            if (next == null) {
                return null;
            }
            for (WFCNode neighbor : next.getOrientedNeighbors().values()) {
                if (neighbor == null || !neighbor.isCollapsed()) continue;
                next = null;
                continue block0;
            }
        }
        return next;
    }

    public Vector3i worldToLattice(Vector3i worldCoord) {
        return new Vector3i(Math.floorDiv(worldCoord.x, this.nodeSizeXZ), Math.floorDiv(worldCoord.y, this.nodeSizeY), Math.floorDiv(worldCoord.z, this.nodeSizeXZ));
    }

    public Vector3i latticeToWorld(Vector3i latticeCoord) {
        return new Vector3i(latticeCoord.x * this.nodeSizeXZ + -this.nodeSizeXZ / 2, latticeCoord.y * this.nodeSizeY + this.nodeSizeY / 2, latticeCoord.z * this.nodeSizeXZ + -this.nodeSizeXZ / 2);
    }

    public int getLatticeRadius() {
        return this.latticeRadius;
    }

    public int getNodeSizeXZ() {
        return this.nodeSizeXZ;
    }

    public int getNodeSizeY() {
        return this.nodeSizeY;
    }

    public int getMinYLevel() {
        return this.minYLevel;
    }

    public int getMaxYLevel() {
        return this.maxYLevel;
    }

    public Map<Vector3i, WFCNode> getNodeMap() {
        return this.nodeMap;
    }

    public PriorityQueue<WFCNode> getEntropyQueue() {
        return this.entropyQueue;
    }

    static {
        WFCLattice.initializeDirectionOffsets();
    }

    private static class CollapseDecision {
        final Vector3i nodePosition;
        final ModulesContainer chosenModule;
        final Set<ModulesContainer> previousPossibleStates;
        final Set<Vector3i> affectedNeighbors;

        CollapseDecision(Vector3i nodePosition, ModulesContainer chosenModule, Set<ModulesContainer> previousPossibleStates, Set<Vector3i> affectedNeighbors) {
            this.nodePosition = nodePosition;
            this.chosenModule = chosenModule;
            this.previousPossibleStates = previousPossibleStates;
            this.affectedNeighbors = affectedNeighbors;
        }
    }
}

