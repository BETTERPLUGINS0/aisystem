package com.volmit.iris.engine.actuator;

import com.volmit.iris.engine.decorator.IrisCeilingDecorator;
import com.volmit.iris.engine.decorator.IrisSeaFloorDecorator;
import com.volmit.iris.engine.decorator.IrisSeaSurfaceDecorator;
import com.volmit.iris.engine.decorator.IrisShoreLineDecorator;
import com.volmit.iris.engine.decorator.IrisSurfaceDecorator;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedActuator;
import com.volmit.iris.engine.framework.EngineDecorator;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.function.Predicate;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class IrisDecorantActuator extends EngineAssignedActuator<BlockData> {
   private static final Predicate<BlockData> PREDICATE_SOLID = (var0) -> {
      return var0 != null && !var0.getMaterial().isAir() && !var0.getMaterial().equals(Material.WATER) && !var0.getMaterial().equals(Material.LAVA);
   };
   private final RNG rng;
   private final EngineDecorator surfaceDecorator;
   private final EngineDecorator ceilingDecorator;
   private final EngineDecorator seaSurfaceDecorator;
   private final EngineDecorator seaFloorDecorator;
   private final EngineDecorator shoreLineDecorator;
   private final boolean shouldRay = this.shouldRayDecorate();

   public IrisDecorantActuator(Engine engine) {
      super(var1, "Decorant");
      this.rng = new RNG(var1.getSeedManager().getDecorator());
      this.surfaceDecorator = new IrisSurfaceDecorator(this.getEngine());
      this.ceilingDecorator = new IrisCeilingDecorator(this.getEngine());
      this.seaSurfaceDecorator = new IrisSeaSurfaceDecorator(this.getEngine());
      this.shoreLineDecorator = new IrisShoreLineDecorator(this.getEngine());
      this.seaFloorDecorator = new IrisSeaFloorDecorator(this.getEngine());
   }

   @BlockCoordinates
   public void onActuate(int x, int z, Hunk<BlockData> output, boolean multicore, ChunkContext context) {
      if (this.getEngine().getDimension().isDecorate()) {
         PrecisionStopwatch var6 = PrecisionStopwatch.start();

         for(int var7 = 0; var7 < var3.getWidth(); ++var7) {
            int var9 = Math.round((float)(var1 + var7));

            for(int var13 = 0; var13 < var3.getDepth(); ++var13) {
               int var15 = 0;
               int var16 = 0;
               int var10 = Math.round((float)(var2 + var13));
               int var8 = (int)Math.round((Double)var5.getHeight().get(var7, var13));
               IrisBiome var11 = (IrisBiome)var5.getBiome().get(var7, var13);
               IrisBiome var12 = this.shouldRay ? (IrisBiome)var5.getCave().get(var7, var13) : null;
               if (!var11.getDecorators().isEmpty() || var12 != null && !var12.getDecorators().isEmpty()) {
                  if (var8 < this.getDimension().getFluidHeight()) {
                     this.getSeaSurfaceDecorator().decorate(var7, var13, var9, Math.round((float)(var7 + 1)), Math.round((float)(var1 + var7 - 1)), var10, Math.round((float)(var2 + var13 + 1)), Math.round((float)(var2 + var13 - 1)), var3, var11, this.getDimension().getFluidHeight(), this.getEngine().getHeight());
                     this.getSeaFloorDecorator().decorate(var7, var13, var9, var10, var3, var11, var8 + 1, this.getDimension().getFluidHeight() + 1);
                  }

                  if (var8 == this.getDimension().getFluidHeight()) {
                     this.getShoreLineDecorator().decorate(var7, var13, var9, Math.round((float)(var1 + var7 + 1)), Math.round((float)(var1 + var7 - 1)), var10, Math.round((float)(var2 + var13 + 1)), Math.round((float)(var2 + var13 - 1)), var3, var11, var8, this.getEngine().getHeight());
                  }

                  this.getSurfaceDecorator().decorate(var7, var13, var9, var10, var3, var11, var8, this.getEngine().getHeight() - var8);
                  if (var12 != null && var12.getDecorators().isNotEmpty()) {
                     for(int var17 = var8; var17 > 0; --var17) {
                        boolean var14 = PREDICATE_SOLID.test((BlockData)var3.get(var7, var17, var13));
                        if (var14) {
                           if (var15 > 0) {
                              this.getSurfaceDecorator().decorate(var7, var13, var9, var10, var3, var12, var17, var16);
                              this.getCeilingDecorator().decorate(var7, var13, var9, var10, var3, var12, var16 - 1, var15);
                              var15 = 0;
                           }

                           var16 = var17;
                        } else {
                           ++var15;
                        }
                     }
                  }
               }
            }
         }

         this.getEngine().getMetrics().getDecoration().put(var6.getMilliseconds());
      }
   }

   private boolean shouldRayDecorate() {
      return false;
   }

   @Generated
   public EngineDecorator getSurfaceDecorator() {
      return this.surfaceDecorator;
   }

   @Generated
   public EngineDecorator getCeilingDecorator() {
      return this.ceilingDecorator;
   }

   @Generated
   public EngineDecorator getSeaSurfaceDecorator() {
      return this.seaSurfaceDecorator;
   }

   @Generated
   public EngineDecorator getSeaFloorDecorator() {
      return this.seaFloorDecorator;
   }

   @Generated
   public EngineDecorator getShoreLineDecorator() {
      return this.shoreLineDecorator;
   }
}
