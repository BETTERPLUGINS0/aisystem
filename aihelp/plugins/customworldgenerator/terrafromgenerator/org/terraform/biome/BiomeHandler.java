package org.terraform.biome;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;

public abstract class BiomeHandler {
   public abstract boolean isOcean();

   @NotNull
   public CustomBiomeType getCustomBiome() {
      return CustomBiomeType.NONE;
   }

   public abstract Biome getBiome();

   public abstract Material[] getSurfaceCrust(Random var1);

   public abstract void populateSmallItems(TerraformWorld var1, Random var2, int var3, int var4, int var5, PopulatorDataAbstract var6);

   public abstract void populateLargeItems(TerraformWorld var1, Random var2, PopulatorDataAbstract var3);

   public int getMaxHeightForCaves(@NotNull TerraformWorld tw, int x, int z) {
      return tw.maxY;
   }

   @Nullable
   public BiomeHandler getTransformHandler() {
      return null;
   }

   public void transformTerrain(ChunkCache cache, TerraformWorld tw, Random random, ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.SANDY_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.RIVER;
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      return HeightMap.CORE.getHeight(tw, x, z);
   }

   public boolean forceDefaultToBeach() {
      return false;
   }
}
