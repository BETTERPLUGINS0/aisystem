package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;

@Snippet("river")
@Desc("Represents an Iris river")
public class IrisRiver implements IRare {
   @Required
   @Desc("Typically a 1 in RARITY on a per chunk/fork basis")
   @MinNumber(1.0D)
   private int rarity = 15;
   @Desc("The width style of this river")
   private IrisStyledRange width;
   @Desc("Define the shape of this river")
   private IrisWorm worm;
   @RegistryListResource(IrisBiome.class)
   @Desc("Force this river to only generate the specified custom biome")
   private String customBiome;
   @Desc("The width style of this lake")
   private IrisShapedGeneratorStyle widthStyle;
   @Desc("The depth style of this lake")
   private IrisShapedGeneratorStyle depthStyle;

   public int getSize(IrisData data) {
      return this.worm.getMaxDistance();
   }

   public void generate(MantleWriter writer, RNG rng, Engine engine, int x, int y, int z) {
   }

   @Generated
   public IrisRiver() {
      this.width = new IrisStyledRange(3.0D, 6.0D, NoiseStyle.PERLIN.style());
      this.worm = new IrisWorm();
      this.customBiome = "";
      this.widthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 5, 9);
      this.depthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 4, 7);
   }

   @Generated
   public IrisRiver(final int rarity, final IrisStyledRange width, final IrisWorm worm, final String customBiome, final IrisShapedGeneratorStyle widthStyle, final IrisShapedGeneratorStyle depthStyle) {
      this.width = new IrisStyledRange(3.0D, 6.0D, NoiseStyle.PERLIN.style());
      this.worm = new IrisWorm();
      this.customBiome = "";
      this.widthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 5, 9);
      this.depthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 4, 7);
      this.rarity = var1;
      this.width = var2;
      this.worm = var3;
      this.customBiome = var4;
      this.widthStyle = var5;
      this.depthStyle = var6;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public IrisStyledRange getWidth() {
      return this.width;
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
   public IrisShapedGeneratorStyle getWidthStyle() {
      return this.widthStyle;
   }

   @Generated
   public IrisShapedGeneratorStyle getDepthStyle() {
      return this.depthStyle;
   }

   @Generated
   public IrisRiver setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisRiver setWidth(final IrisStyledRange width) {
      this.width = var1;
      return this;
   }

   @Generated
   public IrisRiver setWorm(final IrisWorm worm) {
      this.worm = var1;
      return this;
   }

   @Generated
   public IrisRiver setCustomBiome(final String customBiome) {
      this.customBiome = var1;
      return this;
   }

   @Generated
   public IrisRiver setWidthStyle(final IrisShapedGeneratorStyle widthStyle) {
      this.widthStyle = var1;
      return this;
   }

   @Generated
   public IrisRiver setDepthStyle(final IrisShapedGeneratorStyle depthStyle) {
      this.depthStyle = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRiver)) {
         return false;
      } else {
         IrisRiver var2 = (IrisRiver)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else {
            label73: {
               IrisStyledRange var3 = this.getWidth();
               IrisStyledRange var4 = var2.getWidth();
               if (var3 == null) {
                  if (var4 == null) {
                     break label73;
                  }
               } else if (var3.equals(var4)) {
                  break label73;
               }

               return false;
            }

            IrisWorm var5 = this.getWorm();
            IrisWorm var6 = var2.getWorm();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label59: {
               String var7 = this.getCustomBiome();
               String var8 = var2.getCustomBiome();
               if (var7 == null) {
                  if (var8 == null) {
                     break label59;
                  }
               } else if (var7.equals(var8)) {
                  break label59;
               }

               return false;
            }

            IrisShapedGeneratorStyle var9 = this.getWidthStyle();
            IrisShapedGeneratorStyle var10 = var2.getWidthStyle();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            IrisShapedGeneratorStyle var11 = this.getDepthStyle();
            IrisShapedGeneratorStyle var12 = var2.getDepthStyle();
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
      return var1 instanceof IrisRiver;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + this.getRarity();
      IrisStyledRange var3 = this.getWidth();
      var8 = var8 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisWorm var4 = this.getWorm();
      var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getCustomBiome();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisShapedGeneratorStyle var6 = this.getWidthStyle();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisShapedGeneratorStyle var7 = this.getDepthStyle();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      int var10000 = this.getRarity();
      return "IrisRiver(rarity=" + var10000 + ", width=" + String.valueOf(this.getWidth()) + ", worm=" + String.valueOf(this.getWorm()) + ", customBiome=" + this.getCustomBiome() + ", widthStyle=" + String.valueOf(this.getWidthStyle()) + ", depthStyle=" + String.valueOf(this.getDepthStyle()) + ")";
   }
}
