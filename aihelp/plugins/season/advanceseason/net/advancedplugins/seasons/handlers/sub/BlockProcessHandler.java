/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.World
 *  org.bukkit.block.Biome
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.data.Levelled
 *  org.bukkit.entity.Player
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.BoundingBox
 *  org.bukkit.util.Vector
 *  org.bukkit.util.VoxelShape
 */
package net.advancedplugins.seasons.handlers.sub;

import com.google.common.collect.ImmutableList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.ReallyFastBlockHandler;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.GriefDefenderHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ResidenceHook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomeDescriptor;
import net.advancedplugins.seasons.biomes.BiomeUtils;
import net.advancedplugins.seasons.biomes.SeasonData;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.handlers.grass.GrassPatternsUtil;
import net.advancedplugins.seasons.handlers.sub.CustomChunkData;
import net.advancedplugins.seasons.handlers.sub.SnowPatternUtil;
import net.advancedplugins.seasons.objects.CustomWorld;
import net.advancedplugins.seasons.utils.LocalChunkSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;

public class BlockProcessHandler
extends DataHandler {
    private final HashMap<String, Queue<Pair<Location, SeasonType>>> locationQueue = new HashMap();
    private BukkitTask snowGenerator;
    private BukkitTask locationScanner;
    private final ConcurrentHashMap<String, LocalChunkSnapshot> snapshotCache = new ConcurrentHashMap();
    private final int chunkScanRadius;
    private final List<Pair<Integer, Integer>> randomizedChunks;
    private ReallyFastBlockHandler handler = null;
    private static NamespacedKey KEY_CHUNK_STAGE;
    private static NamespacedKey KEY_CHUNK_STAGE_SEASON;
    private final ImmutableList<String> materialBlacklist = ((ImmutableList.Builder)ImmutableList.builder().add(new String[]{"STAIRS", "FENCE", "DIRT_PATH", "ICE"})).build();
    private final boolean checkProtection;
    private final HashMap<String, CustomWorld> worldData = new HashMap();
    private boolean lock = false;

    public BlockProcessHandler(JavaPlugin javaPlugin) {
        super("blocks", javaPlugin);
        this.chunkScanRadius = javaPlugin.getConfig().getInt("chunkRadius", 7);
        this.randomizedChunks = this.generateRandomizedChunkCoordinates();
        KEY_CHUNK_STAGE = new NamespacedKey((Plugin)javaPlugin, "chunk_stage");
        KEY_CHUNK_STAGE_SEASON = new NamespacedKey((Plugin)javaPlugin, "chunk_stage_season");
        boolean bl = this.checkProtection = !javaPlugin.getConfig().getBoolean("ignoreLandProtection", false);
        if (javaPlugin.getConfig().getBoolean("snowEnabled", true)) {
            this.loadWorlds();
            this.initScanner();
            this.initGenerator();
            Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)javaPlugin, this::cleanupOldSnapshots, 20L, 20L);
            if (this.locationScanner != null) {
                this.addTask(this.locationScanner.getTaskId());
            }
            if (this.snowGenerator != null) {
                this.addTask(this.snowGenerator.getTaskId());
            }
        }
    }

    private void loadWorlds() {
        for (String string : this.getKeys("worlds")) {
            boolean bl = this.getBoolean("worlds." + string + ".generateCustomGrass");
            this.worldData.put(string, new CustomWorld(string, bl));
        }
    }

    private List<Pair<Integer, Integer>> generateRandomizedChunkCoordinates() {
        ArrayList<Pair<Integer, Integer>> arrayList = new ArrayList<Pair<Integer, Integer>>();
        for (int i = -this.chunkScanRadius; i <= this.chunkScanRadius; ++i) {
            for (int j = -this.chunkScanRadius; j <= this.chunkScanRadius; ++j) {
                arrayList.add(new Pair<Integer, Integer>(i, j));
            }
        }
        return arrayList;
    }

    private void initGenerator() {
        this.snowGenerator = new BukkitRunnable(){

            public void run() {
                for (String string2 : Core.getWorldHandler().getEnabledWorlds()) {
                    World world = Bukkit.getWorld((String)string2);
                    if (world == null || BlockProcessHandler.this.locationQueue.computeIfAbsent(world.getName(), string -> new ArrayDeque()).isEmpty() || !Core.getWorldHandler().isWorldEnabled(world.getName())) continue;
                    SeasonType seasonType = Core.getSeasonHandler().getSeason(world.getName()).getType();
                    BlockProcessHandler.this.handler = ReallyFastBlockHandler.getForWorld(world);
                    EnumMap<Material, HashSet> enumMap = new EnumMap<Material, HashSet>(Material.class);
                    SeasonType seasonType2 = Core.getSeasonHandler().getSeason(world.getName()).getType();
                    Queue<Pair<Location, SeasonType>> queue = BlockProcessHandler.this.locationQueue.get(world.getName());
                    int n = queue.size() < 100 ? queue.size() : queue.size() / 3;
                    for (int i = 0; i < n; ++i) {
                        Block block;
                        Block block2;
                        Pair<Material, Block> pair;
                        Location location;
                        Pair<Location, SeasonType> pair2 = queue.remove();
                        if (!pair2.getValue().equals((Object)seasonType) || (location = pair2.getKey()) == null || !location.getChunk().isLoaded() || (pair = BlockProcessHandler.this.handleBlock(block2 = location.getBlock(), block = block2.getRelative(0, 1, 0), seasonType2)) == null) continue;
                        enumMap.computeIfAbsent(pair.getKey(), material -> new HashSet()).add(pair.getValue());
                    }
                    for (Map.Entry entry : enumMap.entrySet()) {
                        BlockProcessHandler.this.handler.setType(entry.getKey(), ((HashSet)entry.getValue()).toArray(new Block[0]));
                    }
                }
            }
        }.runTaskTimer((Plugin)Core.getInstance(), 2L, 2L);
    }

    private void initScanner() {
        BukkitRunnable bukkitRunnable = new BukkitRunnable(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void run() {
                if (BlockProcessHandler.this.lock) {
                    ASManager.debug("\u00a7c[AdvancedSeasons] skipping block processing, server behind ticks.");
                    return;
                }
                BlockProcessHandler.this.lock = true;
                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName())) continue;
                        SeasonType seasonType = Core.getSeasonHandler().getSeason(player.getWorld().getName()).getType();
                        boolean bl = seasonType.equals((Object)SeasonType.WINTER);
                        boolean bl2 = seasonType.equals((Object)SeasonType.SPRING);
                        if (bl && player.getWorld().isClearWeather()) continue;
                        Chunk chunk = player.getLocation().getChunk();
                        for (Pair<Integer, Integer> pair : BlockProcessHandler.this.randomizedChunks) {
                            int n;
                            LocalChunkSnapshot localChunkSnapshot;
                            boolean bl3;
                            int n2;
                            int n3 = pair.getKey();
                            int n4 = pair.getValue();
                            int n5 = chunk.getX() + n3;
                            Chunk chunk2 = BlockProcessHandler.this.getChunk(chunk, n5, n2 = chunk.getZ() + n4);
                            if (chunk2 == null) continue;
                            int n6 = (Integer)chunk2.getPersistentDataContainer().getOrDefault(KEY_CHUNK_STAGE, PersistentDataType.INTEGER, (Object)1);
                            String string2 = (String)chunk2.getPersistentDataContainer().getOrDefault(KEY_CHUNK_STAGE_SEASON, PersistentDataType.STRING, (Object)"");
                            if (n6 > 16 && string2.equals(seasonType.name())) continue;
                            String string3 = n5 + ";" + n2 + ";" + chunk.getWorld().getName();
                            boolean bl4 = bl3 = string2.equals(SeasonType.WINTER.name()) && seasonType.equals((Object)SeasonType.SUMMER);
                            if (!string2.equals(seasonType.name())) {
                                chunk2.getPersistentDataContainer().set(KEY_CHUNK_STAGE, PersistentDataType.INTEGER, (Object)1);
                                chunk2.getPersistentDataContainer().set(KEY_CHUNK_STAGE_SEASON, PersistentDataType.STRING, (Object)seasonType.name());
                                n6 = 1;
                            }
                            if ((localChunkSnapshot = BlockProcessHandler.this.snapshotCache.computeIfAbsent(string3, string -> BlockProcessHandler.this.getSnapshot(n5, n2, chunk2))) == null) continue;
                            if (bl || bl2) {
                                for (Vector vector : SnowPatternUtil.getOffsetsForStage(n6)) {
                                    if (!BlockProcessHandler.this.processChunkScan(chunk, n5, n2, localChunkSnapshot, vector, seasonType)) continue;
                                }
                                n = n6 + 1;
                            } else {
                                int n7 = 0;
                                int n8 = 0;
                                for (Vector vector : SnowPatternUtil.MELT_STATE_TO_OFFSETS_MAP.get(1)) {
                                    ++n7;
                                    if (!BlockProcessHandler.this.processChunkScan(chunk, n5, n2, localChunkSnapshot, vector, seasonType)) continue;
                                    ++n8;
                                }
                                n = 17;
                                if (bl3) {
                                    // empty if block
                                }
                            }
                            chunk2.getPersistentDataContainer().set(KEY_CHUNK_STAGE, PersistentDataType.INTEGER, (Object)n);
                            if (n != 16) continue;
                            BlockProcessHandler.this.snapshotCache.remove(string3);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    BlockProcessHandler.this.lock = false;
                }
            }
        };
        this.locationScanner = MinecraftVersion.isPaper() ? bukkitRunnable.runTaskTimerAsynchronously((Plugin)Core.getInstance(), 20L, 20L) : bukkitRunnable.runTaskTimer((Plugin)Core.getInstance(), 40L, 40L);
    }

    private LocalChunkSnapshot getSnapshot(int n, int n2, Chunk chunk) {
        try {
            return new LocalChunkSnapshot(n, n2, chunk, chunk.getWorld());
        } catch (Exception exception) {
            return null;
        }
    }

    private boolean processChunkScan(Chunk chunk, int n, int n2, LocalChunkSnapshot localChunkSnapshot, Vector vector, SeasonType seasonType) {
        CustomChunkData.BlockData blockData = localChunkSnapshot.getChunkSnapshot().getHighestBlockData(vector.getBlockX(), vector.getBlockZ());
        if (blockData == null) {
            return false;
        }
        Location location = new Location(chunk.getWorld(), (double)(n * 16 + vector.getBlockX()), (double)blockData.getHighestY(), (double)(n2 * 16 + vector.getBlockZ()));
        BiomeDescriptor biomeDescriptor = BiomeDescriptor.fromLocation(location);
        boolean bl = Core.getBiomesHandler().getAdvancedBiome(biomeDescriptor).isEmpty();
        if (bl) {
            return false;
        }
        boolean bl2 = this.shouldAddToLocGen(blockData.getMaterial(), blockData.getMaterialAbove(), biomeDescriptor, seasonType);
        if (!bl2) {
            return true;
        }
        this.locationQueue.computeIfAbsent(chunk.getWorld().getName(), string -> new ArrayDeque()).add(new Pair<Location, SeasonType>(location, seasonType));
        return false;
    }

    private boolean shouldAddToLocGen(Material material, Material material2, BiomeDescriptor biomeDescriptor, SeasonType seasonType) {
        String string = biomeDescriptor.upperCaseName();
        return Core.getBiomesHandler().getAdvancedBiome(biomeDescriptor).map(advancedBiomeBase -> {
            if (seasonType == SeasonType.WINTER) {
                if (BiomeUtils.getSnowyBiomes().contains(string)) {
                    return false;
                }
                SeasonData seasonData = advancedBiomeBase.getSeasons().get((Object)SeasonType.WINTER);
                if (material.equals((Object)Material.WATER)) {
                    return seasonData.getWinterFreeze();
                }
                return seasonData.isSnow() && !material2.equals((Object)Material.SNOW) && this.isAllowedOnTop(material.name()) && this.isMaterialCorrect(material);
            }
            if (BiomeUtils.getNoSnowBiomes().contains(biomeDescriptor.bukkitBiome()) || BiomeUtils.getSnowyBiomes().contains(string)) {
                return false;
            }
            if (material.equals((Object)Material.ICE) || material.equals((Object)Material.PACKED_ICE)) {
                return true;
            }
            return true;
        }).orElse(false);
    }

    private Pair<Material, Block> handleBlock(Block block, Block block2, SeasonType seasonType) {
        if (this.checkProtection && Core.getProtectionHandler().isProtected(block2.getLocation())) {
            return null;
        }
        switch (seasonType) {
            case WINTER: {
                if (block.getLightFromBlocks() >= 12) {
                    return null;
                }
                if (block.getType().equals((Object)Material.WATER)) {
                    if (HooksHandler.getHook(HookPlugin.RESIDENCE) != null && ((ResidenceHook)HooksHandler.getHook(HookPlugin.RESIDENCE)).isProtected(block.getLocation())) {
                        return null;
                    }
                    if (HooksHandler.getHook(HookPlugin.GRIEFDEFENDER) != null && !((GriefDefenderHook)HooksHandler.getHook(HookPlugin.GRIEFDEFENDER)).canIceGenerate(block.getLocation())) {
                        return null;
                    }
                    if (Core.getBiomesHandler().isFrozen(BiomeDescriptor.fromBlock(block), seasonType) && ((Levelled)block.getBlockData()).getLevel() == 0) {
                        boolean bl = Arrays.stream(BlockFace.values()).anyMatch(blockFace -> !block.getRelative(blockFace).getType().equals((Object)Material.WATER) && !block.getRelative(blockFace).getType().equals((Object)Material.AIR) && !block.getRelative(blockFace).getType().equals((Object)Material.ICE) && !block.getRelative(blockFace).getType().equals((Object)Material.PACKED_ICE));
                        if (bl) {
                            return new Pair<Material, Block>(Material.PACKED_ICE, block);
                        }
                        return new Pair<Material, Block>(Material.ICE, block);
                    }
                }
                if (block.getType().isTransparent() || block.isLiquid()) {
                    return null;
                }
                if (!this.isCube(block)) {
                    return null;
                }
                if (!this.isMaterialCorrect(block.getType())) {
                    return null;
                }
                if (!(block2.getType().equals((Object)Material.AIR) || ASManager.isVegetation(block2.getType()) && this.canGenerate(block2.getWorld().getName(), block2.getType()))) {
                    return null;
                }
                return new Pair<Material, Block>(Material.SNOW, block2);
            }
        }
        Biome biome = block.getBiome();
        if (BiomeUtils.getNoSnowBiomes().contains(biome) || BiomeUtils.getSnowyBiomes().contains(biome.name())) {
            return null;
        }
        if (block.getType().equals((Object)Material.ICE) || block.getType().equals((Object)Material.PACKED_ICE)) {
            return new Pair<Material, Block>(Material.WATER, block);
        }
        if (block2.getType().equals((Object)Material.SNOW)) {
            if (!block.getType().equals((Object)Material.GRASS_BLOCK)) {
                return new Pair<Material, Block>(Material.AIR, block2);
            }
            Material material = GrassPatternsUtil.process(block2);
            if (material == null) {
                return null;
            }
            return new Pair<Material, Block>(material, block2);
        }
        return null;
    }

    private boolean canGenerate(String string, Material material) {
        if (!this.worldData.containsKey(string)) {
            return true;
        }
        return this.worldData.get(string).isGenerateCustomGrass();
    }

    private boolean isCube(Block block) {
        VoxelShape voxelShape = block.getCollisionShape();
        BoundingBox boundingBox = block.getBoundingBox();
        return voxelShape.getBoundingBoxes().size() == 1 && boundingBox.getWidthX() == 1.0 && boundingBox.getHeight() == 1.0 && boundingBox.getWidthZ() == 1.0;
    }

    private boolean isAllowedOnTop(String string) {
        for (String string2 : this.materialBlacklist) {
            if (!string.contains(string2)) continue;
            return false;
        }
        return true;
    }

    private boolean isMaterialCorrect(Material material) {
        if (!material.isSolid()) {
            return false;
        }
        if (material.isTransparent()) {
            return false;
        }
        return !material.equals((Object)Material.SNOW);
    }

    public void resetLocations(String string2) {
        this.locationQueue.computeIfAbsent(string2, string -> new ArrayDeque()).removeIf(pair -> ((Location)pair.getKey()).getWorld().getName().equals(string2));
        this.cleanupOldSnapshots();
    }

    private Chunk getChunk(Chunk chunk, int n, int n2) {
        if (MinecraftVersion.isPaper()) {
            try {
                return (Chunk)chunk.getWorld().getChunkAtAsync(n, n2).get();
            } catch (Exception exception) {
                return null;
            }
        }
        return chunk.getWorld().getChunkAt(n, n2);
    }

    private void cleanupOldSnapshots() {
        Iterator<Map.Entry<String, LocalChunkSnapshot>> iterator = this.snapshotCache.entrySet().iterator();
        ASManager.debug("chunk size: " + this.snapshotCache.size() + " ");
        long l = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Map.Entry<String, LocalChunkSnapshot> entry = iterator.next();
            LocalChunkSnapshot localChunkSnapshot = entry.getValue();
            if (l - localChunkSnapshot.getLastGet() <= 2000L) continue;
            iterator.remove();
        }
    }

    public void stopTasks() {
        try {
            Bukkit.getServer().getScheduler().cancelTask(this.locationScanner.getTaskId());
        } catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.getServer().getScheduler().cancelTask(this.snowGenerator.getTaskId());
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public static NamespacedKey getKEY_CHUNK_STAGE() {
        return KEY_CHUNK_STAGE;
    }

    public static NamespacedKey getKEY_CHUNK_STAGE_SEASON() {
        return KEY_CHUNK_STAGE_SEASON;
    }
}

