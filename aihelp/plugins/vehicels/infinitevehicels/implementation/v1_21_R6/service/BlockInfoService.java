package implementation.v1_21_R6.service;

import advancedplugins.pm2.cv.api.enums.EnumSurface;
import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

public class BlockInfoService implements advancedplugins.pm2.cv.api.service.BlockInfoService {
   public boolean isCanStandOnSurfaceAt(World var1, int var2, int var3, int var4) {
      BlockPos var5 = new BlockPos(var2, var3, var4);
      ServerLevel var6 = ((CraftWorld)var1).getHandle();
      BlockState var7 = var6.getChunkAt(var5).getBlockState(var5);
      Block var8 = var7.getBlock();
      if (!(var8 instanceof SnowLayerBlock) && !(var8 instanceof CarpetBlock) && !(var8 instanceof BasePressurePlateBlock)) {
         VoxelShape var9 = var7.getCollisionShape(var6, var5);
         return var9 != null && !var9.isEmpty();
      } else {
         return false;
      }
   }

   public EnumSurface getSurfaceTypeAt(World var1, int var2, int var3, int var4, boolean var5) {
      BlockPos var6 = new BlockPos(var2, var3, var4);
      BlockState var7 = ((CraftWorld)var1).getHandle().getChunkAt(var6).getBlockState(var6);
      return this.getSurfaceType(var1, var6, var7, var5);
   }

   public advancedplugins.pm2.cv.api.service.BlockInfoService.SurfaceResult getSurfaceTypesAt(World var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8) {
      Preconditions.checkArgument(var3 <= var5, "minX must be <= maxX");
      Preconditions.checkArgument(var4 <= var6, "minZ must be <= maxZ");
      int var9 = var5 - var3 + 1;
      int var10 = var6 - var4 + 1;
      EnumSurface[] var11 = new EnumSurface[var9 * var10];
      ServerLevel var12 = ((CraftWorld)var1).getHandle();
      ChunkAccess var13 = null;
      byte var14 = 0;
      byte var15 = 0;

      for(int var16 = 0; var16 < var9; ++var16) {
         for(int var17 = 0; var17 < var10; ++var17) {
            int var18 = var3 + var16;
            int var19 = var4 + var17;
            int var20 = var18 >> 4;
            int var21 = var19 >> 4;
            if (var13 == null || var14 != var20 || var15 != var21) {
               var13 = var12.getChunk(var20, var21, ChunkStatus.SURFACE);
            }

            BlockPos var22 = new BlockPos(var18, var2, var19);
            if (!var7) {
               BlockPos var23 = new BlockPos(var18, var2 + 1, var19);
               BlockState var24 = var13.getBlockState(var23);
               Block var25 = var24.getBlock();
               if (var25 instanceof SnowLayerBlock || var25 instanceof WoolCarpetBlock || var25 instanceof BasePressurePlateBlock) {
                  var22 = var23;
               }
            }

            var11[var16 * var10 + var17] = this.getSurfaceType(var1, var22, var13.getBlockState(var22), var8);
         }
      }

      return new advancedplugins.pm2.cv.api.service.BlockInfoService.SurfaceResult(var3, var4, var5, var6, var11);
   }

   private EnumSurface getSurfaceType(World var1, BlockPos var2, BlockState var3, boolean var4) {
      ServerLevel var5 = ((CraftWorld)var1).getHandle();
      Block var6 = var3.getBlock();
      boolean var7 = !var1.isClearWeather();
      if (var6 instanceof AirBlock) {
         return EnumSurface.EMPTY;
      } else if (var7 && this.isSlipperyWhenRaining(var6)) {
         return EnumSurface.SLIPPERY;
      } else if (var6 != Blocks.SAND && !(var6 instanceof SoulSandBlock) && var6 != Blocks.SUSPICIOUS_SAND && var6 != Blocks.GRAVEL && var6 != Blocks.SUSPICIOUS_GRAVEL && var6 != Blocks.CLAY && !(var6 instanceof ConcretePowderBlock)) {
         if (var6 != Blocks.SNOW && var6 != Blocks.SNOW_BLOCK && var6 != Blocks.POWDER_SNOW_CAULDRON && !(var6 instanceof SnowLayerBlock)) {
            if (!(var6 instanceof IceBlock) && var6 != Blocks.BLUE_ICE && var6 != Blocks.PACKED_ICE && var6 != Blocks.MOSS_BLOCK && var6 != Blocks.MOSS_CARPET && !(var6 instanceof WetSpongeBlock)) {
               if (var6 != Blocks.WATER && var6 != Blocks.WATER_CAULDRON) {
                  if (var6 != Blocks.LAVA && var6 != Blocks.LAVA_CAULDRON) {
                     if (!(var6 instanceof BasePressurePlateBlock) && !(var6 instanceof CarpetBlock)) {
                        VoxelShape var8 = var3.getCollisionShape(var5, var2);
                        return !var8.isEmpty() ? EnumSurface.SOLID : EnumSurface.UNKNOWN;
                     } else {
                        return EnumSurface.UNKNOWN;
                     }
                  } else {
                     return EnumSurface.LAVA;
                  }
               } else {
                  return EnumSurface.WATER;
               }
            } else {
               return EnumSurface.SLIPPERY;
            }
         } else {
            return EnumSurface.SNOWY;
         }
      } else {
         return EnumSurface.DUSTY;
      }
   }

   private boolean isSlipperyWhenRaining(Block var1) {
      return this.isGlass(var1) || var1 == Blocks.DIRT || var1 == Blocks.COARSE_DIRT || var1 == Blocks.DIRT_PATH || var1 == Blocks.ROOTED_DIRT || var1 instanceof GrassBlock || var1 instanceof TallGrassBlock || var1 instanceof LeavesBlock;
   }

   private boolean isGlass(Block var1) {
      if (!(var1 instanceof StainedGlassBlock) && !(var1 instanceof StainedGlassPaneBlock) && !(var1 instanceof TintedGlassBlock)) {
         return var1 == Blocks.GLASS || var1 == Blocks.GLASS_PANE;
      } else {
         return true;
      }
   }
}
