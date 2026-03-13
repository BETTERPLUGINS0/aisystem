package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import lombok.Generated;

@Snippet("generator")
@Desc("A noise generator")
public class IrisNoiseGenerator {
   private final transient AtomicCache<CNG> generator;
   @MinNumber(1.0E-4D)
   @Desc("The coordinate input zoom")
   private double zoom;
   @Desc("Reverse the output. So that noise = -noise + opacity")
   private boolean negative;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The output multiplier")
   private double opacity;
   @Desc("Coordinate offset x")
   private double offsetX;
   @Desc("Height output offset y. Avoid using with terrain generation.")
   private double offsetY;
   @Desc("Coordinate offset z")
   private double offsetZ;
   @Required
   @Desc("The seed")
   private long seed;
   @Desc("Apply a parametric curve on the output")
   private boolean parametric;
   @Desc("Apply a bezier curve on the output")
   private boolean bezier;
   @Desc("Apply a sin-center curve on the output (0, and 1 = 0 and 0.5 = 1.0 using a sinoid shape.)")
   private boolean sinCentered;
   @Desc("The exponent noise^EXPONENT")
   private double exponent;
   @Desc("Enable / disable. Outputs offsetY if disabled")
   private boolean enabled;
   @Required
   @Desc("The Noise Style")
   private IrisGeneratorStyle style;
   @MinNumber(1.0D)
   @Desc("Multiple octaves for multple generators of changing zooms added together")
   private int octaves;
   @ArrayType(
      min = 1,
      type = IrisNoiseGenerator.class
   )
   @Desc("Apply a child noise generator to fracture the input coordinates of this generator")
   private KList<IrisNoiseGenerator> fracture;

   public IrisNoiseGenerator(boolean enabled) {
      this();
      this.enabled = var1;
   }

   protected CNG getGenerator(long superSeed, IrisData data) {
      return (CNG)this.generator.aquire(() -> {
         return this.style.create(new RNG(var1 + 33955677L - this.seed), var3).oct(this.octaves);
      });
   }

   public double getMax() {
      return this.getOffsetY() + this.opacity;
   }

   public double getNoise(long superSeed, double xv, double zv, IrisData data) {
      if (!this.enabled) {
         return this.offsetY;
      } else {
         double var8 = var3;
         double var10 = var5;
         int var12 = 33;

         for(Iterator var13 = this.fracture.iterator(); var13.hasNext(); var12 += 819) {
            IrisNoiseGenerator var14 = (IrisNoiseGenerator)var13.next();
            if (var14.isEnabled()) {
               var8 += var14.getNoise(var1 + this.seed + (long)var12, var3, var5, var7) - this.opacity / 2.0D;
               var10 -= var14.getNoise(var1 + this.seed + (long)var12, var5, var3, var7) - this.opacity / 2.0D;
            }
         }

         double var15 = this.getGenerator(var1, var7).fitDouble(0.0D, this.opacity, var8 / this.zoom + this.offsetX, var10 / this.zoom + this.offsetZ);
         var15 = this.negative ? -var15 + this.opacity : var15;
         var15 = (this.exponent != 1.0D ? (var15 < 0.0D ? -Math.pow(-var15, this.exponent) : Math.pow(var15, this.exponent)) : var15) + this.offsetY;
         var15 = this.parametric ? IrisInterpolation.parametric(var15, 1.0D) : var15;
         var15 = this.bezier ? IrisInterpolation.bezier(var15) : var15;
         var15 = this.sinCentered ? IrisInterpolation.sinCenter(var15) : var15;
         return var15;
      }
   }

   public KList<IrisNoiseGenerator> getAllComposites() {
      KList var1 = new KList();
      var1.add((Object)this);
      Iterator var2 = this.getFracture().iterator();

      while(var2.hasNext()) {
         IrisNoiseGenerator var3 = (IrisNoiseGenerator)var2.next();
         var1.addAll(var3.getAllComposites());
      }

      return var1;
   }

   @Generated
   public IrisNoiseGenerator() {
      this.generator = new AtomicCache();
      this.zoom = 1.0D;
      this.negative = false;
      this.opacity = 1.0D;
      this.offsetX = 0.0D;
      this.offsetY = 0.0D;
      this.offsetZ = 0.0D;
      this.seed = 0L;
      this.parametric = false;
      this.bezier = false;
      this.sinCentered = false;
      this.exponent = 1.0D;
      this.enabled = true;
      this.style = NoiseStyle.IRIS.style();
      this.octaves = 1;
      this.fracture = new KList();
   }

   @Generated
   public IrisNoiseGenerator(final double zoom, final boolean negative, final double opacity, final double offsetX, final double offsetY, final double offsetZ, final long seed, final boolean parametric, final boolean bezier, final boolean sinCentered, final double exponent, final boolean enabled, final IrisGeneratorStyle style, final int octaves, final KList<IrisNoiseGenerator> fracture) {
      this.generator = new AtomicCache();
      this.zoom = 1.0D;
      this.negative = false;
      this.opacity = 1.0D;
      this.offsetX = 0.0D;
      this.offsetY = 0.0D;
      this.offsetZ = 0.0D;
      this.seed = 0L;
      this.parametric = false;
      this.bezier = false;
      this.sinCentered = false;
      this.exponent = 1.0D;
      this.enabled = true;
      this.style = NoiseStyle.IRIS.style();
      this.octaves = 1;
      this.fracture = new KList();
      this.zoom = var1;
      this.negative = var3;
      this.opacity = var4;
      this.offsetX = var6;
      this.offsetY = var8;
      this.offsetZ = var10;
      this.seed = var12;
      this.parametric = var14;
      this.bezier = var15;
      this.sinCentered = var16;
      this.exponent = var17;
      this.enabled = var19;
      this.style = var20;
      this.octaves = var21;
      this.fracture = var22;
   }

   @Generated
   public AtomicCache<CNG> getGenerator() {
      return this.generator;
   }

   @Generated
   public double getZoom() {
      return this.zoom;
   }

   @Generated
   public boolean isNegative() {
      return this.negative;
   }

   @Generated
   public double getOpacity() {
      return this.opacity;
   }

   @Generated
   public double getOffsetX() {
      return this.offsetX;
   }

   @Generated
   public double getOffsetY() {
      return this.offsetY;
   }

   @Generated
   public double getOffsetZ() {
      return this.offsetZ;
   }

   @Generated
   public long getSeed() {
      return this.seed;
   }

   @Generated
   public boolean isParametric() {
      return this.parametric;
   }

   @Generated
   public boolean isBezier() {
      return this.bezier;
   }

   @Generated
   public boolean isSinCentered() {
      return this.sinCentered;
   }

   @Generated
   public double getExponent() {
      return this.exponent;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public IrisGeneratorStyle getStyle() {
      return this.style;
   }

   @Generated
   public int getOctaves() {
      return this.octaves;
   }

   @Generated
   public KList<IrisNoiseGenerator> getFracture() {
      return this.fracture;
   }

   @Generated
   public IrisNoiseGenerator setZoom(final double zoom) {
      this.zoom = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setNegative(final boolean negative) {
      this.negative = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setOpacity(final double opacity) {
      this.opacity = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setOffsetX(final double offsetX) {
      this.offsetX = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setOffsetY(final double offsetY) {
      this.offsetY = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setOffsetZ(final double offsetZ) {
      this.offsetZ = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setSeed(final long seed) {
      this.seed = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setParametric(final boolean parametric) {
      this.parametric = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setBezier(final boolean bezier) {
      this.bezier = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setSinCentered(final boolean sinCentered) {
      this.sinCentered = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setExponent(final double exponent) {
      this.exponent = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setEnabled(final boolean enabled) {
      this.enabled = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setStyle(final IrisGeneratorStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setOctaves(final int octaves) {
      this.octaves = var1;
      return this;
   }

   @Generated
   public IrisNoiseGenerator setFracture(final KList<IrisNoiseGenerator> fracture) {
      this.fracture = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisNoiseGenerator)) {
         return false;
      } else {
         IrisNoiseGenerator var2 = (IrisNoiseGenerator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getZoom(), var2.getZoom()) != 0) {
            return false;
         } else if (this.isNegative() != var2.isNegative()) {
            return false;
         } else if (Double.compare(this.getOpacity(), var2.getOpacity()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetX(), var2.getOffsetX()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetY(), var2.getOffsetY()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetZ(), var2.getOffsetZ()) != 0) {
            return false;
         } else if (this.getSeed() != var2.getSeed()) {
            return false;
         } else if (this.isParametric() != var2.isParametric()) {
            return false;
         } else if (this.isBezier() != var2.isBezier()) {
            return false;
         } else if (this.isSinCentered() != var2.isSinCentered()) {
            return false;
         } else if (Double.compare(this.getExponent(), var2.getExponent()) != 0) {
            return false;
         } else if (this.isEnabled() != var2.isEnabled()) {
            return false;
         } else if (this.getOctaves() != var2.getOctaves()) {
            return false;
         } else {
            IrisGeneratorStyle var3 = this.getStyle();
            IrisGeneratorStyle var4 = var2.getStyle();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getFracture();
            KList var6 = var2.getFracture();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisNoiseGenerator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getZoom());
      int var19 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var19 = var19 * 59 + (this.isNegative() ? 79 : 97);
      long var5 = Double.doubleToLongBits(this.getOpacity());
      var19 = var19 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getOffsetX());
      var19 = var19 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getOffsetY());
      var19 = var19 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getOffsetZ());
      var19 = var19 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = this.getSeed();
      var19 = var19 * 59 + (int)(var13 >>> 32 ^ var13);
      var19 = var19 * 59 + (this.isParametric() ? 79 : 97);
      var19 = var19 * 59 + (this.isBezier() ? 79 : 97);
      var19 = var19 * 59 + (this.isSinCentered() ? 79 : 97);
      long var15 = Double.doubleToLongBits(this.getExponent());
      var19 = var19 * 59 + (int)(var15 >>> 32 ^ var15);
      var19 = var19 * 59 + (this.isEnabled() ? 79 : 97);
      var19 = var19 * 59 + this.getOctaves();
      IrisGeneratorStyle var17 = this.getStyle();
      var19 = var19 * 59 + (var17 == null ? 43 : var17.hashCode());
      KList var18 = this.getFracture();
      var19 = var19 * 59 + (var18 == null ? 43 : var18.hashCode());
      return var19;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getGenerator());
      return "IrisNoiseGenerator(generator=" + var10000 + ", zoom=" + this.getZoom() + ", negative=" + this.isNegative() + ", opacity=" + this.getOpacity() + ", offsetX=" + this.getOffsetX() + ", offsetY=" + this.getOffsetY() + ", offsetZ=" + this.getOffsetZ() + ", seed=" + this.getSeed() + ", parametric=" + this.isParametric() + ", bezier=" + this.isBezier() + ", sinCentered=" + this.isSinCentered() + ", exponent=" + this.getExponent() + ", enabled=" + this.isEnabled() + ", style=" + String.valueOf(this.getStyle()) + ", octaves=" + this.getOctaves() + ", fracture=" + String.valueOf(this.getFracture()) + ")";
   }
}
