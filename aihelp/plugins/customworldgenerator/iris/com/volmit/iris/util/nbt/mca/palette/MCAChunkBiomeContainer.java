package com.volmit.iris.util.nbt.mca.palette;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCAChunkBiomeContainer<T> {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final int WIDTH_BITS = MCAMth.ceillog2(16) - 2;
   private static final int HORIZONTAL_MASK;
   private static final int PACKED_X_LENGTH;
   private static final int PACKED_Z_LENGTH;
   public static final int PACKED_Y_LENGTH;
   public static final int MAX_SIZE;
   public final MCAIdMap<T> biomeRegistry;
   private final T[] biomes;
   private final int quartMinY;
   private final int quartHeight;

   protected MCAChunkBiomeContainer(MCAIdMap<T> registry, int minHeight, int maxHeight, T[] abiomebase) {
      this.biomeRegistry = var1;
      this.biomes = var4;
      this.quartMinY = MCAQuartPos.fromBlock(var2);
      this.quartHeight = MCAQuartPos.fromBlock(var3) - 1;
   }

   public MCAChunkBiomeContainer(MCAIdMap<T> registry, int min, int max) {
      this(var1, var2, var3, new int[(1 << WIDTH_BITS + WIDTH_BITS) * ceilDiv(var3 - var2, 4)]);
   }

   public MCAChunkBiomeContainer(MCAIdMap<T> registry, int minHeight, int maxHeight, int[] aint) {
      this(var1, var2, var3, new Object[var4.length]);
      int var5 = -1;

      for(int var6 = 0; var6 < this.biomes.length; ++var6) {
         int var7 = var4[var6];
         Object var8 = var1.byId(var7);
         if (var8 == null) {
            if (var5 == -1) {
               var5 = var6;
            }

            this.biomes[var6] = var1.byId(0);
         } else {
            this.biomes[var6] = var8;
         }
      }

      if (var5 != -1) {
         LOGGER.warn("Invalid biome data received, starting from {}: {}", var5, Arrays.toString(var4));
      }

   }

   private static int ceilDiv(int i, int j) {
      return (var0 + var1 - 1) / var1;
   }

   public int[] writeBiomes() {
      int[] var1 = new int[this.biomes.length];

      for(int var2 = 0; var2 < this.biomes.length; ++var2) {
         var1[var2] = this.biomeRegistry.getId(this.biomes[var2]);
      }

      return var1;
   }

   public T getBiome(int i, int j, int k) {
      int var4 = var1 & HORIZONTAL_MASK;
      int var5 = MCAMth.clamp((int)(var2 - this.quartMinY), (int)0, (int)this.quartHeight);
      int var6 = var3 & HORIZONTAL_MASK;
      return this.biomes[var5 << WIDTH_BITS + WIDTH_BITS | var6 << WIDTH_BITS | var4];
   }

   public void setBiome(int i, int j, int k, T biome) {
      int var5 = var1 & HORIZONTAL_MASK;
      int var6 = MCAMth.clamp((int)(var2 - this.quartMinY), (int)0, (int)this.quartHeight);
      int var7 = var3 & HORIZONTAL_MASK;
      this.biomes[var6 << WIDTH_BITS + WIDTH_BITS | var7 << WIDTH_BITS | var5] = var4;
   }

   static {
      HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;
      PACKED_X_LENGTH = 1 + MCAMth.log2(MCAMth.smallestEncompassingPowerOfTwo(30000000));
      PACKED_Z_LENGTH = PACKED_X_LENGTH;
      PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
      MAX_SIZE = 1 << WIDTH_BITS + WIDTH_BITS + PACKED_Y_LENGTH - 2;
   }
}
