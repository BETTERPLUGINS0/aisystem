/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Registry
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Biome
 *  org.bukkit.block.Block
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import me.zombie_striker.qav.util.xseries.base.XModule;
import me.zombie_striker.qav.util.xseries.base.XRegistry;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class XBiome
extends XModule<XBiome, Biome> {
    public static final XRegistry<XBiome, Biome> REGISTRY = new XRegistry<XBiome, Biome>(Biome.class, XBiome.class, () -> Registry.BIOME, XBiome::new, XBiome[]::new);
    public static final XBiome WINDSWEPT_HILLS = XBiome.std("WINDSWEPT_HILLS", "MOUNTAINS", "EXTREME_HILLS");
    public static final XBiome SNOWY_PLAINS = XBiome.std("SNOWY_PLAINS", "SNOWY_TUNDRA", "ICE_FLATS", "ICE_PLAINS");
    public static final XBiome SPARSE_JUNGLE = XBiome.std("SPARSE_JUNGLE", "JUNGLE_EDGE", "JUNGLE_EDGE");
    public static final XBiome STONY_SHORE = XBiome.std("STONY_SHORE", "STONE_SHORE", "STONE_BEACH");
    public static final XBiome CHERRY_GROVE = XBiome.std("CHERRY_GROVE");
    public static final XBiome PALE_GARDEN = XBiome.std("PALE_GARDEN");
    public static final XBiome OLD_GROWTH_PINE_TAIGA = XBiome.std("OLD_GROWTH_PINE_TAIGA", "GIANT_TREE_TAIGA", "REDWOOD_TAIGA", "MEGA_TAIGA");
    public static final XBiome WINDSWEPT_FOREST = XBiome.std("WINDSWEPT_FOREST", "WOODED_MOUNTAINS", "EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_PLUS");
    public static final XBiome WOODED_BADLANDS = XBiome.std("WOODED_BADLANDS", "WOODED_BADLANDS_PLATEAU", "MESA_ROCK", "MESA_PLATEAU_FOREST");
    public static final XBiome WINDSWEPT_GRAVELLY_HILLS = XBiome.std("WINDSWEPT_GRAVELLY_HILLS", "GRAVELLY_MOUNTAINS", "MUTATED_EXTREME_HILLS", "EXTREME_HILLS_MOUNTAINS");
    public static final XBiome OLD_GROWTH_BIRCH_FOREST = XBiome.std("OLD_GROWTH_BIRCH_FOREST", "TALL_BIRCH_FOREST", "MUTATED_BIRCH_FOREST", "BIRCH_FOREST_MOUNTAINS");
    public static final XBiome OLD_GROWTH_SPRUCE_TAIGA = XBiome.std("OLD_GROWTH_SPRUCE_TAIGA", "GIANT_SPRUCE_TAIGA", "MUTATED_REDWOOD_TAIGA", "MEGA_SPRUCE_TAIGA");
    public static final XBiome WINDSWEPT_SAVANNA = XBiome.std("WINDSWEPT_SAVANNA", "SHATTERED_SAVANNA", "MUTATED_SAVANNA", "SAVANNA_MOUNTAINS");
    public static final XBiome MEADOW = XBiome.std("MEADOW");
    public static final XBiome MANGROVE_SWAMP = XBiome.std("MANGROVE_SWAMP");
    public static final XBiome DEEP_DARK = XBiome.std("DEEP_DARK");
    public static final XBiome GROVE = XBiome.std("GROVE");
    public static final XBiome SNOWY_SLOPES = XBiome.std("SNOWY_SLOPES");
    public static final XBiome FROZEN_PEAKS = XBiome.std("FROZEN_PEAKS");
    public static final XBiome JAGGED_PEAKS = XBiome.std("JAGGED_PEAKS");
    public static final XBiome STONY_PEAKS = XBiome.std("STONY_PEAKS");
    public static final XBiome BADLANDS = XBiome.std("BADLANDS", "MESA");
    public static final XBiome BADLANDS_PLATEAU = XBiome.std(WOODED_BADLANDS, "BADLANDS_PLATEAU", "MESA_CLEAR_ROCK", "MESA_PLATEAU");
    public static final XBiome BEACH = XBiome.std("BEACH", "BEACHES");
    public static final XBiome BIRCH_FOREST = XBiome.std(OLD_GROWTH_BIRCH_FOREST, "BIRCH_FOREST");
    public static final XBiome BIRCH_FOREST_HILLS = XBiome.std(OLD_GROWTH_BIRCH_FOREST, "BIRCH_FOREST_HILLS");
    public static final XBiome COLD_OCEAN = XBiome.std("COLD_OCEAN");
    public static final XBiome DARK_FOREST = XBiome.std("DARK_FOREST", "ROOFED_FOREST");
    public static final XBiome DARK_FOREST_HILLS = XBiome.std("DARK_FOREST_HILLS", "MUTATED_ROOFED_FOREST", "ROOFED_FOREST_MOUNTAINS");
    public static final XBiome DEEP_COLD_OCEAN = XBiome.std("DEEP_COLD_OCEAN", "COLD_DEEP_OCEAN");
    public static final XBiome DEEP_FROZEN_OCEAN = XBiome.std("DEEP_FROZEN_OCEAN", "FROZEN_DEEP_OCEAN");
    public static final XBiome DEEP_LUKEWARM_OCEAN = XBiome.std("DEEP_LUKEWARM_OCEAN", "LUKEWARM_DEEP_OCEAN");
    public static final XBiome DEEP_OCEAN = XBiome.std("DEEP_OCEAN");
    public static final XBiome DEEP_WARM_OCEAN = XBiome.std("DEEP_WARM_OCEAN", "WARM_DEEP_OCEAN");
    public static final XBiome DESERT = XBiome.std("DESERT");
    public static final XBiome DESERT_HILLS = XBiome.std("DESERT_HILLS");
    public static final XBiome DESERT_LAKES = XBiome.std("DESERT_LAKES", "MUTATED_DESERT", "DESERT_MOUNTAINS");
    public static final XBiome END_BARRENS = XBiome.std(World.Environment.THE_END, "END_BARRENS", "SKY_ISLAND_BARREN");
    public static final XBiome END_HIGHLANDS = XBiome.std(World.Environment.THE_END, "END_HIGHLANDS", "SKY_ISLAND_HIGH");
    public static final XBiome END_MIDLANDS = XBiome.std(World.Environment.THE_END, "END_MIDLANDS", "SKY_ISLAND_MEDIUM");
    public static final XBiome ERODED_BADLANDS = XBiome.std("ERODED_BADLANDS", "MUTATED_MESA", "MESA_BRYCE");
    public static final XBiome FLOWER_FOREST = XBiome.std("FLOWER_FOREST", "MUTATED_FOREST");
    public static final XBiome FOREST = XBiome.std("FOREST");
    public static final XBiome FROZEN_OCEAN = XBiome.std("FROZEN_OCEAN");
    public static final XBiome FROZEN_RIVER = XBiome.std("FROZEN_RIVER");
    public static final XBiome GIANT_SPRUCE_TAIGA = XBiome.std(OLD_GROWTH_SPRUCE_TAIGA, "GIANT_SPRUCE_TAIGA", "MUTATED_REDWOOD_TAIGA", "MEGA_SPRUCE_TAIGA");
    public static final XBiome GIANT_SPRUCE_TAIGA_HILLS = XBiome.std(OLD_GROWTH_SPRUCE_TAIGA, "GIANT_SPRUCE_TAIGA_HILLS", "MUTATED_REDWOOD_TAIGA_HILLS", "MEGA_SPRUCE_TAIGA_HILLS");
    public static final XBiome GIANT_TREE_TAIGA = XBiome.std(OLD_GROWTH_PINE_TAIGA, "GIANT_TREE_TAIGA", "REDWOOD_TAIGA", "MEGA_TAIGA");
    public static final XBiome GIANT_TREE_TAIGA_HILLS = XBiome.std(OLD_GROWTH_PINE_TAIGA, "GIANT_TREE_TAIGA_HILLS", "REDWOOD_TAIGA_HILLS", "MEGA_TAIGA_HILLS");
    public static final XBiome ICE_SPIKES = XBiome.std("ICE_SPIKES", "MUTATED_ICE_FLATS", "ICE_PLAINS_SPIKES");
    public static final XBiome JUNGLE = XBiome.std("JUNGLE");
    public static final XBiome JUNGLE_HILLS = XBiome.std("JUNGLE_HILLS");
    public static final XBiome LUKEWARM_OCEAN = XBiome.std("LUKEWARM_OCEAN");
    public static final XBiome MODIFIED_BADLANDS_PLATEAU = XBiome.std(WOODED_BADLANDS, "MODIFIED_BADLANDS_PLATEAU", "MUTATED_MESA_CLEAR_ROCK", "MESA_PLATEAU");
    public static final XBiome MODIFIED_GRAVELLY_MOUNTAINS = XBiome.std(WINDSWEPT_GRAVELLY_HILLS, "MODIFIED_GRAVELLY_MOUNTAINS", "MUTATED_EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_MOUNTAINS");
    public static final XBiome MODIFIED_JUNGLE = XBiome.std("MODIFIED_JUNGLE", "MUTATED_JUNGLE", "JUNGLE_MOUNTAINS");
    public static final XBiome MODIFIED_JUNGLE_EDGE = XBiome.std(SPARSE_JUNGLE, "MODIFIED_JUNGLE_EDGE", "MUTATED_JUNGLE_EDGE", "JUNGLE_EDGE_MOUNTAINS");
    public static final XBiome MODIFIED_WOODED_BADLANDS_PLATEAU = XBiome.std(WOODED_BADLANDS, "MODIFIED_WOODED_BADLANDS_PLATEAU", "MUTATED_MESA_ROCK", "MESA_PLATEAU_FOREST_MOUNTAINS");
    public static final XBiome MOUNTAIN_EDGE = XBiome.std(SPARSE_JUNGLE, "MOUNTAIN_EDGE", "SMALLER_EXTREME_HILLS");
    public static final XBiome MUSHROOM_FIELDS = XBiome.std("MUSHROOM_FIELDS", "MUSHROOM_ISLAND");
    public static final XBiome MUSHROOM_FIELD_SHORE = XBiome.std(STONY_SHORE, "MUSHROOM_FIELD_SHORE", "MUSHROOM_ISLAND_SHORE", "MUSHROOM_SHORE");
    public static final XBiome SOUL_SAND_VALLEY = XBiome.std(World.Environment.NETHER, "SOUL_SAND_VALLEY");
    public static final XBiome CRIMSON_FOREST = XBiome.std(World.Environment.NETHER, "CRIMSON_FOREST");
    public static final XBiome WARPED_FOREST = XBiome.std(World.Environment.NETHER, "WARPED_FOREST");
    public static final XBiome BASALT_DELTAS = XBiome.std(World.Environment.NETHER, "BASALT_DELTAS");
    public static final XBiome NETHER_WASTES = XBiome.std(World.Environment.NETHER, "NETHER_WASTES", "NETHER", "HELL");
    public static final XBiome OCEAN = XBiome.std("OCEAN");
    public static final XBiome PLAINS = XBiome.std("PLAINS");
    public static final XBiome RIVER = XBiome.std("RIVER");
    public static final XBiome SAVANNA = XBiome.std("SAVANNA");
    public static final XBiome SAVANNA_PLATEAU = XBiome.std(WINDSWEPT_SAVANNA, "SAVANNA_ROCK", "SAVANNA_PLATEAU");
    public static final XBiome SHATTERED_SAVANNA_PLATEAU = XBiome.std(WINDSWEPT_SAVANNA, "SHATTERED_SAVANNA_PLATEAU", "MUTATED_SAVANNA_ROCK", "SAVANNA_PLATEAU_MOUNTAINS");
    public static final XBiome SMALL_END_ISLANDS = XBiome.std(World.Environment.THE_END, "SMALL_END_ISLANDS", "SKY_ISLAND_LOW");
    public static final XBiome SNOWY_BEACH = XBiome.std("SNOWY_BEACH", "COLD_BEACH");
    public static final XBiome SNOWY_MOUNTAINS = XBiome.std(WINDSWEPT_HILLS, "SNOWY_MOUNTAINS", "ICE_MOUNTAINS");
    public static final XBiome SNOWY_TAIGA = XBiome.std("SNOWY_TAIGA", "TAIGA_COLD", "COLD_TAIGA");
    public static final XBiome SNOWY_TAIGA_HILLS = XBiome.std("SNOWY_TAIGA_HILLS", "TAIGA_COLD_HILLS", "COLD_TAIGA_HILLS");
    public static final XBiome SNOWY_TAIGA_MOUNTAINS = XBiome.std(WINDSWEPT_FOREST, "SNOWY_TAIGA_MOUNTAINS", "MUTATED_TAIGA_COLD", "COLD_TAIGA_MOUNTAINS");
    public static final XBiome SUNFLOWER_PLAINS = XBiome.std("SUNFLOWER_PLAINS", "MUTATED_PLAINS");
    public static final XBiome SWAMP = XBiome.std("SWAMP", "SWAMPLAND");
    public static final XBiome SWAMP_HILLS = XBiome.std("SWAMP_HILLS", "MUTATED_SWAMPLAND", "SWAMPLAND_MOUNTAINS");
    public static final XBiome TAIGA = XBiome.std("TAIGA");
    public static final XBiome TAIGA_HILLS = XBiome.std("TAIGA_HILLS");
    public static final XBiome TAIGA_MOUNTAINS = XBiome.std(WINDSWEPT_FOREST, "TAIGA_MOUNTAINS", "MUTATED_TAIGA");
    public static final XBiome CUSTOM = XBiome.std("CUSTOM");
    public static final XBiome TALL_BIRCH_FOREST = XBiome.std(OLD_GROWTH_BIRCH_FOREST, "TALL_BIRCH_FOREST", "MUTATED_BIRCH_FOREST", "BIRCH_FOREST_MOUNTAINS");
    public static final XBiome TALL_BIRCH_HILLS = XBiome.std(OLD_GROWTH_BIRCH_FOREST, "TALL_BIRCH_HILLS", "MUTATED_BIRCH_FOREST_HILLS", "MESA_PLATEAU_FOREST_MOUNTAINS");
    public static final XBiome THE_END = XBiome.std(World.Environment.THE_END, "THE_END", "SKY");
    public static final XBiome THE_VOID = XBiome.std("THE_VOID", "VOID");
    public static final XBiome WARM_OCEAN = XBiome.std("WARM_OCEAN");
    public static final XBiome WOODED_BADLANDS_PLATEAU = XBiome.std("WOODED_BADLANDS_PLATEAU", "MESA_ROCK", "MESA_PLATEAU_FOREST");
    public static final XBiome WOODED_HILLS = XBiome.std("WOODED_HILLS", "FOREST_HILLS");
    public static final XBiome WOODED_MOUNTAINS = XBiome.std("WOODED_MOUNTAINS", "EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_PLUS");
    public static final XBiome BAMBOO_JUNGLE = XBiome.std("BAMBOO_JUNGLE");
    public static final XBiome BAMBOO_JUNGLE_HILLS = XBiome.std("BAMBOO_JUNGLE_HILLS");
    public static final XBiome DRIPSTONE_CAVES = XBiome.std("DRIPSTONE_CAVES");
    public static final XBiome LUSH_CAVES = XBiome.std("LUSH_CAVES");
    private static final boolean World_getMaxHeight$SUPPORTED;
    private static final boolean World_getMinHeight$SUPPORTED;
    @Nullable
    private final World.Environment environment;

    public XBiome(World.Environment environment, Biome biome, String[] stringArray) {
        super(biome, stringArray);
        this.environment = environment;
    }

    private XBiome(Biome biome, String[] stringArray) {
        this(null, biome, stringArray);
    }

    public Optional<World.Environment> getEnvironment() {
        return Optional.ofNullable(this.environment);
    }

    @Deprecated
    @Nullable
    public Biome getBiome() {
        return (Biome)this.get();
    }

    @NotNull
    public CompletableFuture<Void> setBiome(@NotNull Chunk chunk) {
        Biome biome = (Biome)this.get();
        Objects.requireNonNull(biome, () -> "Unsupported biome: " + this.name());
        Objects.requireNonNull(chunk, "Cannot set biome of null chunk");
        if (!chunk.isLoaded() && !chunk.load(true)) {
            throw new IllegalStateException("Could not load chunk at " + chunk.getX() + ", " + chunk.getZ());
        }
        int n = World_getMaxHeight$SUPPORTED ? chunk.getWorld().getMaxHeight() : 1;
        int n2 = World_getMinHeight$SUPPORTED ? chunk.getWorld().getMinHeight() : 0;
        return CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 16; ++i) {
                for (int j = n2; j < n; j += 4) {
                    for (int k = 0; k < 16; ++k) {
                        Block block = chunk.getBlock(i, j, k);
                        if (block.getBiome() == biome) continue;
                        block.setBiome(biome);
                    }
                }
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @NotNull
    public CompletableFuture<Void> setBiome(@NotNull Location location, @NotNull Location location2) {
        Biome biome = (Biome)this.get();
        Objects.requireNonNull(location, "Start location cannot be null");
        Objects.requireNonNull(location2, "End location cannot be null");
        Objects.requireNonNull(biome, () -> "Unsupported biome: " + this.name());
        World world = location.getWorld();
        if (!world.getUID().equals(location2.getWorld().getUID())) {
            throw new IllegalArgumentException("Location worlds mismatch");
        }
        int n = World_getMaxHeight$SUPPORTED ? world.getMaxHeight() : 1;
        int n2 = World_getMinHeight$SUPPORTED ? world.getMinHeight() : 0;
        return CompletableFuture.runAsync(() -> {
            for (int i = location.getBlockX(); i < location2.getBlockX(); ++i) {
                for (int j = n2; j < n; j += 4) {
                    for (int k = location.getBlockZ(); k < location2.getBlockZ(); ++k) {
                        Block block = new Location(world, (double)i, (double)j, (double)k).getBlock();
                        if (block.getBiome() == biome) continue;
                        block.setBiome(biome);
                    }
                }
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @NotNull
    public static XBiome of(@NotNull Biome biome) {
        return REGISTRY.getByBukkitForm(biome);
    }

    public static Optional<XBiome> of(@NotNull String string) {
        return REGISTRY.getByName(string);
    }

    @Deprecated
    public static XBiome[] values() {
        return (XBiome[])REGISTRY.values();
    }

    @NotNull
    public static @Unmodifiable Collection<XBiome> getValues() {
        return REGISTRY.getValues();
    }

    @NotNull
    private static XBiome std(@NotNull World.Environment environment, @NotNull String ... stringArray) {
        return REGISTRY.std(biome -> new XBiome(environment, (Biome)biome, stringArray), stringArray);
    }

    @NotNull
    private static XBiome std(@Nullable XBiome xBiome, @NotNull String ... stringArray) {
        return REGISTRY.std(biome -> new XBiome(null, (Biome)biome, stringArray), xBiome, stringArray);
    }

    @NotNull
    private static XBiome std(@NotNull String ... stringArray) {
        return REGISTRY.std(biome -> new XBiome(World.Environment.NORMAL, (Biome)biome, stringArray), stringArray);
    }

    static {
        boolean bl = false;
        boolean bl2 = false;
        try {
            World.class.getMethod("getMaxHeight", new Class[0]);
            bl = true;
        } catch (Exception exception) {
            // empty catch block
        }
        try {
            World.class.getMethod("getMinHeight", new Class[0]);
            bl2 = true;
        } catch (Exception exception) {
            // empty catch block
        }
        World_getMaxHeight$SUPPORTED = bl;
        World_getMinHeight$SUPPORTED = bl2;
    }
}

