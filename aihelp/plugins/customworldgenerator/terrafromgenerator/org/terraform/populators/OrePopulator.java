package org.terraform.populators;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;

public class OrePopulator {
   private final BlockData type;
   private final int baseChance;
   private final int maxOreSize;
   private final int minOreSize;
   private final int maxNumberOfVeins;
   private final int peakSpawnChanceHeight;
   private final int maxSpawnHeight;
   private final Set<BiomeBank> requiredBiomes;
   private final int maxDistance;
   private final boolean ignorePeakSpawnChance;
   private int minRange;
   private static final ConcurrentHashMap<TerraformWorld, FastNoise> privateNoiseCache = new ConcurrentHashMap();

   public OrePopulator(Material type, int baseChance, int maxOreSize, int maxNumberOfVeins, int peakSpawnChanceHeight, int maxSpawnHeight, boolean ignorePeakSpawnChance, BiomeBank... requiredBiomes) {
      this.type = Bukkit.createBlockData(type);
      this.baseChance = baseChance;
      this.maxOreSize = maxOreSize;
      this.minOreSize = maxOreSize / 2;
      this.maxNumberOfVeins = maxNumberOfVeins;
      this.peakSpawnChanceHeight = peakSpawnChanceHeight;
      this.maxSpawnHeight = maxSpawnHeight;
      this.requiredBiomes = Set.of(requiredBiomes);
      this.ignorePeakSpawnChance = ignorePeakSpawnChance;
      this.minRange = TerraformGeneratorPlugin.injector.getMinY() + 1;
      this.maxDistance = Math.max(Math.abs(this.minRange - peakSpawnChanceHeight), Math.abs(maxSpawnHeight - peakSpawnChanceHeight));
   }

   public OrePopulator(Material type, int baseChance, int maxOreSize, int maxNumberOfVeins, int minRange, int peakSpawnChanceHeight, int maxSpawnHeight, boolean ignorePeakSpawnChance, BiomeBank... requiredBiomes) {
      this.type = Bukkit.createBlockData(type);
      this.baseChance = baseChance;
      this.maxOreSize = maxOreSize;
      this.minOreSize = maxOreSize / 2;
      this.maxNumberOfVeins = TConfig.c.FEATURE_ORES_ENABLED ? maxNumberOfVeins : 0;
      this.minRange = minRange;
      this.peakSpawnChanceHeight = peakSpawnChanceHeight;
      this.maxSpawnHeight = maxSpawnHeight;
      this.requiredBiomes = Set.of(requiredBiomes);
      this.ignorePeakSpawnChance = ignorePeakSpawnChance;
      this.maxDistance = Math.max(Math.abs(minRange - peakSpawnChanceHeight), Math.abs(maxSpawnHeight - peakSpawnChanceHeight));
   }

   public void populate(@NotNull TerraformWorld world, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (this.requiredBiomes.size() > 0) {
         BiomeBank b = BiomeBank.getBiomeSectionFromChunk(world, data.getChunkX(), data.getChunkZ()).getBiomeBank();
         if (!this.requiredBiomes.contains(b)) {
            return;
         }
      }

      for(int i = 0; i < this.maxNumberOfVeins; ++i) {
         if (GenUtils.chance(random, this.baseChance, 100)) {
            int x = GenUtils.randInt(random, 0, 15) + data.getChunkX() * 16;
            int z = GenUtils.randInt(random, 0, 15) + data.getChunkZ() * 16;
            int range = this.maxSpawnHeight;
            if (this.minRange <= range) {
               if (this.minRange < world.minY) {
                  this.minRange = world.minY;
               }

               int y = GenUtils.randInt(random, this.minRange + 64, range + 64) - 64;
               if (!this.ignorePeakSpawnChance) {
                  int distance = Math.abs(y - this.peakSpawnChanceHeight);
                  if (!GenUtils.chance((int)Math.round(100.0D * (1.0D - (double)((float)distance / (float)this.maxDistance))), 100)) {
                     continue;
                  }
               }

               this.placeOre(Objects.hash(new Object[]{world.getSeed(), 7118794}), data, x, y, z);
            }
         }
      }

   }

   public void placeOre(int seed, @NotNull PopulatorDataAbstract data, int coreX, int coreY, int coreZ) {
      double size = GenUtils.randDouble(new Random((long)seed), (double)this.minOreSize, (double)this.maxOreSize);
      double radius = Math.pow(0.75D * size * 0.3183098861837907D, 0.3333333333333333D);
      if (!(radius <= 0.0D)) {
         if (radius <= 0.5D) {
            data.setBlockData(coreX, coreY, coreZ, (BlockData)GenUtils.randChoice(new Random((long)seed), this.type));
         } else {
            FastNoise noise = (FastNoise)privateNoiseCache.get(data.getTerraformWorld());
            if (noise == null) {
               noise = new FastNoise(seed);
               noise.SetNoiseType(FastNoise.NoiseType.Simplex);
               noise.SetFrequency(0.09F);
               privateNoiseCache.put(data.getTerraformWorld(), noise);
            }

            ArrayDeque<Long> bfsQueue = new ArrayDeque();
            HashSet<Integer> visited = new HashSet();
            visited.add(Objects.hash(new Object[]{0, 0, 0}));
            bfsQueue.add(0L);

            while(true) {
               while(bfsQueue.size() > 0) {
                  long v = (Long)bfsQueue.remove();
                  short rZ = (short)((int)(v & 65535L));
                  short rY = (short)((int)(v >> 16 & 65535L));
                  short rX = (short)((int)(v >> 32 & 65535L));
                  BlockFace[] var18 = BlockUtils.sixBlockFaces;
                  int y = var18.length;

                  int z;
                  for(z = 0; z < y; ++z) {
                     BlockFace face = var18[z];
                     long nX = (long)(rX + face.getModX());
                     long nY = (long)(rY + face.getModY());
                     long nZ = (long)(rZ + face.getModZ());
                     int hash = Objects.hash(new Object[]{nX, nY, nZ});
                     if (visited.add(hash) && (long)coreY + nY > (long)TerraformGeneratorPlugin.injector.getMinY() && (long)coreY + nY < (long)TerraformGeneratorPlugin.injector.getMaxY()) {
                        double equationResult = Math.pow((double)nX, 2.0D) / Math.pow(radius, 2.0D) + Math.pow((double)nY, 2.0D) / Math.pow(radius, 2.0D) + Math.pow((double)nZ, 2.0D) / Math.pow(radius, 2.0D);
                        if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)(nX + (long)coreX), (float)(nY + (long)coreY), (float)(nZ + (long)coreZ))) {
                           bfsQueue.add(nZ | nY << 16 | nX << 32);
                        }
                     }
                  }

                  int x = rX + coreX;
                  y = rY + coreY;
                  z = rZ + coreZ;
                  Material replaced = data.getType(x, y, z);
                  if (replaced == Material.STONE) {
                     data.setBlockData(x, y, z, this.type);
                  } else if (this.type.getMaterial() == Material.DEEPSLATE && BlockUtils.ores.contains(replaced)) {
                     data.setBlockData(x, y, z, BlockUtils.deepSlateVersion(replaced));
                  } else if (replaced == Material.DEEPSLATE) {
                     data.setBlockData(x, y, z, BlockUtils.deepSlateVersion(this.type.getMaterial()));
                  }
               }

               return;
            }
         }
      }
   }

   public Material getType() {
      return this.type.getMaterial();
   }

   public int getBaseChance() {
      return this.baseChance;
   }

   public int getMaxOreSize() {
      return this.maxOreSize;
   }

   public int getMinOreSize() {
      return this.minOreSize;
   }

   public int getMaxNumberOfVeins() {
      return this.maxNumberOfVeins;
   }

   public int getPeakSpawnChanceHeight() {
      return this.peakSpawnChanceHeight;
   }

   public int getMaxSpawnHeight() {
      return this.maxSpawnHeight;
   }

   public int getMinRange() {
      return this.minRange;
   }

   public Set<BiomeBank> getRequiredBiomes() {
      return this.requiredBiomes;
   }

   public int getMaxDistance() {
      return this.maxDistance;
   }

   public boolean isIgnorePeakSpawnChance() {
      return this.ignorePeakSpawnChance;
   }
}
