package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.noise.ExpressionNoise;
import com.volmit.iris.util.noise.ImageNoise;
import com.volmit.iris.util.noise.NoiseGenerator;
import java.util.Objects;
import lombok.Generated;

@Snippet("style")
@Desc("A gen style")
public class IrisGeneratorStyle {
   private final transient AtomicCache<CNG> cng = new AtomicCache();
   @Desc("The chance is 1 in CHANCE per interval")
   private NoiseStyle style;
   @Desc("If set above 0, this style will be cellularized")
   private double cellularFrequency;
   @Desc("Cell zooms")
   private double cellularZoom;
   @MinNumber(1.0E-5D)
   @Desc("The zoom of this style")
   private double zoom;
   @Desc("Instead of using the style property, use a custom expression to represent this style.")
   @RegistryListResource(IrisExpression.class)
   private String expression;
   @Desc("Use an Image map instead of a generated value")
   private IrisImageMap imageMap;
   @Desc("Instead of using the style property, use a custom noise generator to represent this style.\nFile extension: .noise.kts")
   @RegistryListResource(IrisScript.class)
   private String script;
   @MinNumber(1.0E-5D)
   @Desc("The Output multiplier. Only used if parent is fracture.")
   private double multiplier;
   @Desc("If set to true, each dimension will be fractured with a different order of input coordinates. This is usually 2 or 3 times slower than normal.")
   private boolean axialFracturing;
   @Desc("Apply a generator to the coordinate field fed into this parent generator. I.e. Distort your generator with another generator.")
   private IrisGeneratorStyle fracture;
   @MinNumber(0.01562D)
   @MaxNumber(64.0D)
   @Desc("The exponent")
   private double exponent;
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("If the cache size is set above 0, this generator will be cached")
   private int cacheSize;

   public IrisGeneratorStyle(NoiseStyle s) {
      this.style = NoiseStyle.FLAT;
      this.cellularFrequency = 0.0D;
      this.cellularZoom = 1.0D;
      this.zoom = 1.0D;
      this.expression = null;
      this.imageMap = null;
      this.script = null;
      this.multiplier = 1.0D;
      this.axialFracturing = false;
      this.fracture = null;
      this.exponent = 1.0D;
      this.cacheSize = 0;
      this.style = var1;
   }

   public IrisGeneratorStyle zoomed(double z) {
      this.zoom = var1;
      return this;
   }

   public CNG createNoCache(RNG rng, IrisData data) {
      return this.createNoCache(var1, var2, false);
   }

   private int hash() {
      return Objects.hash(new Object[]{this.expression, this.imageMap, this.multiplier, this.axialFracturing, this.fracture != null ? this.fracture.hash() : 0, this.exponent, this.cacheSize, this.zoom, this.cellularZoom, this.cellularFrequency, this.style});
   }

   public CNG createNoCache(RNG rng, IrisData data, boolean actuallyCached) {
      String var4 = this.hash().makeConcatWithConstants<invokedynamic>(this.hash());
      CNG var5 = null;
      if (this.getExpression() != null) {
         IrisExpression var6 = (IrisExpression)var2.getExpressionLoader().load(this.getExpression());
         if (var6 != null) {
            var5 = (new CNG(var1, new ExpressionNoise(var1, var6), 1.0D, 1)).bake();
         }
      } else if (this.getImageMap() != null) {
         var5 = (new CNG(var1, new ImageNoise(var2, this.getImageMap()), 1.0D, 1)).bake();
      } else if (this.getScript() != null) {
         Object var8 = var2.getEnvironment().createNoise(this.getScript(), var1);
         if (var8 == null) {
            Iris.warn("Failed to create noise from script: " + this.getScript());
         }

         if (var8 instanceof NoiseGenerator) {
            NoiseGenerator var7 = (NoiseGenerator)var8;
            var5 = (new CNG(var1, var7, 1.0D, 1)).bake();
         }
      }

      if (var5 == null) {
         var5 = this.style.create(var1).bake();
      }

      var5 = var5.scale(1.0D / this.zoom).pow(this.exponent).bake();
      var5.setTrueFracturing(this.axialFracturing);
      if (this.fracture != null) {
         var5.fractureWith(this.fracture.create(var1.nextParallelRNG(2934), var2), this.fracture.getMultiplier());
      }

      return this.cellularFrequency > 0.0D ? var5.cellularize(var1.nextParallelRNG(884466), this.cellularFrequency).scale(1.0D / this.cellularZoom).bake() : var5;
   }

   public double warp(RNG rng, IrisData data, double value, double... coords) {
      return this.create(var1, var2).noise(var5) + var3;
   }

   public CNG create(RNG rng, IrisData data) {
      return (CNG)this.cng.aquire(() -> {
         return this.createNoCache(var1, var2, true);
      });
   }

   public boolean isFlat() {
      return this.style.equals(NoiseStyle.FLAT);
   }

   public double getMaxFractureDistance() {
      return this.multiplier;
   }

   @Generated
   public IrisGeneratorStyle() {
      this.style = NoiseStyle.FLAT;
      this.cellularFrequency = 0.0D;
      this.cellularZoom = 1.0D;
      this.zoom = 1.0D;
      this.expression = null;
      this.imageMap = null;
      this.script = null;
      this.multiplier = 1.0D;
      this.axialFracturing = false;
      this.fracture = null;
      this.exponent = 1.0D;
      this.cacheSize = 0;
   }

   @Generated
   public IrisGeneratorStyle(final NoiseStyle style, final double cellularFrequency, final double cellularZoom, final double zoom, final String expression, final IrisImageMap imageMap, final String script, final double multiplier, final boolean axialFracturing, final IrisGeneratorStyle fracture, final double exponent, final int cacheSize) {
      this.style = NoiseStyle.FLAT;
      this.cellularFrequency = 0.0D;
      this.cellularZoom = 1.0D;
      this.zoom = 1.0D;
      this.expression = null;
      this.imageMap = null;
      this.script = null;
      this.multiplier = 1.0D;
      this.axialFracturing = false;
      this.fracture = null;
      this.exponent = 1.0D;
      this.cacheSize = 0;
      this.style = var1;
      this.cellularFrequency = var2;
      this.cellularZoom = var4;
      this.zoom = var6;
      this.expression = var8;
      this.imageMap = var9;
      this.script = var10;
      this.multiplier = var11;
      this.axialFracturing = var13;
      this.fracture = var14;
      this.exponent = var15;
      this.cacheSize = var17;
   }

   @Generated
   public AtomicCache<CNG> getCng() {
      return this.cng;
   }

   @Generated
   public NoiseStyle getStyle() {
      return this.style;
   }

   @Generated
   public double getCellularFrequency() {
      return this.cellularFrequency;
   }

   @Generated
   public double getCellularZoom() {
      return this.cellularZoom;
   }

   @Generated
   public double getZoom() {
      return this.zoom;
   }

   @Generated
   public String getExpression() {
      return this.expression;
   }

   @Generated
   public IrisImageMap getImageMap() {
      return this.imageMap;
   }

   @Generated
   public String getScript() {
      return this.script;
   }

   @Generated
   public double getMultiplier() {
      return this.multiplier;
   }

   @Generated
   public boolean isAxialFracturing() {
      return this.axialFracturing;
   }

   @Generated
   public IrisGeneratorStyle getFracture() {
      return this.fracture;
   }

   @Generated
   public double getExponent() {
      return this.exponent;
   }

   @Generated
   public int getCacheSize() {
      return this.cacheSize;
   }

   @Generated
   public IrisGeneratorStyle setStyle(final NoiseStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setCellularFrequency(final double cellularFrequency) {
      this.cellularFrequency = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setCellularZoom(final double cellularZoom) {
      this.cellularZoom = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setZoom(final double zoom) {
      this.zoom = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setExpression(final String expression) {
      this.expression = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setImageMap(final IrisImageMap imageMap) {
      this.imageMap = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setScript(final String script) {
      this.script = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setMultiplier(final double multiplier) {
      this.multiplier = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setAxialFracturing(final boolean axialFracturing) {
      this.axialFracturing = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setFracture(final IrisGeneratorStyle fracture) {
      this.fracture = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setExponent(final double exponent) {
      this.exponent = var1;
      return this;
   }

   @Generated
   public IrisGeneratorStyle setCacheSize(final int cacheSize) {
      this.cacheSize = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisGeneratorStyle)) {
         return false;
      } else {
         IrisGeneratorStyle var2 = (IrisGeneratorStyle)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getCellularFrequency(), var2.getCellularFrequency()) != 0) {
            return false;
         } else if (Double.compare(this.getCellularZoom(), var2.getCellularZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getZoom(), var2.getZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getMultiplier(), var2.getMultiplier()) != 0) {
            return false;
         } else if (this.isAxialFracturing() != var2.isAxialFracturing()) {
            return false;
         } else if (Double.compare(this.getExponent(), var2.getExponent()) != 0) {
            return false;
         } else if (this.getCacheSize() != var2.getCacheSize()) {
            return false;
         } else {
            label88: {
               NoiseStyle var3 = this.getStyle();
               NoiseStyle var4 = var2.getStyle();
               if (var3 == null) {
                  if (var4 == null) {
                     break label88;
                  }
               } else if (var3.equals(var4)) {
                  break label88;
               }

               return false;
            }

            label81: {
               String var5 = this.getExpression();
               String var6 = var2.getExpression();
               if (var5 == null) {
                  if (var6 == null) {
                     break label81;
                  }
               } else if (var5.equals(var6)) {
                  break label81;
               }

               return false;
            }

            IrisImageMap var7 = this.getImageMap();
            IrisImageMap var8 = var2.getImageMap();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            String var9 = this.getScript();
            String var10 = var2.getScript();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            IrisGeneratorStyle var11 = this.getFracture();
            IrisGeneratorStyle var12 = var2.getFracture();
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
      return var1 instanceof IrisGeneratorStyle;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getCellularFrequency());
      int var18 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getCellularZoom());
      var18 = var18 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getZoom());
      var18 = var18 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getMultiplier());
      var18 = var18 * 59 + (int)(var9 >>> 32 ^ var9);
      var18 = var18 * 59 + (this.isAxialFracturing() ? 79 : 97);
      long var11 = Double.doubleToLongBits(this.getExponent());
      var18 = var18 * 59 + (int)(var11 >>> 32 ^ var11);
      var18 = var18 * 59 + this.getCacheSize();
      NoiseStyle var13 = this.getStyle();
      var18 = var18 * 59 + (var13 == null ? 43 : var13.hashCode());
      String var14 = this.getExpression();
      var18 = var18 * 59 + (var14 == null ? 43 : var14.hashCode());
      IrisImageMap var15 = this.getImageMap();
      var18 = var18 * 59 + (var15 == null ? 43 : var15.hashCode());
      String var16 = this.getScript();
      var18 = var18 * 59 + (var16 == null ? 43 : var16.hashCode());
      IrisGeneratorStyle var17 = this.getFracture();
      var18 = var18 * 59 + (var17 == null ? 43 : var17.hashCode());
      return var18;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCng());
      return "IrisGeneratorStyle(cng=" + var10000 + ", style=" + String.valueOf(this.getStyle()) + ", cellularFrequency=" + this.getCellularFrequency() + ", cellularZoom=" + this.getCellularZoom() + ", zoom=" + this.getZoom() + ", expression=" + this.getExpression() + ", imageMap=" + String.valueOf(this.getImageMap()) + ", script=" + this.getScript() + ", multiplier=" + this.getMultiplier() + ", axialFracturing=" + this.isAxialFracturing() + ", fracture=" + String.valueOf(this.getFracture()) + ", exponent=" + this.getExponent() + ", cacheSize=" + this.getCacheSize() + ")";
   }
}
