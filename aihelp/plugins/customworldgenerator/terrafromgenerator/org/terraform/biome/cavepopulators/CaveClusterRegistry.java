package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;

public enum CaveClusterRegistry {
   LUSH(9527213, TConfig.c.BIOME_CAVE_LUSHCLUSTER_SEPARATION, (float)TConfig.c.BIOME_CAVE_LUSHCLUSTER_MAXPERTUB),
   DRIPSTONE(5902907, TConfig.c.BIOME_CAVE_DRIPSTONECLUSTER_SEPARATION, (float)TConfig.c.BIOME_CAVE_DRIPSTONECLUSTER_MAXPERTUB),
   CRYSTALLINE(4427781, TConfig.c.BIOME_CAVE_CRYSTALLINECLUSTER_SEPARATION, (float)TConfig.c.BIOME_CAVE_CRYSTALLINECLUSTER_MAXPERTUB),
   FLUID(79183628, 40, 0.2F);

   final int hashSeed;
   final int separation;
   final float pertub;

   private CaveClusterRegistry(int param3, int param4, float param5) {
      this.hashSeed = hashSeed;
      this.separation = separation;
      this.pertub = pertub;
   }

   @NotNull
   public AbstractCaveClusterPopulator getPopulator(@NotNull Random random) {
      Object var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = new LushClusterCavePopulator((float)GenUtils.randInt(random, TConfig.c.BIOME_CAVE_LUSHCLUSTER_MINSIZE, TConfig.c.BIOME_CAVE_LUSHCLUSTER_MAXSIZE), false);
         break;
      case 1:
         var10000 = new DripstoneClusterCavePopulator((float)GenUtils.randInt(random, TConfig.c.BIOME_CAVE_DRIPSTONECLUSTER_MINSIZE, TConfig.c.BIOME_CAVE_DRIPSTONECLUSTER_MAXSIZE));
         break;
      case 2:
         var10000 = new CrystallineClusterCavePopulator((float)GenUtils.randInt(random, TConfig.c.BIOME_CAVE_CRYSTALLINECLUSTER_MINSIZE, TConfig.c.BIOME_CAVE_CRYSTALLINECLUSTER_MAXSIZE));
         break;
      case 3:
         var10000 = new CaveFluidClusterPopulator((float)GenUtils.randInt(random, TConfig.c.BIOME_CAVE_CRYSTALLINECLUSTER_MINSIZE, TConfig.c.BIOME_CAVE_CRYSTALLINECLUSTER_MAXSIZE));
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return (AbstractCaveClusterPopulator)var10000;
   }

   public int getHashSeed() {
      return this.hashSeed;
   }

   public int getSeparation() {
      return this.separation;
   }

   public float getPertub() {
      return this.pertub;
   }

   // $FF: synthetic method
   private static CaveClusterRegistry[] $values() {
      return new CaveClusterRegistry[]{LUSH, DRIPSTONE, CRYSTALLINE, FLUID};
   }
}
