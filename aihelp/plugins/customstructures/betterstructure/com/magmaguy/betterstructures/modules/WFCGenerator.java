/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfigFields;
import com.magmaguy.betterstructures.modules.ModulePasting;
import com.magmaguy.betterstructures.modules.ModulesContainer;
import com.magmaguy.betterstructures.modules.WFCLattice;
import com.magmaguy.betterstructures.modules.WFCNode;
import com.magmaguy.betterstructures.modules.WorldInitializer;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3i;

public class WFCGenerator {
    public static HashSet<WFCGenerator> wfcGenerators = new HashSet();
    private ModuleGeneratorsConfigFields moduleGeneratorsConfigFields;
    private WFCLattice spatialGrid;
    private Player player = null;
    private String startingModule;
    private World world;
    private Location startLocation = null;
    private volatile boolean isGenerating;
    private volatile boolean isCancelled;
    private File worldFolder;
    private String worldName;
    private int rollbackCounter = 0;
    private BossBar progressBar;
    private int totalNodes = 0;
    private int completedNodes = 0;

    public WFCGenerator(ModuleGeneratorsConfigFields moduleGeneratorsConfigFields, Player player) {
        this.player = player;
        this.startLocation = player.getLocation();
        this.initialize(moduleGeneratorsConfigFields);
    }

    public WFCGenerator(ModuleGeneratorsConfigFields moduleGeneratorsConfigFields, Location startLocation) {
        this.moduleGeneratorsConfigFields = moduleGeneratorsConfigFields;
        this.startLocation = startLocation;
        this.initialize(moduleGeneratorsConfigFields);
    }

    public static void generateFromConfig(final ModuleGeneratorsConfigFields generatorsConfigFields, final Player player) {
        new BukkitRunnable(){

            public void run() {
                if (generatorsConfigFields.isWorldGeneration()) {
                    String baseWorldName = generatorsConfigFields.getFilename().replace(".yml", "");
                    File worldContainer = Bukkit.getWorldContainer();
                    int i = 0;
                    String worldName = baseWorldName + "_" + i;
                    File worldFolder = new File(worldContainer, worldName);
                    while (worldFolder.exists()) {
                        worldName = baseWorldName + "_" + i;
                        ++i;
                        worldFolder = new File(worldContainer, worldName);
                    }
                } else {
                    new WFCGenerator(generatorsConfigFields, player);
                }
            }
        }.runTaskAsynchronously(MetadataHandler.PLUGIN);
    }

    public static void shutdown() {
        wfcGenerators.forEach(WFCGenerator::cancel);
        wfcGenerators.clear();
    }

    private void initializeProgressBar() {
        if (this.player != null) {
            this.progressBar = Bukkit.createBossBar((String)"Generating Structure...", (BarColor)BarColor.BLUE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
            this.progressBar.addPlayer(this.player);
            this.progressBar.setProgress(0.0);
        }
    }

    private void updateProgressBar(String message) {
        if (this.progressBar != null && this.totalNodes > 0) {
            double progress = (double)this.completedNodes / (double)this.totalNodes;
            this.progressBar.setProgress(Math.min(progress, 1.0));
            this.progressBar.setTitle(message);
        }
    }

    private void removeProgressBar() {
        if (this.progressBar != null) {
            this.progressBar.removeAll();
            this.progressBar = null;
        }
    }

    private void initialize(ModuleGeneratorsConfigFields moduleGeneratorsConfigFields) {
        this.moduleGeneratorsConfigFields = moduleGeneratorsConfigFields;
        this.spatialGrid = new WFCLattice(moduleGeneratorsConfigFields.getRadius(), moduleGeneratorsConfigFields.getModuleSizeXZ(), moduleGeneratorsConfigFields.getModuleSizeY(), moduleGeneratorsConfigFields.getMinChunkY(), moduleGeneratorsConfigFields.getMaxChunkY());
        wfcGenerators.add(this);
        int radius = moduleGeneratorsConfigFields.getRadius();
        int minY = moduleGeneratorsConfigFields.getMinChunkY();
        int maxY = moduleGeneratorsConfigFields.getMaxChunkY();
        this.totalNodes = (radius * 2 + 1) * (radius * 2 + 1) * (maxY - minY + 1);
        List<String> startModules = moduleGeneratorsConfigFields.getStartModules();
        if (startModules.isEmpty()) {
            if (this.player != null) {
                this.player.sendMessage("No start modules exist, you need to install or make modules first!");
            }
            Logger.warn("No start modules exist, you need to install or make modules first!");
            this.cancel();
            return;
        }
        this.startingModule = startModules.get(ThreadLocalRandom.current().nextInt(moduleGeneratorsConfigFields.getStartModules().size())) + "_rotation_0";
        if (moduleGeneratorsConfigFields.isWorldGeneration()) {
            String baseWorldName = moduleGeneratorsConfigFields.getFilename().replace(".yml", "");
            File worldContainer = Bukkit.getWorldContainer();
            int i = 0;
            String worldName = baseWorldName + "_" + i;
            File worldFolder = new File(worldContainer, worldName);
            while (worldFolder.exists()) {
                worldName = baseWorldName + "_" + i;
                ++i;
                worldFolder = new File(worldContainer, worldName);
            }
        }
        this.initializeProgressBar();
        this.reserveChunks();
    }

    private void reserveChunks() {
        this.updateProgressBar("Initializing lattice...");
        this.world = this.moduleGeneratorsConfigFields.isWorldGeneration() ? WorldInitializer.generateWorld(this.worldName, this.player) : this.startLocation.getWorld();
        this.spatialGrid.initializeLattice(this.world, this);
        this.startArrangingModules();
    }

    private void startArrangingModules() {
        this.updateProgressBar("Starting generation...");
        new InitializeGenerationTask().runTaskAsynchronously(MetadataHandler.PLUGIN);
    }

    private void start(String startingModule) {
        if (this.isGenerating) {
            return;
        }
        this.isGenerating = true;
        this.updateProgressBar("Collapsing initial node...");
        try {
            WFCNode startChunk = this.createStartChunk(startingModule);
            if (startChunk == null) {
                return;
            }
            this.generateFast();
        } catch (Exception e) {
            Logger.warn("Error during generation: " + e.getMessage());
            e.printStackTrace();
            this.cleanup();
        }
    }

    private WFCNode createStartChunk(String startingModule) {
        WFCNode startCell = this.spatialGrid.getNodeMap().get(new Vector3i());
        ModulesContainer modulesContainer = ModulesContainer.getModulesContainers().get(startingModule);
        if (modulesContainer == null) {
            Logger.warn("Starting module was null! Cancelling!");
            return null;
        }
        this.paste(startCell, modulesContainer);
        ++this.completedNodes;
        return startCell;
    }

    private void generateFast() {
        this.updateProgressBar("Propagating constraints...");
        while (!this.isCancelled) {
            WFCNode nextCell = this.spatialGrid.getLowestEntropyNode();
            if (nextCell == null) {
                this.done();
                break;
            }
            this.generateNextChunk(nextCell);
        }
    }

    private void paste(WFCNode gridCell, ModulesContainer modulesContainer) {
        this.spatialGrid.recordCollapseDecision(gridCell, modulesContainer);
        gridCell.setModulesContainer(modulesContainer);
        gridCell.getOrientedNeighbors().values().forEach(this.spatialGrid::updateNodeEntropy);
    }

    private void generateNextChunk(WFCNode gridCell) {
        HashSet<ModulesContainer> validOptions = gridCell.getValidOptions();
        if (validOptions == null || validOptions.isEmpty()) {
            this.updateProgressBar("Backtracking...");
            Location targetLocation = gridCell.getRealCenterLocation();
            this.rollbackChunk();
            return;
        }
        ModulesContainer modulesContainer = ModulesContainer.pickWeightedRandomModule(validOptions, gridCell);
        if (modulesContainer == null) {
            this.updateProgressBar("Backtracking...");
            this.rollbackChunk();
            return;
        }
        this.paste(gridCell, modulesContainer);
        ++this.completedNodes;
        this.updateProgressBar("Generating... (" + this.completedNodes + "/" + this.totalNodes + ")");
    }

    private void rollbackChunk() {
        if (!this.spatialGrid.backtrack()) {
            this.updateProgressBar("Generation failed - no decisions to backtrack");
            this.cancel();
            return;
        }
        this.updateProgressBar("Backtracking... (" + this.spatialGrid.getBacktrackDepth() + " decisions remaining)");
        ++this.rollbackCounter;
        if (this.rollbackCounter > 1000) {
            this.updateProgressBar("Generation failed - exceeded backtrack limit");
            Logger.warn("Exceeded backtrack limit!");
            this.cancel();
            if (this.player != null) {
                new WFCGenerator(this.moduleGeneratorsConfigFields, this.player);
            } else {
                new WFCGenerator(this.moduleGeneratorsConfigFields, this.startLocation);
            }
        }
    }

    private void done() {
        this.updateProgressBar("Generation complete!");
        if (this.player != null) {
            this.player.sendMessage("Done assembling!");
            this.player.sendMessage("It will take a moment to paste the structure, and will require relogging.");
        }
        this.isGenerating = false;
        this.instantPaste();
        this.spatialGrid.clearGenerationData();
        this.removeProgressBar();
    }

    private void cleanup() {
        this.spatialGrid.clearAllData();
        wfcGenerators.remove(this);
        this.removeProgressBar();
    }

    public void cancel() {
        this.isCancelled = true;
        this.removeProgressBar();
    }

    private void instantPaste() {
        ArrayDeque<WFCNode> orderedPasteDeque = new ArrayDeque<WFCNode>();
        for (int x = -this.spatialGrid.getLatticeRadius(); x < this.spatialGrid.getLatticeRadius(); ++x) {
            for (int z = -this.spatialGrid.getLatticeRadius(); z < this.spatialGrid.getLatticeRadius(); ++z) {
                for (int y = this.spatialGrid.getMinYLevel(); y <= this.spatialGrid.getMaxYLevel(); ++y) {
                    WFCNode cell = this.spatialGrid.getNodeMap().remove(new Vector3i(x, y, z));
                    if (cell == null) continue;
                    orderedPasteDeque.add(cell);
                }
            }
        }
        new ModulePasting(this.world, this.worldFolder, orderedPasteDeque, this.moduleGeneratorsConfigFields.getSpawnPoolSuffix(), this.startLocation, this.moduleGeneratorsConfigFields);
        this.cleanup();
    }

    public ModuleGeneratorsConfigFields getModuleGeneratorsConfigFields() {
        return this.moduleGeneratorsConfigFields;
    }

    public WFCLattice getSpatialGrid() {
        return this.spatialGrid;
    }

    public World getWorld() {
        return this.world;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    private class InitializeGenerationTask
    extends BukkitRunnable {
        private InitializeGenerationTask() {
        }

        public void run() {
            WFCGenerator.this.start(WFCGenerator.this.startingModule);
        }
    }
}

