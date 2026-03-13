package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Desc("Ore Layer")
public class IrisOreGenerator {
   @Desc("The palette of 'ore' generated")
   private IrisMaterialPalette palette = (new IrisMaterialPalette()).qclear();
   @Desc("The generator style for the 'ore'")
   private IrisGeneratorStyle chanceStyle;
   @Desc("Will ores generate on the surface of the terrain layer")
   private boolean generateSurface;
   @Desc("Threshold for rate of generation")
   private double threshold;
   @Desc("Height limit (min, max)")
   private IrisRange range;
   private transient AtomicCache<CNG> chanceCache;

   public BlockData generate(int x, int y, int z, RNG rng, IrisData data) {
      if (this.palette.getPalette().isEmpty()) {
         return null;
      } else if (!this.range.contains(var2)) {
         return null;
      } else {
         CNG var6 = (CNG)this.chanceCache.aquire(() -> {
            return this.chanceStyle.create(var4, var5);
         });
         return var6.noise((double)var1, (double)var2, (double)var3) > this.threshold ? null : this.palette.get(var4, (double)var1, (double)var2, (double)var3, var5);
      }
   }

   @Generated
   public IrisOreGenerator() {
      this.chanceStyle = new IrisGeneratorStyle(NoiseStyle.STATIC);
      this.generateSurface = false;
      this.threshold = 0.5D;
      this.range = new IrisRange(30.0D, 80.0D);
      this.chanceCache = new AtomicCache();
   }

   @Generated
   public IrisOreGenerator(final IrisMaterialPalette palette, final IrisGeneratorStyle chanceStyle, final boolean generateSurface, final double threshold, final IrisRange range, final AtomicCache<CNG> chanceCache) {
      this.chanceStyle = new IrisGeneratorStyle(NoiseStyle.STATIC);
      this.generateSurface = false;
      this.threshold = 0.5D;
      this.range = new IrisRange(30.0D, 80.0D);
      this.chanceCache = new AtomicCache();
      this.palette = var1;
      this.chanceStyle = var2;
      this.generateSurface = var3;
      this.threshold = var4;
      this.range = var6;
      this.chanceCache = var7;
   }

   @Generated
   public IrisMaterialPalette getPalette() {
      return this.palette;
   }

   @Generated
   public IrisGeneratorStyle getChanceStyle() {
      return this.chanceStyle;
   }

   @Generated
   public boolean isGenerateSurface() {
      return this.generateSurface;
   }

   @Generated
   public double getThreshold() {
      return this.threshold;
   }

   @Generated
   public IrisRange getRange() {
      return this.range;
   }

   @Generated
   public AtomicCache<CNG> getChanceCache() {
      return this.chanceCache;
   }

   @Generated
   public IrisOreGenerator setPalette(final IrisMaterialPalette palette) {
      this.palette = var1;
      return this;
   }

   @Generated
   public IrisOreGenerator setChanceStyle(final IrisGeneratorStyle chanceStyle) {
      this.chanceStyle = var1;
      return this;
   }

   @Generated
   public IrisOreGenerator setGenerateSurface(final boolean generateSurface) {
      this.generateSurface = var1;
      return this;
   }

   @Generated
   public IrisOreGenerator setThreshold(final double threshold) {
      this.threshold = var1;
      return this;
   }

   @Generated
   public IrisOreGenerator setRange(final IrisRange range) {
      this.range = var1;
      return this;
   }

   @Generated
   public IrisOreGenerator setChanceCache(final AtomicCache<CNG> chanceCache) {
      this.chanceCache = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisOreGenerator)) {
         return false;
      } else {
         IrisOreGenerator var2 = (IrisOreGenerator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isGenerateSurface() != var2.isGenerateSurface()) {
            return false;
         } else if (Double.compare(this.getThreshold(), var2.getThreshold()) != 0) {
            return false;
         } else {
            label52: {
               IrisMaterialPalette var3 = this.getPalette();
               IrisMaterialPalette var4 = var2.getPalette();
               if (var3 == null) {
                  if (var4 == null) {
                     break label52;
                  }
               } else if (var3.equals(var4)) {
                  break label52;
               }

               return false;
            }

            IrisGeneratorStyle var5 = this.getChanceStyle();
            IrisGeneratorStyle var6 = var2.getChanceStyle();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisRange var7 = this.getRange();
            IrisRange var8 = var2.getRange();
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
      return var1 instanceof IrisOreGenerator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + (this.isGenerateSurface() ? 79 : 97);
      long var3 = Double.doubleToLongBits(this.getThreshold());
      var8 = var8 * 59 + (int)(var3 >>> 32 ^ var3);
      IrisMaterialPalette var5 = this.getPalette();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisGeneratorStyle var6 = this.getChanceStyle();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisRange var7 = this.getRange();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPalette());
      return "IrisOreGenerator(palette=" + var10000 + ", chanceStyle=" + String.valueOf(this.getChanceStyle()) + ", generateSurface=" + this.isGenerateSurface() + ", threshold=" + this.getThreshold() + ", range=" + String.valueOf(this.getRange()) + ", chanceCache=" + String.valueOf(this.getChanceCache()) + ")";
   }
}
