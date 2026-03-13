package org.terraform.data;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.cave.NoiseCaveRegistry;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformBukkitBlockPopulator;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class TerraformWorld {
   private static final ConcurrentHashMap<String, TerraformWorld> WORLDS = new ConcurrentHashMap();
   @NotNull
   public final NoiseCaveRegistry noiseCaveRegistry;
   private final String worldName;
   private final long seed;
   @NotNull
   private final TerraformBukkitBlockPopulator bukkitBlockPopulator;
   public int minY = 0;
   public int maxY = 256;

   public TerraformWorld(String name, long seed) {
      TerraformGeneratorPlugin.logger.info("Creating TW instance: " + name + " - " + seed);
      this.worldName = name;
      this.seed = seed;
      this.bukkitBlockPopulator = new TerraformBukkitBlockPopulator(this);
      this.noiseCaveRegistry = new NoiseCaveRegistry(this);
   }

   private TerraformWorld(@NotNull World world) {
      TLogger var10000 = TerraformGeneratorPlugin.logger;
      String var10001 = world.getName();
      var10000.info("Creating TW instance: " + var10001 + " - " + world.getSeed());
      this.worldName = world.getName();
      this.seed = world.getSeed();
      this.bukkitBlockPopulator = new TerraformBukkitBlockPopulator(this);
      this.noiseCaveRegistry = new NoiseCaveRegistry(this);
   }

   @NotNull
   public static TerraformWorld forceOverrideSeed(@NotNull World world) {
      TerraformWorld tw = new TerraformWorld(world);
      WORLDS.put(world.getName(), tw);
      return tw;
   }

   @NotNull
   public static TerraformWorld get(@NotNull World world) {
      return (TerraformWorld)WORLDS.computeIfAbsent(world.getName(), (k) -> {
         return new TerraformWorld(world);
      });
   }

   @NotNull
   public static TerraformWorld get(String name, long seed) {
      return (TerraformWorld)WORLDS.computeIfAbsent(name, (k) -> {
         return new TerraformWorld(name, seed);
      });
   }

   @NotNull
   public FastNoise getTemperatureOctave() {
      return NoiseCacheHandler.getNoise(this, NoiseCacheHandler.NoiseCacheEntry.TW_TEMPERATURE, (tw) -> {
         FastNoise n = new FastNoise((int)(tw.getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(TConfig.c.BIOME_TEMPERATURE_FREQUENCY);
         return n;
      });
   }

   @NotNull
   public FastNoise getMoistureOctave() {
      return NoiseCacheHandler.getNoise(this, NoiseCacheHandler.NoiseCacheEntry.TW_MOISTURE, (tw) -> {
         FastNoise n = new FastNoise((int)(tw.getSeed() / 4L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(TConfig.c.BIOME_MOISTURE_FREQUENCY);
         return n;
      });
   }

   @NotNull
   public FastNoise getOceanicNoise() {
      return NoiseCacheHandler.getNoise(this, NoiseCacheHandler.NoiseCacheEntry.TW_OCEANIC, (tw) -> {
         FastNoise n = new FastNoise((int)tw.getSeed() * 12);
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(TConfig.c.BIOME_OCEANIC_FREQUENCY);
         return n;
      });
   }

   @NotNull
   public FastNoise getMountainousNoise() {
      return NoiseCacheHandler.getNoise(this, NoiseCacheHandler.NoiseCacheEntry.TW_MOUNTAINOUS, (tw) -> {
         FastNoise n = new FastNoise((int)tw.getSeed() * 73);
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency((float)TConfig.c.BIOME_MOUNTAINOUS_FREQUENCY);
         return n;
      });
   }

   public long getSeed() {
      return this.seed;
   }

   @NotNull
   public Random getRand(long d) {
      return new Random(this.seed / 4L + 25981L * d);
   }

   @NotNull
   public Random getHashedRand(long a, int b, int c) {
      return new Random(11L * a + (long)Objects.hash(new Object[]{this.seed, 127 * b, 773 * c}));
   }

   @NotNull
   public Random getHashedRand(int x, int y, int z, long multiplier) {
      return new Random((long)Objects.hash(new Object[]{this.seed, 11 * x, 127 * y, 773 * z}) * multiplier);
   }

   public BiomeBank getBiomeBank(int x, int z) {
      ChunkCache cache = TerraformGenerator.getCache(this, x >> 4, z >> 4);
      BiomeBank cachedValue = cache.getBiome(x, z);
      if (!BiomeBank.debugPrint && cachedValue != null) {
         return cachedValue;
      } else {
         int y = HeightMap.getBlockHeight(this, x, z);
         return cache.cacheBiome(x, z, BiomeBank.calculateBiome(this, x, y, z));
      }
   }

   public BiomeBank getBiomeBank(int x, int y, int z) {
      ChunkCache cache = TerraformGenerator.getCache(this, x >> 4, z >> 4);
      BiomeBank cachedValue = cache.getBiome(x, z);
      return cachedValue != null ? cachedValue : cache.cacheBiome(x, z, BiomeBank.calculateBiome(this, x, y, z));
   }

   public String getName() {
      return this.worldName;
   }

   @NotNull
   public World getWorld() {
      return (World)Objects.requireNonNull(Bukkit.getWorld(this.worldName));
   }

   @NotNull
   public TerraformBukkitBlockPopulator getBukkitBlockPopulator() {
      return this.bukkitBlockPopulator;
   }
}
