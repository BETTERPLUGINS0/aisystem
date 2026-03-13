package com.volmit.iris.engine.framework;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.util.parallel.MultiBurst;
import lombok.Generated;

public class EngineTarget {
   private final MultiBurst burster;
   private final IrisData data;
   private IrisDimension dimension;
   private IrisWorld world;

   public EngineTarget(IrisWorld world, IrisDimension dimension, IrisData data) {
      this.world = var1;
      this.dimension = var2;
      this.data = var3;
      this.burster = MultiBurst.burst;
   }

   public int getHeight() {
      return this.world.maxHeight() - this.world.minHeight();
   }

   public void close() {
   }

   @Generated
   public MultiBurst getBurster() {
      return this.burster;
   }

   @Generated
   public IrisData getData() {
      return this.data;
   }

   @Generated
   public IrisDimension getDimension() {
      return this.dimension;
   }

   @Generated
   public IrisWorld getWorld() {
      return this.world;
   }

   @Generated
   public void setDimension(final IrisDimension dimension) {
      this.dimension = var1;
   }

   @Generated
   public void setWorld(final IrisWorld world) {
      this.world = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof EngineTarget)) {
         return false;
      } else {
         EngineTarget var2 = (EngineTarget)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               MultiBurst var3 = this.getBurster();
               MultiBurst var4 = var2.getBurster();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            IrisDimension var5 = this.getDimension();
            IrisDimension var6 = var2.getDimension();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisWorld var7 = this.getWorld();
            IrisWorld var8 = var2.getWorld();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof EngineTarget;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      MultiBurst var3 = this.getBurster();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisDimension var4 = this.getDimension();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisWorld var5 = this.getWorld();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBurster());
      return "EngineTarget(burster=" + var10000 + ", dimension=" + String.valueOf(this.getDimension()) + ", world=" + String.valueOf(this.getWorld()) + ")";
   }
}
