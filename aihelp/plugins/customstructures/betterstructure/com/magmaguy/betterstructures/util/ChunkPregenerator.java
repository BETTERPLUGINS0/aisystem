/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.scheduler.BukkitTask
 */
package com.magmaguy.betterstructures.util;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.api.BuildPlaceEvent;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.WorkloadRunnable;
import java.lang.invoke.CallSite;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitTask;

public class ChunkPregenerator
implements Listener {
    public static HashSet<ChunkPregenerator> activePregenerators = new HashSet();
    private final World world;
    private final Location center;
    private final String shape;
    private final boolean setWorldBorder;
    private final double tickUsage;
    private final int maxRadiusBlocks;
    private final int maxRadiusChunks;
    private int actualMaxRadiusChunks = 0;
    private final Set<String> generatedChunks = new HashSet<String>();
    private final Set<String> loadedChunks = new HashSet<String>();
    private int newlyGeneratedChunks = 0;
    private int chunksLoadedLast30s = 0;
    private int allChunksLoadedLast30s = 0;
    private int structuresGeneratedTotal = 0;
    private int structuresGeneratedLast30s = 0;
    private Integer expectedChunksTotal = null;
    private BukkitTask statsTask;
    private BukkitTask tpsMonitorTask;
    private BukkitTask currentWorkloadTask;
    private volatile boolean isCancelled = false;
    private volatile boolean isPaused = false;
    private int centerChunkX;
    private int centerChunkZ;
    private int currentRadius = 0;

    public ChunkPregenerator(World world, Location center, String shape, int maxRadiusBlocks, int maxRadiusChunks, boolean setWorldBorder) {
        this.world = world;
        this.center = center;
        this.shape = shape;
        this.maxRadiusBlocks = maxRadiusBlocks;
        this.maxRadiusChunks = maxRadiusChunks;
        this.setWorldBorder = setWorldBorder;
        this.tickUsage = DefaultConfig.getPercentageOfTickUsedForPregeneration();
        Bukkit.getPluginManager().registerEvents((Listener)this, MetadataHandler.PLUGIN);
    }

    public void start() {
        this.centerChunkX = this.center.getBlockX() >> 4;
        this.centerChunkZ = this.center.getBlockZ() >> 4;
        Logger.info("Starting chunk pregeneration with shape: " + this.shape + ", center chunk: (" + this.centerChunkX + ", " + this.centerChunkZ + "), radius: " + this.maxRadiusBlocks + " blocks (" + this.maxRadiusChunks + " chunks)");
        activePregenerators.add(this);
        this.statsTask = Bukkit.getScheduler().runTaskTimer(MetadataHandler.PLUGIN, this::reportStats, 600L, 600L);
        this.tpsMonitorTask = Bukkit.getScheduler().runTaskTimer(MetadataHandler.PLUGIN, this::checkTPSAndPause, 40L, 40L);
        this.generateNextLayer();
    }

    private void checkTPSAndPause() {
        if (this.isCancelled) {
            return;
        }
        double currentTPS = this.getTPS();
        double pauseThreshold = DefaultConfig.getPregenerationTPSPauseThreshold();
        double resumeThreshold = DefaultConfig.getPregenerationTPSResumeThreshold();
        if (currentTPS < pauseThreshold) {
            if (!this.isPaused) {
                this.isPaused = true;
                Logger.warn("Pausing chunk pregeneration - TPS below " + pauseThreshold + " (current: " + String.format("%.2f", currentTPS) + ")");
                if (this.currentWorkloadTask != null) {
                    this.currentWorkloadTask.cancel();
                    this.currentWorkloadTask = null;
                }
            }
        } else if (this.isPaused && currentTPS >= resumeThreshold) {
            this.isPaused = false;
            Logger.info("Resuming chunk pregeneration - TPS recovered to " + String.format("%.2f", currentTPS) + " (above " + resumeThreshold + ")");
            this.generateNextLayer();
        }
    }

    private void generateNextLayer() {
        if (this.isCancelled) {
            this.onCancelled();
            return;
        }
        if (this.isPaused) {
            return;
        }
        if (this.currentRadius > this.maxRadiusChunks) {
            this.onComplete();
            return;
        }
        this.actualMaxRadiusChunks = this.currentRadius;
        boolean[] chunksAdded = new boolean[]{false};
        WorkloadRunnable workload = new WorkloadRunnable(this.tickUsage, () -> {
            if (this.isCancelled) {
                this.onCancelled();
                return;
            }
            if (chunksAdded[0]) {
                ++this.currentRadius;
                this.generateNextLayer();
            } else {
                this.onComplete();
            }
        });
        if ("SQUARE".equalsIgnoreCase(this.shape)) {
            chunksAdded[0] = this.generateSquareLayer(workload, this.currentRadius);
        } else if ("CIRCLE".equalsIgnoreCase(this.shape)) {
            chunksAdded[0] = this.generateCircleLayer(workload, this.currentRadius);
        } else {
            Logger.warn("Invalid shape: " + this.shape + ". Must be SQUARE or CIRCLE.");
            this.onComplete();
            return;
        }
        if (!chunksAdded[0]) {
            ++this.currentRadius;
            this.generateNextLayer();
            return;
        }
        this.currentWorkloadTask = workload.runTaskTimer(MetadataHandler.PLUGIN, 0L, 1L);
    }

    private boolean generateSquareLayer(WorkloadRunnable workload, int radius) {
        boolean chunksAdded = false;
        for (int x = this.centerChunkX - radius; x <= this.centerChunkX + radius; ++x) {
            if (this.addChunkToWorkload(workload, x, this.centerChunkZ - radius)) {
                chunksAdded = true;
            }
            if (!this.addChunkToWorkload(workload, x, this.centerChunkZ + radius)) continue;
            chunksAdded = true;
        }
        for (int z = this.centerChunkZ - radius + 1; z < this.centerChunkZ + radius; ++z) {
            if (this.addChunkToWorkload(workload, this.centerChunkX - radius, z)) {
                chunksAdded = true;
            }
            if (!this.addChunkToWorkload(workload, this.centerChunkX + radius, z)) continue;
            chunksAdded = true;
        }
        return chunksAdded;
    }

    private boolean generateCircleLayer(WorkloadRunnable workload, int radius) {
        boolean chunksAdded = false;
        int radiusSquared = radius * radius;
        int nextRadiusSquared = (radius + 1) * (radius + 1);
        for (int x = this.centerChunkX - radius - 1; x <= this.centerChunkX + radius + 1; ++x) {
            for (int z = this.centerChunkZ - radius - 1; z <= this.centerChunkZ + radius + 1; ++z) {
                int dx = x - this.centerChunkX;
                int dz = z - this.centerChunkZ;
                int distanceSquared = dx * dx + dz * dz;
                if (distanceSquared < radiusSquared || distanceSquared >= nextRadiusSquared || !this.addChunkToWorkload(workload, x, z)) continue;
                chunksAdded = true;
            }
        }
        return chunksAdded;
    }

    private boolean addChunkToWorkload(WorkloadRunnable workload, int chunkX, int chunkZ) {
        String chunkKey = chunkX + "," + chunkZ;
        if (this.generatedChunks.contains(chunkKey)) {
            return false;
        }
        this.generatedChunks.add(chunkKey);
        workload.addWorkload(() -> this.generateChunk(chunkX, chunkZ));
        return true;
    }

    private void generateChunk(int chunkX, int chunkZ) {
        try {
            Chunk chunk = this.world.getChunkAt(chunkX, chunkZ);
            if (!chunk.isLoaded()) {
                chunk.load(true);
            }
        } catch (Exception e) {
            Logger.warn("Failed to generate chunk at (" + chunkX + ", " + chunkZ + "): " + e.getMessage());
        }
    }

    private void onComplete() {
        this.cleanup();
        Logger.info("Chunk pregeneration completed. Processed " + this.generatedChunks.size() + " chunks, newly generated " + this.newlyGeneratedChunks + " chunks with max radius: " + this.maxRadiusBlocks + " blocks (" + this.actualMaxRadiusChunks + " chunks)");
        if (this.setWorldBorder) {
            this.setWorldBorder();
        }
    }

    private void onCancelled() {
        this.cleanup();
        Logger.info("Chunk pregeneration cancelled. Processed " + this.generatedChunks.size() + " chunks, newly generated " + this.newlyGeneratedChunks + " chunks with max radius reached: " + this.actualMaxRadiusChunks * 16 + " blocks (" + this.actualMaxRadiusChunks + " chunks)");
    }

    private void cleanup() {
        if (this.statsTask != null) {
            this.statsTask.cancel();
            this.statsTask = null;
        }
        if (this.tpsMonitorTask != null) {
            this.tpsMonitorTask.cancel();
            this.tpsMonitorTask = null;
        }
        if (this.currentWorkloadTask != null) {
            this.currentWorkloadTask.cancel();
            this.currentWorkloadTask = null;
        }
        HandlerList.unregisterAll((Listener)this);
        activePregenerators.remove(this);
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBuildPlace(BuildPlaceEvent event) {
        if (event.getFitAnything().getLocation().getWorld().equals((Object)this.world)) {
            ++this.structuresGeneratedTotal;
            ++this.structuresGeneratedLast30s;
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.getWorld().equals((Object)this.world)) {
            return;
        }
        String chunkKey = event.getChunk().getX() + "," + event.getChunk().getZ();
        if (this.generatedChunks.contains(chunkKey)) {
            if (!this.loadedChunks.contains(chunkKey)) {
                this.loadedChunks.add(chunkKey);
                ++this.allChunksLoadedLast30s;
            }
            if (event.isNewChunk()) {
                ++this.newlyGeneratedChunks;
                ++this.chunksLoadedLast30s;
            }
        }
    }

    private void reportStats() {
        int chunksProcessed = this.generatedChunks.size();
        double tps = this.getTPS();
        if (this.expectedChunksTotal == null) {
            this.expectedChunksTotal = this.calculateExpectedChunksTotal();
        }
        String estimatedTimeLeft = this.calculateEstimatedTimeLeft(chunksProcessed, this.expectedChunksTotal, this.allChunksLoadedLast30s);
        Logger.info("=== Pregeneration Stats ===");
        if (this.isPaused) {
            Logger.info("Status: PAUSED (waiting for TPS to recover)");
        }
        Logger.info("Chunks Processed: " + chunksProcessed + " / " + this.expectedChunksTotal);
        Logger.info("Chunks Newly Generated (Total): " + this.newlyGeneratedChunks);
        Logger.info("New Chunks Loaded (Last 30s): " + this.chunksLoadedLast30s);
        Logger.info("Current TPS: " + String.format("%.2f", tps));
        Logger.info("Generation Settings:");
        Logger.info("  - Shape: " + this.shape);
        Logger.info("  - Tick Usage: " + String.format("%.1f", this.tickUsage * 100.0) + "%");
        Logger.info("  - Target Radius: " + this.maxRadiusBlocks + " blocks (" + this.maxRadiusChunks + " chunks)");
        Logger.info("  - Current Chunk Radius: " + this.currentRadius + " / " + this.maxRadiusChunks + " (" + this.currentRadius * 16 + " / " + this.maxRadiusBlocks + " blocks)");
        Logger.info("  - Max Chunk Radius Reached: " + this.actualMaxRadiusChunks);
        Logger.info("Structures Generated (Total): " + this.structuresGeneratedTotal);
        Logger.info("Structures Generated (Last 30s): " + this.structuresGeneratedLast30s);
        if (estimatedTimeLeft != null) {
            Logger.info("Estimated Time Remaining: " + estimatedTimeLeft);
        }
        Logger.info("===========================");
        this.chunksLoadedLast30s = 0;
        this.allChunksLoadedLast30s = 0;
        this.structuresGeneratedLast30s = 0;
    }

    private String calculateEstimatedTimeLeft(int chunksLoaded, int expectedChunksTotal, int chunksLoadedLast30s) {
        if (chunksLoadedLast30s == 0 || expectedChunksTotal == 0) {
            return null;
        }
        int remainingChunks = expectedChunksTotal - chunksLoaded;
        if (remainingChunks <= 0) {
            return "Complete";
        }
        double chunksPerSecond = (double)chunksLoadedLast30s / 30.0;
        if (chunksPerSecond <= 0.0) {
            return null;
        }
        long estimatedSeconds = Math.round((double)remainingChunks / chunksPerSecond);
        return this.formatTime(estimatedSeconds);
    }

    private String formatTime(long seconds) {
        if (seconds < 60L) {
            return seconds + " second" + (seconds != 1L ? "s" : "");
        }
        if (seconds < 3600L) {
            long minutes = seconds / 60L;
            long remainingSeconds = seconds % 60L;
            if (remainingSeconds == 0L) {
                return minutes + " minute" + (minutes != 1L ? "s" : "");
            }
            return minutes + " minute" + (minutes != 1L ? "s" : "") + " " + remainingSeconds + " second" + (remainingSeconds != 1L ? "s" : "");
        }
        long hours = seconds / 3600L;
        long remainingMinutes = seconds % 3600L / 60L;
        if (remainingMinutes == 0L) {
            return hours + " hour" + (hours != 1L ? "s" : "");
        }
        return hours + " hour" + (hours != 1L ? "s" : "") + " " + remainingMinutes + " minute" + (remainingMinutes != 1L ? "s" : "");
    }

    private int calculateExpectedChunksTotal() {
        try {
            HashSet<CallSite> chunksToGenerate = new HashSet<CallSite>();
            if ("SQUARE".equalsIgnoreCase(this.shape)) {
                for (int radius = 0; radius <= this.maxRadiusChunks; ++radius) {
                    for (int x = this.centerChunkX - radius; x <= this.centerChunkX + radius; ++x) {
                        chunksToGenerate.add((CallSite)((Object)(x + "," + (this.centerChunkZ - radius))));
                        chunksToGenerate.add((CallSite)((Object)(x + "," + (this.centerChunkZ + radius))));
                    }
                    for (int z = this.centerChunkZ - radius + 1; z < this.centerChunkZ + radius; ++z) {
                        chunksToGenerate.add((CallSite)((Object)(this.centerChunkX - radius + "," + z)));
                        chunksToGenerate.add((CallSite)((Object)(this.centerChunkX + radius + "," + z)));
                    }
                }
            } else if ("CIRCLE".equalsIgnoreCase(this.shape)) {
                for (int radius = 0; radius <= this.maxRadiusChunks; ++radius) {
                    int radiusSquared = radius * radius;
                    int nextRadiusSquared = (radius + 1) * (radius + 1);
                    for (int x = this.centerChunkX - radius - 1; x <= this.centerChunkX + radius + 1; ++x) {
                        for (int z = this.centerChunkZ - radius - 1; z <= this.centerChunkZ + radius + 1; ++z) {
                            int dx = x - this.centerChunkX;
                            int dz = z - this.centerChunkZ;
                            int distanceSquared = dx * dx + dz * dz;
                            if (distanceSquared < radiusSquared || distanceSquared >= nextRadiusSquared) continue;
                            chunksToGenerate.add((CallSite)((Object)(x + "," + z)));
                        }
                    }
                }
            }
            return chunksToGenerate.size();
        } catch (Exception e) {
            Logger.warn("Failed to calculate expected chunks total: " + e.getMessage());
            return 0;
        }
    }

    private double getTPS() {
        try {
            Server server = Bukkit.getServer();
            Method getTPSMethod = server.getClass().getMethod("getTPS", new Class[0]);
            double[] tps = (double[])getTPSMethod.invoke(server, new Object[0]);
            if (tps != null && tps.length > 0) {
                return Math.min(20.0, Math.max(0.0, tps[0]));
            }
        } catch (Exception exception) {
            // empty catch block
        }
        return 20.0;
    }

    private void setWorldBorder() {
        try {
            double borderSize = (double)this.maxRadiusBlocks * 2.0;
            this.world.getWorldBorder().setCenter(this.center.getX(), this.center.getZ());
            this.world.getWorldBorder().setSize(borderSize);
            Logger.info("World border set to size: " + borderSize + " blocks (radius: " + this.maxRadiusBlocks + "), center: (" + this.center.getX() + ", " + this.center.getZ() + ")");
        } catch (Exception e) {
            Logger.warn("Failed to set world border: " + e.getMessage());
        }
    }

    public World getWorld() {
        return this.world;
    }

    public Location getCenter() {
        return this.center;
    }

    public String getShape() {
        return this.shape;
    }
}

