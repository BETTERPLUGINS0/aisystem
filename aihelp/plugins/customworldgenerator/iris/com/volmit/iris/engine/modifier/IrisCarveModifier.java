package com.volmit.iris.engine.modifier;

import com.volmit.iris.engine.actuator.IrisDecorantActuator;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedModifier;
import com.volmit.iris.engine.object.InferredType;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDecorationPart;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.matter.slices.MarkerMatter;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class IrisCarveModifier extends EngineAssignedModifier<BlockData> {
   private final RNG rng;
   private final BlockData AIR;
   private final BlockData LAVA;
   private final IrisDecorantActuator decorant;

   public IrisCarveModifier(Engine engine) {
      super(var1, "Carve");
      this.AIR = Material.CAVE_AIR.createBlockData();
      this.LAVA = Material.LAVA.createBlockData();
      this.rng = new RNG(this.getEngine().getSeedManager().getCarve());
      this.decorant = new IrisDecorantActuator(var1);
   }

   @ChunkCoordinates
   public void onModify(int x, int z, Hunk<BlockData> output, boolean multicore, ChunkContext context) {
      PrecisionStopwatch var6 = PrecisionStopwatch.start();
      Mantle var7 = this.getEngine().getMantle().getMantle();
      MantleChunk var8 = var7.getChunk(var1, var2).use();
      KMap var9 = new KMap();
      KMap var10 = new KMap();
      Consumer4 var11 = (var6x, var7x, var8x, var9x) -> {
         if (var9x != null) {
            if (var7x < this.getEngine().getWorld().maxHeight() - this.getEngine().getWorld().minHeight() && var7x > 0) {
               int var10x = var6x & 15;
               int var11 = var8x & 15;
               BlockData var12 = (BlockData)var3.get(var10x, var7x, var11);
               if (!B.isFluid(var12)) {
                  ((KList)var9.computeIfAbsent(Cache.key(var10x, var11), (var0) -> {
                     return new KList();
                  })).qadd(var7x);
                  if (var11 < 15 && var8.get(var6x, var7x, var8x + 1, MatterCavern.class) == null) {
                     var10.put(new IrisPosition(var10x, var7x, var11 + 1), var9x);
                  }

                  if (var10x < 15 && var8.get(var6x + 1, var7x, var8x, MatterCavern.class) == null) {
                     var10.put(new IrisPosition(var10x + 1, var7x, var11), var9x);
                  }

                  if (var11 > 0 && var8.get(var6x, var7x, var8x - 1, MatterCavern.class) == null) {
                     var10.put(new IrisPosition(var10x, var7x, var11 - 1), var9x);
                  }

                  if (var10x > 0 && var8.get(var6x - 1, var7x, var8x, MatterCavern.class) == null) {
                     var10.put(new IrisPosition(var10x - 1, var7x, var11), var9x);
                  }

                  if (!var12.getMaterial().isAir()) {
                     if (var9x.isWater()) {
                        var3.set(var10x, var7x, var11, (BlockData)var5.getFluid().get(var10x, var11));
                     } else if (var9x.isLava()) {
                        var3.set(var10x, var7x, var11, this.LAVA);
                     } else if (this.getEngine().getDimension().getCaveLavaHeight() > var7x) {
                        var3.set(var10x, var7x, var11, this.LAVA);
                     } else {
                        var3.set(var10x, var7x, var11, this.AIR);
                     }

                  }
               }
            }
         }
      };
      var8.iterate(MatterCavern.class, var11);
      var10.forEach((var5x, var6x) -> {
         IrisBiome var7 = var6x.getCustomBiome().isEmpty() ? this.getEngine().getCaveBiome(var5x.getX() + (var1 << 4), var5x.getZ() + (var2 << 4)) : (IrisBiome)this.getEngine().getData().getBiomeLoader().load(var6x.getCustomBiome());
         if (var7 != null) {
            var7.setInferredType(InferredType.CAVE);
            BlockData var8 = var7.getWall().get(this.rng, (double)(var5x.getX() + (var1 << 4)), (double)var5x.getY(), (double)(var5x.getZ() + (var2 << 4)), this.getData());
            if (var8 != null && B.isSolid((BlockData)var3.get(var5x.getX(), var5x.getY(), var5x.getZ())) && (double)var5x.getY() <= (Double)var5.getHeight().get(var5x.getX(), var5x.getZ())) {
               var3.set(var5x.getX(), var5x.getY(), var5x.getZ(), var8);
            }
         }

      });
      var9.forEach((var6x, var7x) -> {
         if (!var7x.isEmpty()) {
            int var8x = Cache.keyX(var6x);
            int var9 = Cache.keyZ(var6x);
            var7x.sort(Integer::compare);
            IrisCarveModifier.CaveZone var10 = new IrisCarveModifier.CaveZone();
            var10.setFloor((Integer)var7x.get(0));
            int var11 = (Integer)var7x.get(0) - 1;
            Iterator var12 = var7x.iterator();

            while(var12.hasNext()) {
               Integer var13 = (Integer)var12.next();
               if (var13 >= 0 && var13 <= this.getEngine().getHeight()) {
                  if (var13 == var11 + 1) {
                     var11 = var13;
                     var10.ceiling = var11;
                  } else if (var10.isValid(this.getEngine())) {
                     this.processZone(var3, var8, var7, var10, var8x, var9, var8x + (var1 << 4), var9 + (var2 << 4));
                     var10 = new IrisCarveModifier.CaveZone();
                     var10.setFloor(var13);
                     var11 = var13;
                  }
               }
            }

            if (var10.isValid(this.getEngine())) {
               this.processZone(var3, var8, var7, var10, var8x, var9, var8x + (var1 << 4), var9 + (var2 << 4));
            }

         }
      });
      this.getEngine().getMetrics().getDeposit().put(var6.getMilliseconds());
      var8.release();
   }

   private void processZone(Hunk<BlockData> output, MantleChunk mc, Mantle mantle, IrisCarveModifier.CaveZone zone, int rx, int rz, int xx, int zz) {
      boolean var9 = B.isSolid((BlockData)var1.getClosest(var5, var4.floor - 1, var6));
      boolean var10 = B.isSolid((BlockData)var1.getClosest(var5, var4.ceiling + 1, var6));
      int var11 = (var4.floor + var4.ceiling) / 2;
      int var12 = var4.airThickness();
      String var13 = "";
      if (B.isDecorant((BlockData)var1.getClosest(var5, var4.ceiling + 1, var6))) {
         var1.set(var5, var4.ceiling + 1, var6, this.AIR);
      }

      if (B.isDecorant((BlockData)var1.get(var5, var4.ceiling, var6))) {
         var1.set(var5, var4.ceiling, var6, this.AIR);
      }

      if (M.r(0.0625D)) {
         var3.set(var7, var4.ceiling, var8, (Object)MarkerMatter.CAVE_CEILING);
      }

      if (M.r(0.0625D)) {
         var3.set(var7, var4.floor, var8, (Object)MarkerMatter.CAVE_FLOOR);
      }

      for(int var14 = var4.floor; var14 <= var4.ceiling; ++var14) {
         MatterCavern var15 = (MatterCavern)var2.getOrCreate(var14 >> 4).slice(MatterCavern.class).get(var5, var14 & 15, var6);
         if (var15 != null && !var15.getCustomBiome().isEmpty()) {
            var13 = var15.getCustomBiome();
            break;
         }
      }

      IrisBiome var20 = var13.isEmpty() ? this.getEngine().getCaveBiome(var7, var8) : (IrisBiome)this.getEngine().getData().getBiomeLoader().load(var13);
      if (var20 != null) {
         var20.setInferredType(InferredType.CAVE);
         KList var21 = var20.generateLayers(this.getDimension(), (double)var7, (double)var8, this.rng, 3, var4.floor, this.getData(), this.getComplex());

         int var16;
         BlockData var18;
         for(var16 = 0; var16 < var4.floor - 1 && var21.hasIndex(var16); ++var16) {
            int var17 = var4.floor - var16 - 1;
            var18 = (BlockData)var21.get(var16);
            BlockData var19 = (BlockData)var1.get(var5, var17, var6);
            if (B.isSolid(var19)) {
               if (B.isOre(var19)) {
                  var1.set(var5, var17, var6, B.toDeepSlateOre(var19, var18));
               } else {
                  var1.set(var5, var17, var6, (BlockData)var21.get(var16));
               }
            }
         }

         var21 = var20.generateCeilingLayers(this.getDimension(), (double)var7, (double)var8, this.rng, 3, var4.ceiling, this.getData(), this.getComplex());
         if (var4.ceiling + 1 < var3.getWorldHeight()) {
            for(var16 = 0; var16 < var4.ceiling + 1 && var21.hasIndex(var16); ++var16) {
               BlockData var23 = (BlockData)var21.get(var16);
               var18 = (BlockData)var1.get(var5, var4.ceiling + var16 + 1, var6);
               if (B.isSolid(var18)) {
                  if (B.isOre(var18)) {
                     var1.set(var5, var4.ceiling + var16 + 1, var6, B.toDeepSlateOre(var18, var23));
                  } else {
                     var1.set(var5, var4.ceiling + var16 + 1, var6, var23);
                  }
               }
            }
         }

         Iterator var22 = var20.getDecorators().iterator();

         while(true) {
            while(var22.hasNext()) {
               IrisDecorator var24 = (IrisDecorator)var22.next();
               if (var24.getPartOf().equals(IrisDecorationPart.NONE) && B.isSolid((BlockData)var1.get(var5, var4.getFloor() - 1, var6))) {
                  this.decorant.getSurfaceDecorator().decorate(var5, var6, var7, var7, var7, var8, var8, var8, var1, var20, var4.getFloor() - 1, var4.airThickness());
               } else if (var24.getPartOf().equals(IrisDecorationPart.CEILING) && B.isSolid((BlockData)var1.get(var5, var4.getCeiling() + 1, var6))) {
                  this.decorant.getCeilingDecorator().decorate(var5, var6, var7, var7, var7, var8, var8, var8, var1, var20, var4.getCeiling(), var4.airThickness());
               }
            }

            return;
         }
      }
   }

   public static class CaveZone {
      private int ceiling = -1;
      private int floor = -1;

      public int airThickness() {
         return this.ceiling - this.floor - 1;
      }

      public boolean isValid(Engine engine) {
         return this.floor < this.ceiling && this.ceiling - this.floor >= 1 && this.floor >= 0 && this.ceiling <= var1.getHeight() && this.airThickness() > 0;
      }

      public String toString() {
         return this.floor + "-" + this.ceiling;
      }

      @Generated
      public int getCeiling() {
         return this.ceiling;
      }

      @Generated
      public int getFloor() {
         return this.floor;
      }

      @Generated
      public void setCeiling(final int ceiling) {
         this.ceiling = var1;
      }

      @Generated
      public void setFloor(final int floor) {
         this.floor = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisCarveModifier.CaveZone)) {
            return false;
         } else {
            IrisCarveModifier.CaveZone var2 = (IrisCarveModifier.CaveZone)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getCeiling() != var2.getCeiling()) {
               return false;
            } else {
               return this.getFloor() == var2.getFloor();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisCarveModifier.CaveZone;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + this.getCeiling();
         var3 = var3 * 59 + this.getFloor();
         return var3;
      }
   }
}
