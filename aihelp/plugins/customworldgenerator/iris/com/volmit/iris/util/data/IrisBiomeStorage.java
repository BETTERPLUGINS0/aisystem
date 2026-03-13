package com.volmit.iris.util.data;

import com.volmit.iris.util.math.IrisMathHelper;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.jetbrains.annotations.NotNull;

public class IrisBiomeStorage implements BiomeGrid {
   public static final int a;
   public static final int b;
   public static final int c;
   private static final int e = (int)Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
   private static final int f = (int)Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
   private final Biome[] g;

   public IrisBiomeStorage(final Biome[] aBiome) {
      this.g = var1;
   }

   public IrisBiomeStorage() {
      this(new Biome[a]);
   }

   public IrisBiomeStorage b() {
      return new IrisBiomeStorage((Biome[])this.g.clone());
   }

   public void inject(BiomeGrid grid) {
      for(int var2 = 0; var2 < 256; ++var2) {
         for(int var3 = 0; var3 < 16; ++var3) {
            for(int var4 = 0; var4 < 16; ++var4) {
               Biome var5 = this.getBiome(var3, var2, var4);
               if (var5 != null && !var5.equals(Biome.THE_VOID)) {
                  var1.setBiome(var3, var2, var4, var5);
               }
            }
         }
      }

   }

   @NotNull
   public Biome getBiome(int x, int z) {
      return null;
   }

   public Biome getBiome(final int x, final int y, final int z) {
      int var4 = var1 & b;
      int var5 = IrisMathHelper.clamp(var2, 0, c);
      int var6 = var3 & b;
      return this.g[var5 << e + e | var6 << e | var4];
   }

   public void setBiome(int x, int z, @NotNull Biome bio) {
   }

   public void setBiome(final int x, final int y, final int z, final Biome biome) {
      int var5 = var1 & b;
      int var6 = IrisMathHelper.clamp(var2, 0, c);
      int var7 = var3 & b;
      this.g[var6 << e + e | var7 << e | var5] = var4;
   }

   static {
      a = 1 << e + e + f;
      b = (1 << e) - 1;
      c = (1 << f) - 1;
   }
}
