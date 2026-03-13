package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import lombok.Generated;

@Desc("Translate objects")
public class IrisRavine extends IrisRegistrant {
   @Desc("Define the shape of this ravine (2d, ignores Y)")
   private IrisWorm worm = new IrisWorm();
   @RegistryListResource(IrisBiome.class)
   @Desc("Force this cave to only generate the specified custom biome")
   private String customBiome = "";
   @Desc("Define potential forking features")
   private IrisCarving fork = new IrisCarving();
   @Desc("The style used to determine the curvature of this worm's y")
   private IrisShapedGeneratorStyle depthStyle;
   @Desc("The style used to determine the curvature of this worm's y")
   private IrisShapedGeneratorStyle baseWidthStyle;
   @MinNumber(1.0D)
   @MaxNumber(100.0D)
   @Desc("The angle at which the ravine widens as it gets closer to the surface")
   private double angle;
   @MinNumber(1.0D)
   @MaxNumber(100.0D)
   @Desc("The angle at which the ravine widens as it gets closer to the surface")
   private double topAngle;
   @Desc("To fill this cave with lava, set the lava level to a height from the bottom most point of the cave.")
   private int lavaLevel;
   @Desc("How many worm nodes must be placed to actually generate a ravine? Higher reduces the chances but also reduces ravine 'holes'")
   private int nodeThreshold;
   @MinNumber(1.0D)
   @MaxNumber(8.0D)
   @Desc("The thickness of the ravine ribs")
   private double ribThickness;

   public String getFolderName() {
      return "ravines";
   }

   public String getTypeName() {
      return "Ravine";
   }

   public void generate(MantleWriter writer, RNG rng, Engine engine, int x, int y, int z) {
      this.generate(var1, var2, new RNG(var3.getSeedManager().getCarve()), var3, var4, var5, var6, 0, -1);
   }

   public void generate(MantleWriter writer, RNG rng, RNG base, Engine engine, int x, int y, int z, int recursion, int waterHint) {
      KList var10 = this.getWorm().generate(var3.nextParallelRNG(879615), var4.getData(), var1, (IrisRange)null, var5, var6, var7, true, 0.0D);
      CNG var11 = this.depthStyle.getGenerator().create(var3.nextParallelRNG(7894156), var4.getData());
      CNG var12 = this.baseWidthStyle.getGenerator().create(var3.nextParallelRNG(15315456), var4.getData());
      int var13 = Math.max(var9, -1);
      boolean var14 = false;
      int var19;
      int var20;
      int var21;
      if (var13 == -1) {
         Iterator var15 = var10.iterator();

         while(var15.hasNext()) {
            IrisPosition var16 = (IrisPosition)var15.next();
            int var17 = var6 == -1 ? ((Double)var4.getComplex().getHeightStream().get((double)var5, (double)var7)).intValue() : var6;
            int var18 = (int)Math.round(var11.fitDouble((double)this.depthStyle.getMin(), (double)this.depthStyle.getMax(), (double)var16.getX(), (double)var16.getZ()));
            var19 = (int)Math.round((double)var17 - (double)var18 * 0.45D);
            var20 = var19 + var18;
            var21 = var4.getHeight(var5, var7, true);
            if (var20 > var21 && var21 < var4.getDimension().getFluidHeight()) {
               var13 = Math.max(var13, var20);
               var14 = true;
               break;
            }
         }
      } else {
         var14 = true;
      }

      MatterCavern var26 = new MatterCavern(true, this.customBiome, (byte)(var14 ? 1 : 0));
      MatterCavern var27 = new MatterCavern(true, this.customBiome, (byte)2);
      if (var10.size() >= this.nodeThreshold) {
         Iterator var28 = var10.iterator();

         while(var28.hasNext()) {
            IrisPosition var29 = (IrisPosition)var28.next();
            var19 = var6 == -1 ? ((Double)var4.getComplex().getHeightStream().get((double)var5, (double)var7)).intValue() : var6;
            var20 = (int)Math.round(var11.fitDouble((double)this.depthStyle.getMin(), (double)this.depthStyle.getMax(), (double)var29.getX(), (double)var29.getZ()));
            var21 = (int)Math.round(var12.fitDouble((double)this.baseWidthStyle.getMin(), (double)this.baseWidthStyle.getMax(), (double)var29.getX(), (double)var29.getZ()));
            int var22 = (int)Math.round((double)var19 - (double)var20 * 0.45D);
            this.fork.doCarving(var1, var2, var3, var4, var29.getX(), var2.i(var22 - var20, var22), var29.getZ(), var8, var13);

            int var23;
            double var24;
            for(var23 = var22 + var20; var23 >= var22; --var23) {
               if ((double)var23 % this.ribThickness == 0.0D) {
                  var24 = (double)var21 + (double)(var22 + var20 - var23) * (this.angle / 360.0D);
                  if (var24 <= 0.25D || (double)var23 <= this.ribThickness + 2.0D) {
                     break;
                  }

                  if (this.lavaLevel >= 0 && var23 <= this.lavaLevel + (var22 - this.depthStyle.getMid())) {
                     var1.setElipsoid(var29.getX(), var23, var29.getZ(), var24, this.ribThickness, var24, true, var27);
                  } else {
                     var1.setElipsoid(var29.getX(), var23, var29.getZ(), var24, this.ribThickness, var24, true, var26);
                  }
               }
            }

            for(var23 = var22 - var20; var23 <= var22; ++var23) {
               if ((double)var23 % this.ribThickness == 0.0D) {
                  var24 = (double)var21 - (double)(var22 - var20 - var23) * (this.angle / 360.0D);
                  if (var24 <= 0.25D || (double)var23 <= this.ribThickness + 2.0D) {
                     break;
                  }

                  if (this.lavaLevel >= 0 && var23 <= this.lavaLevel + (var22 - this.depthStyle.getMid())) {
                     var1.setElipsoid(var29.getX(), var23, var29.getZ(), var24, this.ribThickness, var24, true, var27);
                  } else {
                     var1.setElipsoid(var29.getX(), var23, var29.getZ(), var24, this.ribThickness, var24, true, var26);
                  }
               }
            }
         }

      }
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   public int getMaxSize(IrisData data, int depth) {
      return this.getWorm().getMaxDistance() + this.fork.getMaxRange(var1, var2);
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRavine)) {
         return false;
      } else {
         IrisRavine var2 = (IrisRavine)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else if (Double.compare(this.getAngle(), var2.getAngle()) != 0) {
            return false;
         } else if (Double.compare(this.getTopAngle(), var2.getTopAngle()) != 0) {
            return false;
         } else if (this.getLavaLevel() != var2.getLavaLevel()) {
            return false;
         } else if (this.getNodeThreshold() != var2.getNodeThreshold()) {
            return false;
         } else if (Double.compare(this.getRibThickness(), var2.getRibThickness()) != 0) {
            return false;
         } else {
            label86: {
               IrisWorm var3 = this.getWorm();
               IrisWorm var4 = var2.getWorm();
               if (var3 == null) {
                  if (var4 == null) {
                     break label86;
                  }
               } else if (var3.equals(var4)) {
                  break label86;
               }

               return false;
            }

            label79: {
               String var5 = this.getCustomBiome();
               String var6 = var2.getCustomBiome();
               if (var5 == null) {
                  if (var6 == null) {
                     break label79;
                  }
               } else if (var5.equals(var6)) {
                  break label79;
               }

               return false;
            }

            IrisCarving var7 = this.getFork();
            IrisCarving var8 = var2.getFork();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisShapedGeneratorStyle var9 = this.getDepthStyle();
            IrisShapedGeneratorStyle var10 = var2.getDepthStyle();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            IrisShapedGeneratorStyle var11 = this.getBaseWidthStyle();
            IrisShapedGeneratorStyle var12 = var2.getBaseWidthStyle();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisRavine;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      long var3 = Double.doubleToLongBits(this.getAngle());
      var2 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getTopAngle());
      var2 = var2 * 59 + (int)(var5 >>> 32 ^ var5);
      var2 = var2 * 59 + this.getLavaLevel();
      var2 = var2 * 59 + this.getNodeThreshold();
      long var7 = Double.doubleToLongBits(this.getRibThickness());
      var2 = var2 * 59 + (int)(var7 >>> 32 ^ var7);
      IrisWorm var9 = this.getWorm();
      var2 = var2 * 59 + (var9 == null ? 43 : var9.hashCode());
      String var10 = this.getCustomBiome();
      var2 = var2 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisCarving var11 = this.getFork();
      var2 = var2 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisShapedGeneratorStyle var12 = this.getDepthStyle();
      var2 = var2 * 59 + (var12 == null ? 43 : var12.hashCode());
      IrisShapedGeneratorStyle var13 = this.getBaseWidthStyle();
      var2 = var2 * 59 + (var13 == null ? 43 : var13.hashCode());
      return var2;
   }

   @Generated
   public IrisRavine() {
      this.depthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, 5, 18);
      this.baseWidthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, 3, 6);
      this.angle = 18.0D;
      this.topAngle = 38.0D;
      this.lavaLevel = -1;
      this.nodeThreshold = 5;
      this.ribThickness = 3.0D;
   }

   @Generated
   public IrisRavine(final IrisWorm worm, final String customBiome, final IrisCarving fork, final IrisShapedGeneratorStyle depthStyle, final IrisShapedGeneratorStyle baseWidthStyle, final double angle, final double topAngle, final int lavaLevel, final int nodeThreshold, final double ribThickness) {
      this.depthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, 5, 18);
      this.baseWidthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN, 3, 6);
      this.angle = 18.0D;
      this.topAngle = 38.0D;
      this.lavaLevel = -1;
      this.nodeThreshold = 5;
      this.ribThickness = 3.0D;
      this.worm = var1;
      this.customBiome = var2;
      this.fork = var3;
      this.depthStyle = var4;
      this.baseWidthStyle = var5;
      this.angle = var6;
      this.topAngle = var8;
      this.lavaLevel = var10;
      this.nodeThreshold = var11;
      this.ribThickness = var12;
   }

   @Generated
   public IrisWorm getWorm() {
      return this.worm;
   }

   @Generated
   public String getCustomBiome() {
      return this.customBiome;
   }

   @Generated
   public IrisCarving getFork() {
      return this.fork;
   }

   @Generated
   public IrisShapedGeneratorStyle getDepthStyle() {
      return this.depthStyle;
   }

   @Generated
   public IrisShapedGeneratorStyle getBaseWidthStyle() {
      return this.baseWidthStyle;
   }

   @Generated
   public double getAngle() {
      return this.angle;
   }

   @Generated
   public double getTopAngle() {
      return this.topAngle;
   }

   @Generated
   public int getLavaLevel() {
      return this.lavaLevel;
   }

   @Generated
   public int getNodeThreshold() {
      return this.nodeThreshold;
   }

   @Generated
   public double getRibThickness() {
      return this.ribThickness;
   }

   @Generated
   public IrisRavine setWorm(final IrisWorm worm) {
      this.worm = var1;
      return this;
   }

   @Generated
   public IrisRavine setCustomBiome(final String customBiome) {
      this.customBiome = var1;
      return this;
   }

   @Generated
   public IrisRavine setFork(final IrisCarving fork) {
      this.fork = var1;
      return this;
   }

   @Generated
   public IrisRavine setDepthStyle(final IrisShapedGeneratorStyle depthStyle) {
      this.depthStyle = var1;
      return this;
   }

   @Generated
   public IrisRavine setBaseWidthStyle(final IrisShapedGeneratorStyle baseWidthStyle) {
      this.baseWidthStyle = var1;
      return this;
   }

   @Generated
   public IrisRavine setAngle(final double angle) {
      this.angle = var1;
      return this;
   }

   @Generated
   public IrisRavine setTopAngle(final double topAngle) {
      this.topAngle = var1;
      return this;
   }

   @Generated
   public IrisRavine setLavaLevel(final int lavaLevel) {
      this.lavaLevel = var1;
      return this;
   }

   @Generated
   public IrisRavine setNodeThreshold(final int nodeThreshold) {
      this.nodeThreshold = var1;
      return this;
   }

   @Generated
   public IrisRavine setRibThickness(final double ribThickness) {
      this.ribThickness = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getWorm());
      return "IrisRavine(worm=" + var10000 + ", customBiome=" + this.getCustomBiome() + ", fork=" + String.valueOf(this.getFork()) + ", depthStyle=" + String.valueOf(this.getDepthStyle()) + ", baseWidthStyle=" + String.valueOf(this.getBaseWidthStyle()) + ", angle=" + this.getAngle() + ", topAngle=" + this.getTopAngle() + ", lavaLevel=" + this.getLavaLevel() + ", nodeThreshold=" + this.getNodeThreshold() + ", ribThickness=" + this.getRibThickness() + ")";
   }
}
