package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import lombok.Generated;

@Snippet("worm")
@Desc("Generate worms")
public class IrisWorm {
   @Desc("The style used to determine the curvature of this worm's x")
   private IrisShapedGeneratorStyle xStyle;
   @Desc("The style used to determine the curvature of this worm's y")
   private IrisShapedGeneratorStyle yStyle;
   @Desc("The style used to determine the curvature of this worm's z")
   private IrisShapedGeneratorStyle zStyle;
   @Desc("The max block distance this worm can travel from its start. This can have performance implications at ranges over 1,000 blocks but it's not too serious, test.")
   private int maxDistance;
   @Desc("The iterations this worm can make")
   private int maxIterations;
   @Desc("By default if a worm loops back into itself, it stops at that point and does not continue. This is an optimization, to prevent this turn this option on.")
   private boolean allowLoops;
   @Desc("The thickness of the worms. Each individual worm has the same thickness while traveling however, each spawned worm will vary in thickness.")
   private IrisStyledRange girth;

   public KList<IrisPosition> generate(RNG rng, IrisData data, MantleWriter writer, IrisRange verticalRange, int x, int y, int z, boolean breakSurface, double distance) {
      int var11 = this.maxIterations;
      double var18 = (double)var5;
      double var20 = (double)var6;
      double var22 = (double)var7;
      IrisPosition var24 = new IrisPosition(var5, var6, var7);
      KList var25 = new KList();
      KSet var26 = this.allowLoops ? null : new KSet(new IrisPosition[0]);
      CNG var27 = this.xStyle.getGenerator().create(var1.nextParallelRNG(14567), var2);
      CNG var28 = this.yStyle.getGenerator().create(var1.nextParallelRNG(64789), var2);
      CNG var29 = this.zStyle.getGenerator().create(var1.nextParallelRNG(34790), var2);

      while(var11-- > 0) {
         IrisPosition var30 = new IrisPosition((double)Math.round(var18), (double)Math.round(var20), (double)Math.round(var22));
         var25.add((Object)var30);
         if (var26 != null) {
            var26.add(var30);
         }

         double var12 = var27.fitDouble((double)this.xStyle.getMin(), (double)this.xStyle.getMax(), var18, var20, var22);
         double var14 = var28.fitDouble((double)this.yStyle.getMin(), (double)this.yStyle.getMax(), var18, var20, var22);
         double var16 = var29.fitDouble((double)this.zStyle.getMin(), (double)this.zStyle.getMax(), var18, var20, var22);
         var18 += var12;
         var20 += var14;
         var22 += var16;
         IrisPosition var31 = new IrisPosition((double)Math.round(var18), (double)Math.round(var20), (double)Math.round(var22));
         if (!var8 && (double)var3.getEngineMantle().getHighest(var31.getX(), var31.getZ(), true) <= (double)var31.getY() + var9 || var4 != null && !var4.contains(var31.getY()) || !var3.isWithin((int)Math.round(var18), var4 != null ? (int)Math.round(var20) : 5, (int)Math.round(var22)) || var31.isLongerThan(var24, this.maxDistance) || var26 != null && var26.contains(var31)) {
            break;
         }
      }

      return var25;
   }

   @Generated
   public IrisWorm() {
      this.xStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, -2, 2);
      this.yStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, -2, 2);
      this.zStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, -2, 2);
      this.maxDistance = 128;
      this.maxIterations = 512;
      this.allowLoops = false;
      this.girth = (new IrisStyledRange()).setMin(3.0D).setMax(5.0D).setStyle(new IrisGeneratorStyle(NoiseStyle.PERLIN));
   }

   @Generated
   public IrisWorm(final IrisShapedGeneratorStyle xStyle, final IrisShapedGeneratorStyle yStyle, final IrisShapedGeneratorStyle zStyle, final int maxDistance, final int maxIterations, final boolean allowLoops, final IrisStyledRange girth) {
      this.xStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, -2, 2);
      this.yStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, -2, 2);
      this.zStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, -2, 2);
      this.maxDistance = 128;
      this.maxIterations = 512;
      this.allowLoops = false;
      this.girth = (new IrisStyledRange()).setMin(3.0D).setMax(5.0D).setStyle(new IrisGeneratorStyle(NoiseStyle.PERLIN));
      this.xStyle = var1;
      this.yStyle = var2;
      this.zStyle = var3;
      this.maxDistance = var4;
      this.maxIterations = var5;
      this.allowLoops = var6;
      this.girth = var7;
   }

   @Generated
   public IrisShapedGeneratorStyle getXStyle() {
      return this.xStyle;
   }

   @Generated
   public IrisShapedGeneratorStyle getYStyle() {
      return this.yStyle;
   }

   @Generated
   public IrisShapedGeneratorStyle getZStyle() {
      return this.zStyle;
   }

   @Generated
   public int getMaxDistance() {
      return this.maxDistance;
   }

   @Generated
   public int getMaxIterations() {
      return this.maxIterations;
   }

   @Generated
   public boolean isAllowLoops() {
      return this.allowLoops;
   }

   @Generated
   public IrisStyledRange getGirth() {
      return this.girth;
   }

   @Generated
   public IrisWorm setXStyle(final IrisShapedGeneratorStyle xStyle) {
      this.xStyle = var1;
      return this;
   }

   @Generated
   public IrisWorm setYStyle(final IrisShapedGeneratorStyle yStyle) {
      this.yStyle = var1;
      return this;
   }

   @Generated
   public IrisWorm setZStyle(final IrisShapedGeneratorStyle zStyle) {
      this.zStyle = var1;
      return this;
   }

   @Generated
   public IrisWorm setMaxDistance(final int maxDistance) {
      this.maxDistance = var1;
      return this;
   }

   @Generated
   public IrisWorm setMaxIterations(final int maxIterations) {
      this.maxIterations = var1;
      return this;
   }

   @Generated
   public IrisWorm setAllowLoops(final boolean allowLoops) {
      this.allowLoops = var1;
      return this;
   }

   @Generated
   public IrisWorm setGirth(final IrisStyledRange girth) {
      this.girth = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisWorm)) {
         return false;
      } else {
         IrisWorm var2 = (IrisWorm)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMaxDistance() != var2.getMaxDistance()) {
            return false;
         } else if (this.getMaxIterations() != var2.getMaxIterations()) {
            return false;
         } else if (this.isAllowLoops() != var2.isAllowLoops()) {
            return false;
         } else {
            IrisShapedGeneratorStyle var3 = this.getXStyle();
            IrisShapedGeneratorStyle var4 = var2.getXStyle();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label59: {
               IrisShapedGeneratorStyle var5 = this.getYStyle();
               IrisShapedGeneratorStyle var6 = var2.getYStyle();
               if (var5 == null) {
                  if (var6 == null) {
                     break label59;
                  }
               } else if (var5.equals(var6)) {
                  break label59;
               }

               return false;
            }

            IrisShapedGeneratorStyle var7 = this.getZStyle();
            IrisShapedGeneratorStyle var8 = var2.getZStyle();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisStyledRange var9 = this.getGirth();
            IrisStyledRange var10 = var2.getGirth();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisWorm;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var7 = var2 * 59 + this.getMaxDistance();
      var7 = var7 * 59 + this.getMaxIterations();
      var7 = var7 * 59 + (this.isAllowLoops() ? 79 : 97);
      IrisShapedGeneratorStyle var3 = this.getXStyle();
      var7 = var7 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisShapedGeneratorStyle var4 = this.getYStyle();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisShapedGeneratorStyle var5 = this.getZStyle();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisStyledRange var6 = this.getGirth();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getXStyle());
      return "IrisWorm(xStyle=" + var10000 + ", yStyle=" + String.valueOf(this.getYStyle()) + ", zStyle=" + String.valueOf(this.getZStyle()) + ", maxDistance=" + this.getMaxDistance() + ", maxIterations=" + this.getMaxIterations() + ", allowLoops=" + this.isAllowLoops() + ", girth=" + String.valueOf(this.getGirth()) + ")";
   }
}
