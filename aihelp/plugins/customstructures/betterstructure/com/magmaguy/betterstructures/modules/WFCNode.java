/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Display$Billboard
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.TextDisplay
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Transformation
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.modules.Direction;
import com.magmaguy.betterstructures.modules.ModulePasting;
import com.magmaguy.betterstructures.modules.ModulesContainer;
import com.magmaguy.betterstructures.modules.WFCGenerator;
import com.magmaguy.betterstructures.modules.WFCLattice;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class WFCNode {
    private final Vector3i nodePosition;
    private final WFCLattice lattice;
    private final World world;
    private final Map<Vector3i, WFCNode> nodeMap;
    private final int magnitudeSquared;
    private final WFCGenerator wfcGenerator;
    private ModulesContainer modulesContainer;
    private HashSet<ModulesContainer> possibleStates = null;
    private List<TextDisplay> textDisplays;
    private Map<Direction, WFCNode> adjacentNodes = new EnumMap<Direction, WFCNode>(Direction.class);

    public WFCNode(Vector3i nodePosition, World world, WFCLattice lattice, Map<Vector3i, WFCNode> nodeMap, WFCGenerator wfcGenerator) {
        this.nodePosition = new Vector3i(nodePosition);
        this.world = world;
        this.lattice = lattice;
        this.nodeMap = nodeMap;
        this.magnitudeSquared = (int)nodePosition.lengthSquared();
        this.wfcGenerator = wfcGenerator;
        if (wfcGenerator.getModuleGeneratorsConfigFields().isDebug()) {
            if (this.isBoundary()) {
                this.debugPaste(Material.PURPLE_STAINED_GLASS);
            } else {
                this.debugPaste(Material.RED_STAINED_GLASS);
            }
        }
        if (this.isBoundary()) {
            this.modulesContainer = ModulesContainer.nothingContainer;
        }
    }

    public void initializeNeighbors() {
        for (Direction direction : Direction.values()) {
            Vector3i offset = WFCLattice.getDirectionOffset(direction);
            Vector3i neighborPos = new Vector3i(this.nodePosition).add(offset);
            this.adjacentNodes.put(direction, this.nodeMap.get(neighborPos));
        }
    }

    public void setModulesContainer(ModulesContainer modulesContainer) {
        this.modulesContainer = modulesContainer;
        if (this.wfcGenerator.getModuleGeneratorsConfigFields().isDebug()) {
            if (modulesContainer == null) {
                this.debugPaste(Material.GRAY_STAINED_GLASS);
            } else if (modulesContainer.isNothing()) {
                this.debugPaste(Material.BLUE_STAINED_GLASS);
            } else {
                this.debugPaste(Material.GREEN_STAINED_GLASS);
            }
        }
    }

    public boolean isBoundary() {
        return Math.abs(this.nodePosition.x) == this.lattice.getLatticeRadius() || Math.abs(this.nodePosition.z) == this.lattice.getLatticeRadius() || this.nodePosition.y < this.lattice.getMinYLevel() || this.nodePosition.y > this.lattice.getMaxYLevel();
    }

    public Vector3i getCellLocation() {
        return new Vector3i(this.nodePosition);
    }

    public void updatePossibleStates() {
        this.possibleStates = ModulesContainer.getValidModulesFromSurroundings(this);
        this.showDebugTextDisplays();
    }

    public int getValidOptionCount() {
        if (this.possibleStates == null) {
            this.updatePossibleStates();
        }
        if (this.possibleStates == null) {
            Logger.warn("Valid options were null when trying to get the size for cell at " + String.valueOf(this.nodePosition));
            return 0;
        }
        return this.possibleStates.size();
    }

    public Map<Direction, WFCNode> getOrientedNeighbors() {
        return this.adjacentNodes;
    }

    public HashSet<ModulesContainer> getValidOptions() {
        if (this.possibleStates == null) {
            this.updatePossibleStates();
        }
        return this.possibleStates;
    }

    public Location getRealLocation(Location startLocation) {
        Vector3i worldCoord = startLocation != null ? this.lattice.latticeToWorld(this.nodePosition).add(startLocation.getBlockX(), startLocation.getBlockY(), startLocation.getBlockZ()) : this.lattice.latticeToWorld(this.nodePosition);
        return new Location(this.world, (double)worldCoord.x, (double)worldCoord.y, (double)worldCoord.z);
    }

    public void showDebugTextDisplays() {
        if (!this.wfcGenerator.getModuleGeneratorsConfigFields().isDebug()) {
            return;
        }
        new BukkitRunnable(){

            public void run() {
                if (WFCNode.this.textDisplays != null && !WFCNode.this.textDisplays.isEmpty()) {
                    WFCNode.this.clearDebugDisplays();
                }
                WFCNode.this.textDisplays = new ArrayList<TextDisplay>();
                if (WFCNode.this.modulesContainer == null) {
                    if (WFCNode.this.possibleStates == null) {
                        WFCNode.this.spawnDebugText(WFCNode.this.getRealCenterLocation(), "Uninitialized", Color.RED, 1.0f);
                        return;
                    }
                    WFCNode.this.spawnDebugText(WFCNode.this.getRealCenterLocation(), "Uninitialized", Color.GREEN, 1.0f);
                    WFCNode.this.spawnDebugText(WFCNode.this.getRealCenterLocation(), "Options count: " + WFCNode.this.possibleStates.size(), Color.GREEN, 1.0f);
                    return;
                }
                Color color = WFCNode.this.generateRandomColor();
                Location centerLocation = WFCNode.this.getRealCenterLocation();
                WFCNode.this.displayMainInfo(centerLocation, color);
                WFCNode.this.displayBorderInfo(centerLocation, color);
            }
        }.runTask(MetadataHandler.PLUGIN);
    }

    private Color generateRandomColor() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return Color.fromRGB((int)random.nextInt(256), (int)random.nextInt(256), (int)random.nextInt(256));
    }

    private Location getLocalCenterLocation() {
        double y = (double)this.lattice.getNodeSizeY() / 2.0;
        if (this.modulesContainer != null && this.modulesContainer.getClipboard() != null) {
            y = (double)this.modulesContainer.getClipboard().getDimensions().y() / 2.0;
        }
        Vector3i worldPos = this.lattice.latticeToWorld(this.nodePosition).add((int)((double)this.lattice.getNodeSizeXZ() / 2.0), (int)y, (int)((double)this.lattice.getNodeSizeXZ() / 2.0));
        return new Location(this.world, (double)worldPos.x, (double)worldPos.y, (double)worldPos.z);
    }

    public Location getRealCenterLocation() {
        return this.getLocalCenterLocation().add(this.wfcGenerator.getStartLocation());
    }

    private void displayMainInfo(Location centerLocation, Color color) {
        this.spawnDebugText(centerLocation, this.modulesContainer.getClipboardFilename(), color, 1.0f);
        this.spawnDebugText(centerLocation.clone().add(0.0, -0.25, 0.0), "Rotation: " + this.modulesContainer.getRotation(), color, 1.0f);
    }

    private void displayBorderInfo(Location centerLocation, Color color) {
        for (Map.Entry<Direction, List<ModulesContainer.NeighborTag>> entry : this.modulesContainer.getBorderTags().entrySet()) {
            Vector3i offset = this.getDirectionOffset(entry.getKey(), 5);
            Location tagLocation = centerLocation.clone().add((double)offset.x, (double)offset.y, (double)offset.z);
            this.spawnDebugText(tagLocation, entry.getKey().name(), color, 1.0f);
            this.displayNeighborTags(tagLocation, entry.getValue(), color);
        }
    }

    private Vector3i getDirectionOffset(Direction direction, int distance) {
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case Direction.UP -> new Vector3i(0, distance, 0);
            case Direction.DOWN -> new Vector3i(0, -distance, 0);
            case Direction.EAST -> new Vector3i(distance, 0, 0);
            case Direction.WEST -> new Vector3i(-distance, 0, 0);
            case Direction.NORTH -> new Vector3i(0, 0, -distance);
            case Direction.SOUTH -> new Vector3i(0, 0, distance);
        };
    }

    private void displayNeighborTags(Location baseLocation, List<ModulesContainer.NeighborTag> tags, Color color) {
        for (int i = 0; i < tags.size(); ++i) {
            Location tagLocation = baseLocation.clone().add(0.0, (double)(-(i + 1)) / 4.0, 0.0);
            this.spawnDebugText(tagLocation, tags.get(i).getTag(), color, 1.0f);
        }
    }

    private void spawnDebugText(final Location location, final String text, final Color color, final float scale) {
        new BukkitRunnable(){

            public void run() {
                Location adjustedLocation = location.clone().subtract(new Vector(0.0, (double)WFCNode.this.textDisplays.size() / 2.0, 0.0));
                TextDisplay textDisplay = (TextDisplay)WFCNode.this.world.spawnEntity(adjustedLocation, EntityType.TEXT_DISPLAY);
                WFCNode.this.configureTextDisplay(textDisplay, text, color, scale);
                WFCNode.this.textDisplays.add(textDisplay);
            }
        }.runTask(MetadataHandler.PLUGIN);
    }

    private void configureTextDisplay(TextDisplay display, String text, Color color, float scale) {
        display.setBillboard(Display.Billboard.CENTER);
        display.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(scale, scale, scale), new AxisAngle4f()));
        display.setBackgroundColor(color);
        display.setSeeThrough(true);
        display.setText(text);
        display.setViewRange(1.0f);
    }

    public boolean isCollapsed() {
        return this.modulesContainer != null;
    }

    public boolean isNothing() {
        return this.modulesContainer != null && this.modulesContainer.isNothing();
    }

    public void resetState() {
        if (this.isInitialNode() || this.isBoundary()) {
            return;
        }
        this.setModulesContainer(null);
        this.possibleStates = null;
        if (this.wfcGenerator.getModuleGeneratorsConfigFields().isDebug()) {
            this.debugPaste(Material.GRAY_STAINED_GLASS);
        }
    }

    public boolean isInitialNode() {
        return new Vector3i().equals(this.nodePosition);
    }

    public void clearGenerationData() {
        this.clearDebugDisplays();
        this.possibleStates = null;
    }

    private void clearDebugDisplays() {
        if (this.textDisplays != null) {
            this.textDisplays.forEach(Entity::remove);
            this.textDisplays.clear();
        }
    }

    private void placeMaterial(Location startLocation, Material material) {
        int sizeXZ = this.wfcGenerator.getModuleGeneratorsConfigFields().getModuleSizeXZ();
        int sizeY = this.wfcGenerator.getModuleGeneratorsConfigFields().getModuleSizeY();
        for (int x = 0; x < sizeXZ; ++x) {
            for (int y = 0; y < sizeY; ++y) {
                for (int z = 0; z < sizeXZ; ++z) {
                    Location blockLocation = startLocation.clone().add((double)x, (double)y, (double)z);
                    boolean isOnXEdge = x == 0 || x == sizeXZ - 1;
                    boolean isOnYEdge = y == 0 || y == sizeY - 1;
                    boolean isOnZEdge = z == 0 || z == sizeXZ - 1;
                    int edgeCount = 0;
                    if (isOnXEdge) {
                        ++edgeCount;
                    }
                    if (isOnYEdge) {
                        ++edgeCount;
                    }
                    if (isOnZEdge) {
                        ++edgeCount;
                    }
                    if (edgeCount >= 2) {
                        blockLocation.getBlock().setType(material);
                        continue;
                    }
                    blockLocation.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public void debugPaste(final Material material) {
        new BukkitRunnable(){

            public void run() {
                WFCNode.this.showDebugTextDisplays();
                Location startLocation = WFCNode.this.getRealLocation(WFCNode.this.wfcGenerator.getStartLocation());
                if (WFCNode.this.modulesContainer == null || WFCNode.this.modulesContainer.isNothing()) {
                    WFCNode.this.placeMaterial(startLocation, material);
                    return;
                }
                ModulePasting.paste(WFCNode.this.modulesContainer.getClipboard(), startLocation, WFCNode.this.modulesContainer.getRotation());
            }
        }.runTask(MetadataHandler.PLUGIN);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public World getWorld() {
        return this.world;
    }

    public int getMagnitudeSquared() {
        return this.magnitudeSquared;
    }

    public WFCGenerator getWfcGenerator() {
        return this.wfcGenerator;
    }

    public ModulesContainer getModulesContainer() {
        return this.modulesContainer;
    }

    public HashSet<ModulesContainer> getPossibleStates() {
        return this.possibleStates;
    }
}

