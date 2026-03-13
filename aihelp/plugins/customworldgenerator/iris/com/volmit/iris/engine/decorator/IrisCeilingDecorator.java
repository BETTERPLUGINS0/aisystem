package com.volmit.iris.engine.decorator;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDecorationPart;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.PointedDripstone.Thickness;

public class IrisCeilingDecorator extends IrisEngineDecorator {
   public IrisCeilingDecorator(Engine engine) {
      super(var1, "Ceiling", IrisDecorationPart.CEILING);
   }

   @BlockCoordinates
   public void decorate(int x, int z, int realX, int realX1, int realX_1, int realZ, int realZ1, int realZ_1, Hunk<BlockData> data, IrisBiome biome, int height, int max) {
      RNG var13 = this.getRNG(var3, var6);
      IrisDecorator var14 = this.getDecorator(var13, var10, (double)var3, (double)var6);
      if (var14 != null) {
         if (!var14.isStacking()) {
            var9.set(var1, var11, var2, this.fixFaces(var14.getBlockData100(var10, var13, (double)var3, (double)var11, (double)var6, this.getData()), var9, var1, var2, var3, var11, var6));
         } else {
            int var15 = var14.getHeight(var13, (double)var3, (double)var6, this.getData());
            if (var14.isScaleStack()) {
               var15 = Math.min((int)Math.ceil((double)var12 * ((double)var15 / 100.0D)), var14.getAbsoluteMaxStack());
            } else {
               var15 = Math.min(var12, var15);
            }

            if (var15 == 1) {
               var9.set(var1, var11, var2, var14.getBlockDataForTop(var10, var13, (double)var3, (double)var11, (double)var6, this.getData()));
               return;
            }

            for(int var16 = 0; var16 < var15; ++var16) {
               int var17 = var11 - var16;
               if (var17 >= this.getEngine().getMinHeight()) {
                  double var18 = (double)var16 / (double)(var15 - 1);
                  BlockData var20 = var18 >= var14.getTopThreshold() ? var14.getBlockDataForTop(var10, var13, (double)var3, (double)var17, (double)var6, this.getData()) : var14.getBlockData100(var10, var13, (double)var3, (double)var17, (double)var6, this.getData());
                  if (var20 instanceof PointedDripstone) {
                     Thickness var21 = Thickness.BASE;
                     if (var15 == 2) {
                        var21 = Thickness.FRUSTUM;
                        if (var16 == var15 - 1) {
                           var21 = Thickness.TIP;
                        }
                     } else if (var16 == var15 - 1) {
                        var21 = Thickness.TIP;
                     } else if (var16 == var15 - 2) {
                        var21 = Thickness.FRUSTUM;
                     }

                     var20 = Material.POINTED_DRIPSTONE.createBlockData();
                     ((PointedDripstone)var20).setThickness(var21);
                     ((PointedDripstone)var20).setVerticalDirection(BlockFace.DOWN);
                  }

                  var9.set(var1, var17, var2, var20);
               }
            }
         }
      }

   }
}
