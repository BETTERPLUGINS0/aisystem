package advancedplugins.pm2.cv.api.service;

import advancedplugins.pm2.cv.api.enums.EnumSurface;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface BlockInfoService extends Service {
   boolean isCanStandOnSurfaceAt(World var1, int var2, int var3, int var4);

   EnumSurface getSurfaceTypeAt(World var1, int var2, int var3, int var4, boolean var5);

   BlockInfoService.SurfaceResult getSurfaceTypesAt(World var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8);

   default boolean isEmpty(World world, int x, int y, int z) {
      return this.getSurfaceTypeAt(world, x, y, z, false) == EnumSurface.EMPTY;
   }

   default boolean isSolid(World world, int x, int y, int z, boolean ignoreWeather) {
      return this.getSurfaceTypeAt(world, x, y, z, ignoreWeather) == EnumSurface.SOLID;
   }

   default boolean isWater(World world, int x, int y, int z) {
      return this.getSurfaceTypeAt(world, x, y, z, false) == EnumSurface.WATER;
   }

   default boolean isLava(World world, int x, int y, int z) {
      return this.getSurfaceTypeAt(world, x, y, z, false) == EnumSurface.LAVA;
   }

   public static class SurfaceResult {
      private final int minX;
      private final int minZ;
      private final int maxX;
      private final int maxZ;
      @NotNull
      private final EnumSurface[] value;

      @NotNull
      public EnumSurface getSurfaceType(int var1, int var2) {
         int var3 = this.maxZ - this.minZ;
         var1 -= this.minX;
         var2 -= this.minZ;
         return this.value[var1 * var3 + var2];
      }

      public int getMinX() {
         return this.minX;
      }

      public int getMinZ() {
         return this.minZ;
      }

      public int getMaxX() {
         return this.maxX;
      }

      public int getMaxZ() {
         return this.maxZ;
      }

      @NotNull
      public EnumSurface[] getValue() {
         return this.value;
      }

      public SurfaceResult(int var1, int var2, int var3, int var4, @NotNull EnumSurface[] var5) {
         this.minX = var1;
         this.minZ = var2;
         this.maxX = var3;
         this.maxZ = var4;
         this.value = var5;
      }
   }
}
