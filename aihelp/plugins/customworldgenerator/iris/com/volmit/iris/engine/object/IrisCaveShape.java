package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import lombok.Generated;

@Snippet("cave-shape")
@Desc("Cave Shape")
public class IrisCaveShape {
   private final transient KMap<IrisPosition, KSet<IrisPosition>> cache = new KMap();
   @Desc("Noise used for the shape of the cave")
   private IrisGeneratorStyle noise = new IrisGeneratorStyle();
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The threshold for noise mask")
   private double noiseThreshold = -1.0D;
   @RegistryListResource(IrisObject.class)
   @Desc("Object used as mask for the shape of the cave")
   private String object = null;
   @Desc("Rotation to apply to objects before using them as mask")
   private IrisObjectRotation objectRotation = new IrisObjectRotation();

   public CNG getNoise(RNG rng, Engine engine) {
      return this.noise.create(var1, var2.getData());
   }

   public KSet<IrisPosition> getMasked(RNG rng, Engine engine) {
      return this.object == null ? null : (KSet)this.cache.computeIfAbsent(this.randomRotation(var1), (var2x) -> {
         KSet var3 = new KSet(new IrisPosition[0]);
         ((IrisObject)var2.getData().getObjectLoader().load(this.object)).getBlocks().forEach((var3x, var4) -> {
            if (!var4.getMaterial().isAir()) {
               var3.add(new IrisPosition(this.objectRotation.rotate(var3x, var2x.getX(), var2x.getY(), var2x.getZ())));
            }
         });
         return var3;
      });
   }

   private IrisPosition randomRotation(RNG rng) {
      return this.objectRotation != null && this.objectRotation.canRotate() ? new IrisPosition(this.randomDegree(var1, this.objectRotation.getXAxis()), this.randomDegree(var1, this.objectRotation.getYAxis()), this.randomDegree(var1, this.objectRotation.getZAxis())) : new IrisPosition(0, 0, 0);
   }

   private int randomDegree(RNG rng, IrisAxisRotationClamp clamp) {
      if (!var2.isEnabled()) {
         return 0;
      } else if (var2.isLocked()) {
         return (int)var2.getMax();
      } else {
         double var3 = var2.getInterval();
         if (var3 < 1.0D) {
            var3 = 1.0D;
         }

         double var5 = var2.getMin();
         double var7 = var2.getMax();
         double var9 = var3 * Math.ceil(Math.abs(var1.d(0.0D, 360.0D) / var3)) % 360.0D;
         if (var2.isUnlimited()) {
            return (int)var9;
         } else {
            if (var5 > var7) {
               var7 = var2.getMin();
               var5 = var2.getMax();
            }

            return (int)(Double)M.clip(var9, var5, var7);
         }
      }
   }

   @Generated
   public IrisCaveShape() {
   }

   @Generated
   public IrisCaveShape(final IrisGeneratorStyle noise, final double noiseThreshold, final String object, final IrisObjectRotation objectRotation) {
      this.noise = var1;
      this.noiseThreshold = var2;
      this.object = var4;
      this.objectRotation = var5;
   }

   @Generated
   public KMap<IrisPosition, KSet<IrisPosition>> getCache() {
      return this.cache;
   }

   @Generated
   public IrisGeneratorStyle getNoise() {
      return this.noise;
   }

   @Generated
   public double getNoiseThreshold() {
      return this.noiseThreshold;
   }

   @Generated
   public String getObject() {
      return this.object;
   }

   @Generated
   public IrisObjectRotation getObjectRotation() {
      return this.objectRotation;
   }

   @Generated
   public IrisCaveShape setNoise(final IrisGeneratorStyle noise) {
      this.noise = var1;
      return this;
   }

   @Generated
   public IrisCaveShape setNoiseThreshold(final double noiseThreshold) {
      this.noiseThreshold = var1;
      return this;
   }

   @Generated
   public IrisCaveShape setObject(final String object) {
      this.object = var1;
      return this;
   }

   @Generated
   public IrisCaveShape setObjectRotation(final IrisObjectRotation objectRotation) {
      this.objectRotation = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCaveShape)) {
         return false;
      } else {
         IrisCaveShape var2 = (IrisCaveShape)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getNoiseThreshold(), var2.getNoiseThreshold()) != 0) {
            return false;
         } else {
            label49: {
               IrisGeneratorStyle var3 = this.getNoise();
               IrisGeneratorStyle var4 = var2.getNoise();
               if (var3 == null) {
                  if (var4 == null) {
                     break label49;
                  }
               } else if (var3.equals(var4)) {
                  break label49;
               }

               return false;
            }

            String var5 = this.getObject();
            String var6 = var2.getObject();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisObjectRotation var7 = this.getObjectRotation();
            IrisObjectRotation var8 = var2.getObjectRotation();
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
      return var1 instanceof IrisCaveShape;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getNoiseThreshold());
      int var8 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      IrisGeneratorStyle var5 = this.getNoise();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getObject();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisObjectRotation var7 = this.getObjectRotation();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCache());
      return "IrisCaveShape(cache=" + var10000 + ", noise=" + String.valueOf(this.getNoise()) + ", noiseThreshold=" + this.getNoiseThreshold() + ", object=" + this.getObject() + ", objectRotation=" + String.valueOf(this.getObjectRotation()) + ")";
   }
}
