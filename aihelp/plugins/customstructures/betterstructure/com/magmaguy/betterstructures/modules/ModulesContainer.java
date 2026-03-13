/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.betterstructures.config.modules.ModulesConfigFields;
import com.magmaguy.betterstructures.modules.Direction;
import com.magmaguy.betterstructures.modules.WFCNode;
import com.magmaguy.betterstructures.util.WeighedProbability;
import com.magmaguy.magmacore.util.Logger;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joml.Vector3i;

public class ModulesContainer {
    private static final HashMap<String, ModulesContainer> modulesContainers = new HashMap();
    private static final List<Integer> validRotations = Arrays.asList(0, 90, 180, 270);
    private final Clipboard clipboard;
    private final String clipboardFilename;
    private final String configFilename;
    private final int rotation;
    private final Map<Direction, HashSet<ModulesContainer>> validBorders = new HashMap<Direction, HashSet<ModulesContainer>>();
    private final ModulesConfigFields modulesConfigField;
    private BorderTags borderTags = new BorderTags(new EnumMap<Direction, List<NeighborTag>>(Direction.class));
    private boolean nothing = false;
    private boolean horizontalEdge = false;
    private static final String WORLD_BORDER = "world_border";
    public static ModulesContainer nothingContainer;

    public ModulesContainer(Clipboard clipboard, String clipboardFilename, ModulesConfigFields modulesConfigField, String configFilename, int rotation) {
        this.clipboard = clipboard;
        this.clipboardFilename = clipboardFilename;
        this.modulesConfigField = modulesConfigField;
        this.configFilename = configFilename;
        this.rotation = rotation;
        if (!clipboardFilename.equalsIgnoreCase("nothing")) {
            this.processBorders(modulesConfigField.getBorderMap());
            modulesContainers.put(clipboardFilename + "_rotation_" + rotation, this);
        } else {
            this.nothing = true;
            modulesContainers.put(clipboardFilename, this);
        }
    }

    public static void initializeModulesContainer(Clipboard clipboard, String clipboardFilename, ModulesConfigFields modulesConfigField, String configFilename) {
        validRotations.forEach(rotation -> new ModulesContainer(clipboard, clipboardFilename, modulesConfigField, configFilename, (int)rotation));
    }

    public static void postInitializeModulesContainer() {
        block0: for (ModulesContainer modulesContainer : modulesContainers.values()) {
            for (Map.Entry<Direction, List<NeighborTag>> buildBorderListEntry : modulesContainer.borderTags.entrySet()) {
                Direction direction = buildBorderListEntry.getKey();
                List<NeighborTag> borderTags = buildBorderListEntry.getValue();
                for (ModulesContainer neighborContainer : modulesContainers.values()) {
                    List<NeighborTag> neighborTags = neighborContainer.borderTags.neighborMap.get((Object)direction.getOpposite());
                    block3: for (NeighborTag borderTag : borderTags) {
                        for (NeighborTag neighborTag : neighborTags) {
                            if (borderTag.getTag().equalsIgnoreCase("nothing")) {
                                modulesContainer.validBorders.computeIfAbsent(direction, k -> new HashSet()).add(modulesContainers.get("nothing"));
                                ModulesContainer.nothingContainer.validBorders.computeIfAbsent(direction.getOpposite(), k -> new HashSet()).add(modulesContainer);
                                continue;
                            }
                            if (borderTag.getTag().equalsIgnoreCase(WORLD_BORDER)) {
                                modulesContainer.validBorders.computeIfAbsent(direction, k -> new HashSet()).add(neighborContainer);
                                modulesContainer.horizontalEdge = true;
                                continue;
                            }
                            if (!borderTag.getTag().equals(neighborTag.getTag()) || !borderTag.isCanMirror() && !neighborTag.isCanMirror()) continue;
                            modulesContainer.validBorders.computeIfAbsent(direction, k -> new HashSet()).add(neighborContainer);
                            continue block3;
                        }
                    }
                }
            }
            for (Direction direction : Direction.values()) {
                if (modulesContainer.horizontalEdge || modulesContainer.validBorders.get((Object)direction) != null && !modulesContainer.validBorders.get((Object)direction).isEmpty()) continue;
                Logger.warn("No valid neighbors for " + modulesContainer.getClipboardFilename() + " in direction " + String.valueOf((Object)direction));
                continue block0;
            }
        }
    }

    public static void initializeSpecialModules() {
        nothingContainer = new ModulesContainer(null, "nothing", new ModulesConfigFields("nothing", true), null, 0);
        ModulesContainer.nothingContainer.borderTags = new BorderTags(Map.of((Object)((Object)Direction.NORTH), Collections.singletonList(new NeighborTag("nothing")), (Object)((Object)Direction.SOUTH), Collections.singletonList(new NeighborTag("nothing")), (Object)((Object)Direction.EAST), Collections.singletonList(new NeighborTag("nothing")), (Object)((Object)Direction.WEST), Collections.singletonList(new NeighborTag("nothing")), (Object)((Object)Direction.UP), Collections.singletonList(new NeighborTag("nothing")), (Object)((Object)Direction.DOWN), Collections.singletonList(new NeighborTag("nothing"))));
    }

    public static void shutdown() {
        modulesContainers.clear();
    }

    public static HashSet<ModulesContainer> getValidModulesFromSurroundings(WFCNode WFCNode2) {
        HashSet validModules = null;
        boolean isGridBorder = WFCNode2.getWfcGenerator().getSpatialGrid().isBoundary(WFCNode2.getCellLocation());
        for (Map.Entry<Direction, WFCNode> buildBorderChunkDataEntry : WFCNode2.getOrientedNeighbors().entrySet()) {
            Direction direction = buildBorderChunkDataEntry.getKey();
            if (buildBorderChunkDataEntry.getValue() == null || buildBorderChunkDataEntry.getValue().getModulesContainer() == null) continue;
            HashSet<ModulesContainer> validBorderSpecificModules = new HashSet<ModulesContainer>();
            for (ModulesContainer modulesContainer : buildBorderChunkDataEntry.getValue().getModulesContainer().validBorders.get((Object)direction.getOpposite())) {
                if (modulesContainer == null || !modulesContainer.getModulesConfigField().isAutomaticallyPlaced() || modulesContainer.isHorizontalEdge() != isGridBorder && (!isGridBorder || !modulesContainer.nothing)) continue;
                boolean repeatStop = false;
                for (WFCNode wFCNode : WFCNode2.getOrientedNeighbors().values()) {
                    if (wFCNode == null || wFCNode.getModulesContainer() == null || modulesContainer.nothing || !modulesContainer.getModulesConfigField().isNoRepeat() || !wFCNode.getModulesContainer().getModulesConfigField().getUuid().equals(modulesContainer.getModulesConfigField().getUuid())) continue;
                    repeatStop = true;
                    break;
                }
                if (repeatStop) continue;
                boolean worldBorderFacesTheOutside = true;
                if (isGridBorder) {
                    block3: for (Map.Entry<Direction, List<NeighborTag>> directionListEntry : modulesContainer.getBorderTags().entrySet()) {
                        Direction checkDirection = directionListEntry.getKey();
                        boolean isOutwardDirection = false;
                        Vector3i pos = WFCNode2.getCellLocation();
                        if (pos.x == -WFCNode2.getWfcGenerator().getSpatialGrid().getLatticeRadius() && checkDirection == Direction.WEST || pos.x == WFCNode2.getWfcGenerator().getSpatialGrid().getLatticeRadius() && checkDirection == Direction.EAST || pos.z == -WFCNode2.getWfcGenerator().getSpatialGrid().getLatticeRadius() && checkDirection == Direction.NORTH || pos.z == WFCNode2.getWfcGenerator().getSpatialGrid().getLatticeRadius() && checkDirection == Direction.SOUTH) {
                            isOutwardDirection = true;
                        }
                        if (!isOutwardDirection) continue;
                        for (NeighborTag tag : directionListEntry.getValue()) {
                            boolean isWorldBorderTag = tag.getTag().equalsIgnoreCase(WORLD_BORDER);
                            if (isWorldBorderTag) continue;
                            worldBorderFacesTheOutside = false;
                            continue block3;
                        }
                    }
                }
                if (!worldBorderFacesTheOutside || !ModulesContainer.checkVerticalRotationValidity(direction, buildBorderChunkDataEntry.getValue().getModulesContainer(), modulesContainer) || !ModulesContainer.checkHorizontalRotationValidity(direction, buildBorderChunkDataEntry.getValue().getModulesContainer(), modulesContainer)) continue;
                Vector3i vector3i = WFCNode2.getCellLocation();
                if (vector3i.y < modulesContainer.modulesConfigField.getMinY() || vector3i.y > modulesContainer.modulesConfigField.getMaxY()) continue;
                validBorderSpecificModules.add(modulesContainer);
            }
            if (validModules == null) {
                validModules = new HashSet(validBorderSpecificModules);
                continue;
            }
            if (validBorderSpecificModules.isEmpty()) continue;
            validModules.retainAll(validBorderSpecificModules);
        }
        if (validModules == null || validModules.isEmpty()) {
            return new HashSet<ModulesContainer>();
        }
        return validModules;
    }

    private static boolean checkVerticalRotationValidity(Direction direction, ModulesContainer module, ModulesContainer neighbour) {
        if (direction != Direction.UP && direction != Direction.DOWN) {
            return true;
        }
        if (module.nothing || neighbour.nothing) {
            return true;
        }
        if (!neighbour.modulesConfigField.isEnforceVerticalRotation() && !module.modulesConfigField.isEnforceVerticalRotation()) {
            return true;
        }
        return module.rotation == neighbour.rotation;
    }

    private static boolean checkHorizontalRotationValidity(Direction direction, ModulesContainer module, ModulesContainer neighbour) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return true;
        }
        if (module.nothing || neighbour.nothing) {
            return true;
        }
        if (!module.modulesConfigField.isEnforceHorizontalRotation() && !neighbour.modulesConfigField.isEnforceHorizontalRotation()) {
            return true;
        }
        return module.rotation == neighbour.rotation;
    }

    public static ModulesContainer pickWeightedRandomModule(HashSet<ModulesContainer> modules, WFCNode WFCNode2) {
        HashMap<Integer, Double> weightMap = new HashMap<Integer, Double>();
        HashMap<Integer, ModulesContainer> moduleMap = new HashMap<Integer, ModulesContainer>();
        int index = 0;
        for (ModulesContainer modulesContainer : modules) {
            double weight = modulesContainer.getWeight();
            if (!modulesContainer.nothing && modulesContainer.getModulesConfigField().getRepetitionPenalty() != 0.0) {
                for (WFCNode value : WFCNode2.getOrientedNeighbors().values()) {
                    if (value == null || value.getModulesContainer() == null || !modulesContainer.getClipboardFilename().equals(value.getModulesContainer().getClipboardFilename())) continue;
                    weight += modulesContainer.getModulesConfigField().getRepetitionPenalty();
                }
            }
            weightMap.put(index, weight);
            moduleMap.put(index, modulesContainer);
            ++index;
        }
        return (ModulesContainer)moduleMap.get(WeighedProbability.pickWeightedProbability(weightMap));
    }

    private double getWeight() {
        if (this.nothing) {
            return 50.0;
        }
        return this.modulesConfigField.getWeight();
    }

    private void processBorders(Map<String, Object> borderMap) {
        for (Map.Entry<String, Object> entry : borderMap.entrySet()) {
            ArrayList<NeighborTag> processedBorderList = new ArrayList<NeighborTag>();
            for (String tag : (List)entry.getValue()) {
                processedBorderList.add(new NeighborTag(tag));
            }
            Direction border = Direction.fromString(entry.getKey());
            if (border == null) {
                Logger.warn("Invalid border " + entry.getKey() + " for module " + this.configFilename);
                continue;
            }
            this.borderTags.put(Direction.transformDirection(border, this.rotation), processedBorderList);
        }
        for (Direction border : Direction.values()) {
            if (this.borderTags.containsKey(border)) continue;
            Logger.warn("Failed to get module border " + border.toString() + " for module " + this.configFilename);
        }
    }

    public static HashMap<String, ModulesContainer> getModulesContainers() {
        return modulesContainers;
    }

    public Clipboard getClipboard() {
        return this.clipboard;
    }

    public String getClipboardFilename() {
        return this.clipboardFilename;
    }

    public int getRotation() {
        return this.rotation;
    }

    public ModulesConfigFields getModulesConfigField() {
        return this.modulesConfigField;
    }

    public BorderTags getBorderTags() {
        return this.borderTags;
    }

    public boolean isNothing() {
        return this.nothing;
    }

    public boolean isHorizontalEdge() {
        return this.horizontalEdge;
    }

    public record BorderTags(Map<Direction, List<NeighborTag>> neighborMap) {
        public void put(Direction direction, List<NeighborTag> tags) {
            this.neighborMap.put(direction, tags);
        }

        public boolean containsKey(Direction direction) {
            return this.neighborMap.containsKey((Object)direction);
        }

        public Set<Map.Entry<Direction, List<NeighborTag>>> entrySet() {
            return this.neighborMap.entrySet();
        }

        public Collection<List<NeighborTag>> values() {
            return this.neighborMap.values();
        }
    }

    public static class NeighborTag {
        private String tag;
        private boolean canMirror = true;
        private boolean isWorldBorder = false;

        public NeighborTag(String tag) {
            this.tag = tag;
            if (tag.contains("no-mirror_")) {
                this.canMirror = false;
                this.tag = this.tag.replace("no-mirror_", "");
            }
            if (tag.equalsIgnoreCase(ModulesContainer.WORLD_BORDER)) {
                this.isWorldBorder = true;
            }
        }

        public String getTag() {
            return this.tag;
        }

        public boolean isCanMirror() {
            return this.canMirror;
        }

        public boolean isWorldBorder() {
            return this.isWorldBorder;
        }
    }
}

