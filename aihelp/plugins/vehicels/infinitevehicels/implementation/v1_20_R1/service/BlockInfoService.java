package implementation.v1_20_R1.service;

import advancedplugins.pm2.cv.api.enums.EnumSurface;
import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockAir;
import net.minecraft.world.level.block.BlockCarpet;
import net.minecraft.world.level.block.BlockConcretePowder;
import net.minecraft.world.level.block.BlockGlass;
import net.minecraft.world.level.block.BlockGrass;
import net.minecraft.world.level.block.BlockGravel;
import net.minecraft.world.level.block.BlockIce;
import net.minecraft.world.level.block.BlockLeaves;
import net.minecraft.world.level.block.BlockLongGrass;
import net.minecraft.world.level.block.BlockPressurePlateAbstract;
import net.minecraft.world.level.block.BlockSand;
import net.minecraft.world.level.block.BlockSlowSand;
import net.minecraft.world.level.block.BlockSnow;
import net.minecraft.world.level.block.BlockStainedGlass;
import net.minecraft.world.level.block.BlockWetSponge;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

public class BlockInfoService implements advancedplugins.pm2.cv.api.service.BlockInfoService {
   public boolean isCanStandOnSurfaceAt(World var1, int var2, int var3, int var4) {
      BlockPosition var5 = new BlockPosition(var2, var3, var4);
      WorldServer var6 = ((CraftWorld)var1).getHandle();
      IBlockData var7 = var6.l(var5).a_(var5);
      Block var8 = var7.b();
      if (!(var8 instanceof BlockSnow) && !(var8 instanceof CarpetBlock) && !(var8 instanceof BlockPressurePlateAbstract)) {
         VoxelShape var9 = var7.k(var6, var5);
         return var9 != null && !var9.b();
      } else {
         return false;
      }
   }

   public EnumSurface getSurfaceTypeAt(World var1, int var2, int var3, int var4, boolean var5) {
      BlockPosition var6 = new BlockPosition(var2, var3, var4);
      IBlockData var7 = ((CraftWorld)var1).getHandle().l(var6).a_(var6);
      return this.getSurfaceType(var1, var6, var7, var5);
   }

   public advancedplugins.pm2.cv.api.service.BlockInfoService.SurfaceResult getSurfaceTypesAt(World var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8) {
      Preconditions.checkArgument(var3 <= var5, "minX must be <= maxX");
      Preconditions.checkArgument(var4 <= var6, "minZ must be <= maxZ");
      int var9 = var5 - var3 + 1;
      int var10 = var6 - var4 + 1;
      EnumSurface[] var11 = new EnumSurface[var9 * var10];
      WorldServer var12 = ((CraftWorld)var1).getHandle();
      IChunkAccess var13 = null;
      byte var14 = 0;
      byte var15 = 0;

      for(int var16 = 0; var16 < var9; ++var16) {
         for(int var17 = 0; var17 < var10; ++var17) {
            int var18 = var3 + var16;
            int var19 = var4 + var17;
            int var20 = var18 >> 4;
            int var21 = var19 >> 4;
            if (var13 == null || var14 != var20 || var15 != var21) {
               var13 = var12.a(var20, var21, ChunkStatus.h);
            }

            BlockPosition var22 = new BlockPosition(var18, var2, var19);
            if (!var7) {
               BlockPosition var23 = new BlockPosition(var18, var2 + 1, var19);
               IBlockData var24 = var13.a_(var23);
               Block var25 = var24.b();
               if (var25 instanceof BlockSnow || var25 instanceof BlockCarpet || var25 instanceof BlockPressurePlateAbstract) {
                  var22 = var23;
               }
            }

            var11[var16 * var10 + var17] = this.getSurfaceType(var1, var22, var13.a_(var22), var8);
         }
      }

      return new advancedplugins.pm2.cv.api.service.BlockInfoService.SurfaceResult(var3, var4, var5, var6, var11);
   }

   private EnumSurface getSurfaceType(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      WorldServer var5 = ((CraftWorld)var1).getHandle();
      Block var6 = var3.b();
      boolean var7 = !var1.isClearWeather();
      if (var6 instanceof BlockAir) {
         return EnumSurface.EMPTY;
      } else if (var7 && this.isSlipperyWhenRaining(var6)) {
         return EnumSurface.SLIPPERY;
      } else if (!(var6 instanceof BlockSand) && !(var6 instanceof BlockSlowSand) && var6 != Blocks.J && !(var6 instanceof BlockGravel) && var6 != Blocks.M && var6 != Blocks.dR && !(var6 instanceof BlockConcretePowder)) {
         if (var6 != Blocks.dN && var6 != Blocks.dP && var6 != Blocks.fw && !(var6 instanceof BlockSnow)) {
            if (!(var6 instanceof BlockIce) && var6 != Blocks.mW && var6 != Blocks.iC && var6 != Blocks.rB && var6 != Blocks.rz && !(var6 instanceof BlockWetSponge)) {
               if (var6 != Blocks.G && var6 != Blocks.fu) {
                  if (var6 != Blocks.H && var6 != Blocks.fv) {
                     if (!(var6 instanceof BlockPressurePlateAbstract) && !(var6 instanceof CarpetBlock)) {
                        VoxelShape var8 = var3.k(var5, var2);
                        return !var8.b() ? EnumSurface.SOLID : EnumSurface.UNKNOWN;
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
      return var1 instanceof BlockGlass || var1 instanceof TintedGlassBlock || var1 instanceof BlockStainedGlass || var1 == Blocks.eZ || var1 == Blocks.j || var1 == Blocks.k || var1 == Blocks.kE || var1 == Blocks.rG || var1 instanceof BlockGrass || var1 instanceof BlockLongGrass || var1 instanceof BlockLeaves;
   }
}
