package me.PM2.infinitevehicles.xseries;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import me.PM2.infinitevehicles.xseries.base.XModule;
import me.PM2.infinitevehicles.xseries.base.XRegistry;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class XBiome extends XModule<XBiome, Biome> {
   public static final XRegistry<XBiome, Biome> REGISTRY = new XRegistry(Biome.class, XBiome.class, () -> {
      return Registry.BIOME;
   }, XBiome::new, (var0x) -> {
      return new XBiome[var0x];
   });
   public static final XBiome WINDSWEPT_HILLS = std("WINDSWEPT_HILLS", "MOUNTAINS", "EXTREME_HILLS");
   public static final XBiome SNOWY_PLAINS = std("SNOWY_PLAINS", "SNOWY_TUNDRA", "ICE_FLATS", "ICE_PLAINS");
   public static final XBiome SPARSE_JUNGLE = std("SPARSE_JUNGLE", "JUNGLE_EDGE", "JUNGLE_EDGE");
   public static final XBiome STONY_SHORE = std("STONY_SHORE", "STONE_SHORE", "STONE_BEACH");
   public static final XBiome CHERRY_GROVE = std("CHERRY_GROVE");
   public static final XBiome PALE_GARDEN = std("PALE_GARDEN");
   public static final XBiome OLD_GROWTH_PINE_TAIGA = std("OLD_GROWTH_PINE_TAIGA", "GIANT_TREE_TAIGA", "REDWOOD_TAIGA", "MEGA_TAIGA");
   public static final XBiome WINDSWEPT_FOREST = std("WINDSWEPT_FOREST", "WOODED_MOUNTAINS", "EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_PLUS");
   public static final XBiome WOODED_BADLANDS = std("WOODED_BADLANDS", "WOODED_BADLANDS_PLATEAU", "MESA_ROCK", "MESA_PLATEAU_FOREST");
   public static final XBiome WINDSWEPT_GRAVELLY_HILLS = std("WINDSWEPT_GRAVELLY_HILLS", "GRAVELLY_MOUNTAINS", "MUTATED_EXTREME_HILLS", "EXTREME_HILLS_MOUNTAINS");
   public static final XBiome OLD_GROWTH_BIRCH_FOREST = std("OLD_GROWTH_BIRCH_FOREST", "TALL_BIRCH_FOREST", "MUTATED_BIRCH_FOREST", "BIRCH_FOREST_MOUNTAINS");
   public static final XBiome OLD_GROWTH_SPRUCE_TAIGA = std("OLD_GROWTH_SPRUCE_TAIGA", "GIANT_SPRUCE_TAIGA", "MUTATED_REDWOOD_TAIGA", "MEGA_SPRUCE_TAIGA");
   public static final XBiome WINDSWEPT_SAVANNA = std("WINDSWEPT_SAVANNA", "SHATTERED_SAVANNA", "MUTATED_SAVANNA", "SAVANNA_MOUNTAINS");
   public static final XBiome MEADOW = std("MEADOW");
   public static final XBiome MANGROVE_SWAMP = std("MANGROVE_SWAMP");
   public static final XBiome DEEP_DARK = std("DEEP_DARK");
   public static final XBiome GROVE = std("GROVE");
   public static final XBiome SNOWY_SLOPES = std("SNOWY_SLOPES");
   public static final XBiome FROZEN_PEAKS = std("FROZEN_PEAKS");
   public static final XBiome JAGGED_PEAKS = std("JAGGED_PEAKS");
   public static final XBiome STONY_PEAKS = std("STONY_PEAKS");
   public static final XBiome BADLANDS = std("BADLANDS", "MESA");
   public static final XBiome BADLANDS_PLATEAU;
   public static final XBiome BEACH;
   public static final XBiome BIRCH_FOREST;
   public static final XBiome BIRCH_FOREST_HILLS;
   public static final XBiome COLD_OCEAN;
   public static final XBiome DARK_FOREST;
   public static final XBiome DARK_FOREST_HILLS;
   public static final XBiome DEEP_COLD_OCEAN;
   public static final XBiome DEEP_FROZEN_OCEAN;
   public static final XBiome DEEP_LUKEWARM_OCEAN;
   public static final XBiome DEEP_OCEAN;
   public static final XBiome DEEP_WARM_OCEAN;
   public static final XBiome DESERT;
   public static final XBiome DESERT_HILLS;
   public static final XBiome DESERT_LAKES;
   public static final XBiome END_BARRENS;
   public static final XBiome END_HIGHLANDS;
   public static final XBiome END_MIDLANDS;
   public static final XBiome ERODED_BADLANDS;
   public static final XBiome FLOWER_FOREST;
   public static final XBiome FOREST;
   public static final XBiome FROZEN_OCEAN;
   public static final XBiome FROZEN_RIVER;
   public static final XBiome GIANT_SPRUCE_TAIGA;
   public static final XBiome GIANT_SPRUCE_TAIGA_HILLS;
   public static final XBiome GIANT_TREE_TAIGA;
   public static final XBiome GIANT_TREE_TAIGA_HILLS;
   public static final XBiome ICE_SPIKES;
   public static final XBiome JUNGLE;
   public static final XBiome JUNGLE_HILLS;
   public static final XBiome LUKEWARM_OCEAN;
   public static final XBiome MODIFIED_BADLANDS_PLATEAU;
   public static final XBiome MODIFIED_GRAVELLY_MOUNTAINS;
   public static final XBiome MODIFIED_JUNGLE;
   public static final XBiome MODIFIED_JUNGLE_EDGE;
   public static final XBiome MODIFIED_WOODED_BADLANDS_PLATEAU;
   public static final XBiome MOUNTAIN_EDGE;
   public static final XBiome MUSHROOM_FIELDS;
   public static final XBiome MUSHROOM_FIELD_SHORE;
   public static final XBiome SOUL_SAND_VALLEY;
   public static final XBiome CRIMSON_FOREST;
   public static final XBiome WARPED_FOREST;
   public static final XBiome BASALT_DELTAS;
   public static final XBiome NETHER_WASTES;
   public static final XBiome OCEAN;
   public static final XBiome PLAINS;
   public static final XBiome RIVER;
   public static final XBiome SAVANNA;
   public static final XBiome SAVANNA_PLATEAU;
   public static final XBiome SHATTERED_SAVANNA_PLATEAU;
   public static final XBiome SMALL_END_ISLANDS;
   public static final XBiome SNOWY_BEACH;
   public static final XBiome SNOWY_MOUNTAINS;
   public static final XBiome SNOWY_TAIGA;
   public static final XBiome SNOWY_TAIGA_HILLS;
   public static final XBiome SNOWY_TAIGA_MOUNTAINS;
   public static final XBiome SUNFLOWER_PLAINS;
   public static final XBiome SWAMP;
   public static final XBiome SWAMP_HILLS;
   public static final XBiome TAIGA;
   public static final XBiome TAIGA_HILLS;
   public static final XBiome TAIGA_MOUNTAINS;
   public static final XBiome CUSTOM;
   public static final XBiome TALL_BIRCH_FOREST;
   public static final XBiome TALL_BIRCH_HILLS;
   public static final XBiome THE_END;
   public static final XBiome THE_VOID;
   public static final XBiome WARM_OCEAN;
   public static final XBiome WOODED_BADLANDS_PLATEAU;
   public static final XBiome WOODED_HILLS;
   public static final XBiome WOODED_MOUNTAINS;
   public static final XBiome BAMBOO_JUNGLE;
   public static final XBiome BAMBOO_JUNGLE_HILLS;
   public static final XBiome DRIPSTONE_CAVES;
   public static final XBiome LUSH_CAVES;
   private static final boolean World_getMaxHeight$SUPPORTED;
   private static final boolean World_getMinHeight$SUPPORTED;
   @Nullable
   private final Environment environment;

   public XBiome(Environment var1, Biome var2, String[] var3) {
      super(var2, var3);
      this.environment = var1;
   }

   private XBiome(Biome var1, String[] var2) {
      this((Environment)null, var1, var2);
   }

   public Optional<Environment> getEnvironment() {
      return Optional.ofNullable(this.environment);
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public Biome getBiome() {
      return (Biome)this.get();
   }

   @NotNull
   public CompletableFuture<Void> setBiome(@NotNull Chunk var1) {
      Biome var2 = (Biome)this.get();
      Objects.requireNonNull(var2, () -> {
         return "Unsupported biome: " + this.name();
      });
      Objects.requireNonNull(var1, "Cannot set biome of null chunk");
      if (!var1.isLoaded() && !var1.load(true)) {
         throw new IllegalStateException("Could not load chunk at " + var1.getX() + ", " + var1.getZ());
      } else {
         int var3 = World_getMaxHeight$SUPPORTED ? var1.getWorld().getMaxHeight() : 1;
         int var4 = World_getMinHeight$SUPPORTED ? var1.getWorld().getMinHeight() : 0;
         return CompletableFuture.runAsync(() -> {
            for(int var4x = 0; var4x < 16; ++var4x) {
               for(int var5 = var4; var5 < var3; var5 += 4) {
                  for(int var6 = 0; var6 < 16; ++var6) {
                     Block var7 = var1.getBlock(var4x, var5, var6);
                     if (var7.getBiome() != var2) {
                        var7.setBiome(var2);
                     }
                  }
               }
            }

         }).exceptionally((var0) -> {
            var0.printStackTrace();
            return null;
         });
      }
   }

   @NotNull
   public CompletableFuture<Void> setBiome(@NotNull Location var1, @NotNull Location var2) {
      Biome var3 = (Biome)this.get();
      Objects.requireNonNull(var1, "Start location cannot be null");
      Objects.requireNonNull(var2, "End location cannot be null");
      Objects.requireNonNull(var3, () -> {
         return "Unsupported biome: " + this.name();
      });
      World var4 = var1.getWorld();
      if (!var4.getUID().equals(var2.getWorld().getUID())) {
         throw new IllegalArgumentException("Location worlds mismatch");
      } else {
         int var5 = World_getMaxHeight$SUPPORTED ? var4.getMaxHeight() : 1;
         int var6 = World_getMinHeight$SUPPORTED ? var4.getMinHeight() : 0;
         return CompletableFuture.runAsync(() -> {
            for(int var6x = var1.getBlockX(); var6x < var2.getBlockX(); ++var6x) {
               for(int var7 = var6; var7 < var5; var7 += 4) {
                  for(int var8 = var1.getBlockZ(); var8 < var2.getBlockZ(); ++var8) {
                     Block var9 = (new Location(var4, (double)var6x, (double)var7, (double)var8)).getBlock();
                     if (var9.getBiome() != var3) {
                        var9.setBiome(var3);
                     }
                  }
               }
            }

         }).exceptionally((var0) -> {
            var0.printStackTrace();
            return null;
         });
      }
   }

   @NotNull
   public static XBiome of(@NotNull Biome var0) {
      return (XBiome)REGISTRY.getByBukkitForm(var0);
   }

   public static Optional<XBiome> of(@NotNull String var0) {
      return REGISTRY.getByName(var0);
   }

   /** @deprecated */
   @Deprecated
   public static XBiome[] values() {
      return (XBiome[])REGISTRY.values();
   }

   @NotNull
   @Unmodifiable
   public static Collection<XBiome> getValues() {
      return REGISTRY.getValues();
   }

   @NotNull
   private static XBiome std(@NotNull Environment var0, @NotNull String... var1) {
      return (XBiome)REGISTRY.std((var2) -> {
         return new XBiome(var0, var2, var1);
      }, var1);
   }

   @NotNull
   private static XBiome std(@Nullable XBiome var0, @NotNull String... var1) {
      return (XBiome)REGISTRY.std((var1x) -> {
         return new XBiome((Environment)null, var1x, var1);
      }, var0, var1);
   }

   @NotNull
   private static XBiome std(@NotNull String... var0) {
      return (XBiome)REGISTRY.std((var1) -> {
         return new XBiome(Environment.NORMAL, var1, var0);
      }, var0);
   }

   static {
      BADLANDS_PLATEAU = std(WOODED_BADLANDS, "BADLANDS_PLATEAU", "MESA_CLEAR_ROCK", "MESA_PLATEAU");
      BEACH = std("BEACH", "BEACHES");
      BIRCH_FOREST = std(OLD_GROWTH_BIRCH_FOREST, "BIRCH_FOREST");
      BIRCH_FOREST_HILLS = std(OLD_GROWTH_BIRCH_FOREST, "BIRCH_FOREST_HILLS");
      COLD_OCEAN = std("COLD_OCEAN");
      DARK_FOREST = std("DARK_FOREST", "ROOFED_FOREST");
      DARK_FOREST_HILLS = std("DARK_FOREST_HILLS", "MUTATED_ROOFED_FOREST", "ROOFED_FOREST_MOUNTAINS");
      DEEP_COLD_OCEAN = std("DEEP_COLD_OCEAN", "COLD_DEEP_OCEAN");
      DEEP_FROZEN_OCEAN = std("DEEP_FROZEN_OCEAN", "FROZEN_DEEP_OCEAN");
      DEEP_LUKEWARM_OCEAN = std("DEEP_LUKEWARM_OCEAN", "LUKEWARM_DEEP_OCEAN");
      DEEP_OCEAN = std("DEEP_OCEAN");
      DEEP_WARM_OCEAN = std("DEEP_WARM_OCEAN", "WARM_DEEP_OCEAN");
      DESERT = std("DESERT");
      DESERT_HILLS = std("DESERT_HILLS");
      DESERT_LAKES = std("DESERT_LAKES", "MUTATED_DESERT", "DESERT_MOUNTAINS");
      END_BARRENS = std(Environment.THE_END, "END_BARRENS", "SKY_ISLAND_BARREN");
      END_HIGHLANDS = std(Environment.THE_END, "END_HIGHLANDS", "SKY_ISLAND_HIGH");
      END_MIDLANDS = std(Environment.THE_END, "END_MIDLANDS", "SKY_ISLAND_MEDIUM");
      ERODED_BADLANDS = std("ERODED_BADLANDS", "MUTATED_MESA", "MESA_BRYCE");
      FLOWER_FOREST = std("FLOWER_FOREST", "MUTATED_FOREST");
      FOREST = std("FOREST");
      FROZEN_OCEAN = std("FROZEN_OCEAN");
      FROZEN_RIVER = std("FROZEN_RIVER");
      GIANT_SPRUCE_TAIGA = std(OLD_GROWTH_SPRUCE_TAIGA, "GIANT_SPRUCE_TAIGA", "MUTATED_REDWOOD_TAIGA", "MEGA_SPRUCE_TAIGA");
      GIANT_SPRUCE_TAIGA_HILLS = std(OLD_GROWTH_SPRUCE_TAIGA, "GIANT_SPRUCE_TAIGA_HILLS", "MUTATED_REDWOOD_TAIGA_HILLS", "MEGA_SPRUCE_TAIGA_HILLS");
      GIANT_TREE_TAIGA = std(OLD_GROWTH_PINE_TAIGA, "GIANT_TREE_TAIGA", "REDWOOD_TAIGA", "MEGA_TAIGA");
      GIANT_TREE_TAIGA_HILLS = std(OLD_GROWTH_PINE_TAIGA, "GIANT_TREE_TAIGA_HILLS", "REDWOOD_TAIGA_HILLS", "MEGA_TAIGA_HILLS");
      ICE_SPIKES = std("ICE_SPIKES", "MUTATED_ICE_FLATS", "ICE_PLAINS_SPIKES");
      JUNGLE = std("JUNGLE");
      JUNGLE_HILLS = std("JUNGLE_HILLS");
      LUKEWARM_OCEAN = std("LUKEWARM_OCEAN");
      MODIFIED_BADLANDS_PLATEAU = std(WOODED_BADLANDS, "MODIFIED_BADLANDS_PLATEAU", "MUTATED_MESA_CLEAR_ROCK", "MESA_PLATEAU");
      MODIFIED_GRAVELLY_MOUNTAINS = std(WINDSWEPT_GRAVELLY_HILLS, "MODIFIED_GRAVELLY_MOUNTAINS", "MUTATED_EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_MOUNTAINS");
      MODIFIED_JUNGLE = std("MODIFIED_JUNGLE", "MUTATED_JUNGLE", "JUNGLE_MOUNTAINS");
      MODIFIED_JUNGLE_EDGE = std(SPARSE_JUNGLE, "MODIFIED_JUNGLE_EDGE", "MUTATED_JUNGLE_EDGE", "JUNGLE_EDGE_MOUNTAINS");
      MODIFIED_WOODED_BADLANDS_PLATEAU = std(WOODED_BADLANDS, "MODIFIED_WOODED_BADLANDS_PLATEAU", "MUTATED_MESA_ROCK", "MESA_PLATEAU_FOREST_MOUNTAINS");
      MOUNTAIN_EDGE = std(SPARSE_JUNGLE, "MOUNTAIN_EDGE", "SMALLER_EXTREME_HILLS");
      MUSHROOM_FIELDS = std("MUSHROOM_FIELDS", "MUSHROOM_ISLAND");
      MUSHROOM_FIELD_SHORE = std(STONY_SHORE, "MUSHROOM_FIELD_SHORE", "MUSHROOM_ISLAND_SHORE", "MUSHROOM_SHORE");
      SOUL_SAND_VALLEY = std(Environment.NETHER, "SOUL_SAND_VALLEY");
      CRIMSON_FOREST = std(Environment.NETHER, "CRIMSON_FOREST");
      WARPED_FOREST = std(Environment.NETHER, "WARPED_FOREST");
      BASALT_DELTAS = std(Environment.NETHER, "BASALT_DELTAS");
      NETHER_WASTES = std(Environment.NETHER, "NETHER_WASTES", "NETHER", "HELL");
      OCEAN = std("OCEAN");
      PLAINS = std("PLAINS");
      RIVER = std("RIVER");
      SAVANNA = std("SAVANNA");
      SAVANNA_PLATEAU = std(WINDSWEPT_SAVANNA, "SAVANNA_ROCK", "SAVANNA_PLATEAU");
      SHATTERED_SAVANNA_PLATEAU = std(WINDSWEPT_SAVANNA, "SHATTERED_SAVANNA_PLATEAU", "MUTATED_SAVANNA_ROCK", "SAVANNA_PLATEAU_MOUNTAINS");
      SMALL_END_ISLANDS = std(Environment.THE_END, "SMALL_END_ISLANDS", "SKY_ISLAND_LOW");
      SNOWY_BEACH = std("SNOWY_BEACH", "COLD_BEACH");
      SNOWY_MOUNTAINS = std(WINDSWEPT_HILLS, "SNOWY_MOUNTAINS", "ICE_MOUNTAINS");
      SNOWY_TAIGA = std("SNOWY_TAIGA", "TAIGA_COLD", "COLD_TAIGA");
      SNOWY_TAIGA_HILLS = std("SNOWY_TAIGA_HILLS", "TAIGA_COLD_HILLS", "COLD_TAIGA_HILLS");
      SNOWY_TAIGA_MOUNTAINS = std(WINDSWEPT_FOREST, "SNOWY_TAIGA_MOUNTAINS", "MUTATED_TAIGA_COLD", "COLD_TAIGA_MOUNTAINS");
      SUNFLOWER_PLAINS = std("SUNFLOWER_PLAINS", "MUTATED_PLAINS");
      SWAMP = std("SWAMP", "SWAMPLAND");
      SWAMP_HILLS = std("SWAMP_HILLS", "MUTATED_SWAMPLAND", "SWAMPLAND_MOUNTAINS");
      TAIGA = std("TAIGA");
      TAIGA_HILLS = std("TAIGA_HILLS");
      TAIGA_MOUNTAINS = std(WINDSWEPT_FOREST, "TAIGA_MOUNTAINS", "MUTATED_TAIGA");
      CUSTOM = std("CUSTOM");
      TALL_BIRCH_FOREST = std(OLD_GROWTH_BIRCH_FOREST, "TALL_BIRCH_FOREST", "MUTATED_BIRCH_FOREST", "BIRCH_FOREST_MOUNTAINS");
      TALL_BIRCH_HILLS = std(OLD_GROWTH_BIRCH_FOREST, "TALL_BIRCH_HILLS", "MUTATED_BIRCH_FOREST_HILLS", "MESA_PLATEAU_FOREST_MOUNTAINS");
      THE_END = std(Environment.THE_END, "THE_END", "SKY");
      THE_VOID = std("THE_VOID", "VOID");
      WARM_OCEAN = std("WARM_OCEAN");
      WOODED_BADLANDS_PLATEAU = std("WOODED_BADLANDS_PLATEAU", "MESA_ROCK", "MESA_PLATEAU_FOREST");
      WOODED_HILLS = std("WOODED_HILLS", "FOREST_HILLS");
      WOODED_MOUNTAINS = std("WOODED_MOUNTAINS", "EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_PLUS");
      BAMBOO_JUNGLE = std("BAMBOO_JUNGLE");
      BAMBOO_JUNGLE_HILLS = std("BAMBOO_JUNGLE_HILLS");
      DRIPSTONE_CAVES = std("DRIPSTONE_CAVES");
      LUSH_CAVES = std("LUSH_CAVES");
      boolean var0 = false;
      boolean var1 = false;

      try {
         World.class.getMethod("getMaxHeight");
         var0 = true;
      } catch (Exception var4) {
      }

      try {
         World.class.getMethod("getMinHeight");
         var1 = true;
      } catch (Exception var3) {
      }

      World_getMaxHeight$SUPPORTED = var0;
      World_getMinHeight$SUPPORTED = var1;
      REGISTRY.discardMetadata();
   }
}
