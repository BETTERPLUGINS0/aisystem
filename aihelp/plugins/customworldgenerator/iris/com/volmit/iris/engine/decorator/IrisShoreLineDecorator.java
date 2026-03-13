package com.volmit.iris.engine.decorator;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDecorationPart;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import org.bukkit.block.data.BlockData;

public class IrisShoreLineDecorator extends IrisEngineDecorator {
   public IrisShoreLineDecorator(Engine engine) {
      super(var1, "Shore Line", IrisDecorationPart.SHORE_LINE);
   }

   @BlockCoordinates
   public void decorate(int x, int z, int realX, int realX1, int realX_1, int realZ, int realZ1, int realZ_1, Hunk<BlockData> data, IrisBiome biome, int height, int max) {
      if (var11 == this.getDimension().getFluidHeight() && ((double)Math.round((Double)this.getComplex().getHeightStream().get((double)var4, (double)var6)) < this.getComplex().getFluidHeight() || (double)Math.round((Double)this.getComplex().getHeightStream().get((double)var5, (double)var6)) < this.getComplex().getFluidHeight() || (double)Math.round((Double)this.getComplex().getHeightStream().get((double)var3, (double)var7)) < this.getComplex().getFluidHeight() || (double)Math.round((Double)this.getComplex().getHeightStream().get((double)var3, (double)var8)) < this.getComplex().getFluidHeight())) {
         RNG var13 = this.getRNG(var3, var6);
         IrisDecorator var14 = this.getDecorator(var13, var10, (double)var3, (double)var6);
         if (var14 != null) {
            if (!var14.isForcePlace() && !var14.getSlopeCondition().isDefault() && !var14.getSlopeCondition().isValid((Double)this.getComplex().getSlopeStream().get((double)var3, (double)var6))) {
               return;
            }

            if (!var14.isStacking()) {
               var9.set(var1, var11 + 1, var2, var14.getBlockData100(var10, var13, (double)var3, (double)var11, (double)var6, this.getData()));
            } else {
               int var15 = var14.getHeight(var13, (double)var3, (double)var6, this.getData());
               int var16;
               if (var14.isScaleStack()) {
                  var16 = var12 - var11;
                  var15 = (int)Math.ceil((double)var16 * ((double)var15 / 100.0D));
               } else {
                  var15 = Math.min(var12 - var11, var15);
               }

               if (var15 == 1) {
                  var9.set(var1, var11, var2, var14.getBlockDataForTop(var10, var13, (double)var3, (double)var11, (double)var6, this.getData()));
                  return;
               }

               for(var16 = 0; var16 < var15; ++var16) {
                  int var17 = var11 + var16;
                  double var18 = (double)var16 / (double)(var15 - 1);
                  var9.set(var1, var17 + 1, var2, var18 >= var14.getTopThreshold() ? var14.getBlockDataForTop(var10, var13, (double)var3, (double)var17, (double)var6, this.getData()) : var14.getBlockData100(var10, var13, (double)var3, (double)var17, (double)var6, this.getData()));
               }
            }
         }
      }

   }
}
