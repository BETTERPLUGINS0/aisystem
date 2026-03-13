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

@Snippet("lake")
@Desc("Represents an Iris Lake")
public class IrisLake implements IRare {
   @Required
   @Desc("Typically a 1 in RARITY on a per chunk/fork basis")
   @MinNumber(1.0D)
   private int rarity = 15;
   @Desc("The width style of this lake")
   private IrisShapedGeneratorStyle widthStyle;
   @Desc("The depth style of this lake")
   private IrisShapedGeneratorStyle depthStyle;
   @Desc("Define the shape of this lake")
   private IrisWorm worm;
   @RegistryListResource(IrisBiome.class)
   @Desc("Force this lake to only generate the specified custom biome")
   private String customBiome;

   public int getSize(IrisData data) {
      return this.worm.getMaxDistance();
   }

   public void generate(MantleWriter writer, RNG rng, Engine engine, int x, int y, int z) {
   }

   @Generated
   public IrisLake() {
      this.widthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 5, 9);
      this.depthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 4, 7);
      this.worm = new IrisWorm();
      this.customBiome = "";
   }

   @Generated
   public IrisLake(final int rarity, final IrisShapedGeneratorStyle widthStyle, final IrisShapedGeneratorStyle depthStyle, final IrisWorm worm, final String customBiome) {
      this.widthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 5, 9);
      this.depthStyle = new IrisShapedGeneratorStyle(NoiseStyle.PERLIN.style(), 4, 7);
      this.worm = new IrisWorm();
      this.customBiome = "";
      this.rarity = var1;
      this.widthStyle = var2;
      this.depthStyle = var3;
      this.worm = var4;
      this.customBiome = var5;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
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
   public IrisWorm getWorm() {
      return this.worm;
   }

   @Generated
   public String getCustomBiome() {
      return this.customBiome;
   }

   @Generated
   public IrisLake setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisLake setWidthStyle(final IrisShapedGeneratorStyle widthStyle) {
      this.widthStyle = var1;
      return this;
   }

   @Generated
   public IrisLake setDepthStyle(final IrisShapedGeneratorStyle depthStyle) {
      this.depthStyle = var1;
      return this;
   }

   @Generated
   public IrisLake setWorm(final IrisWorm worm) {
      this.worm = var1;
      return this;
   }

   @Generated
   public IrisLake setCustomBiome(final String customBiome) {
      this.customBiome = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisLake)) {
         return false;
      } else {
         IrisLake var2 = (IrisLake)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else {
            label61: {
               IrisShapedGeneratorStyle var3 = this.getWidthStyle();
               IrisShapedGeneratorStyle var4 = var2.getWidthStyle();
               if (var3 == null) {
                  if (var4 == null) {
                     break label61;
                  }
               } else if (var3.equals(var4)) {
                  break label61;
               }

               return false;
            }

            label54: {
               IrisShapedGeneratorStyle var5 = this.getDepthStyle();
               IrisShapedGeneratorStyle var6 = var2.getDepthStyle();
               if (var5 == null) {
                  if (var6 == null) {
                     break label54;
                  }
               } else if (var5.equals(var6)) {
                  break label54;
               }

               return false;
            }

            IrisWorm var7 = this.getWorm();
            IrisWorm var8 = var2.getWorm();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            String var9 = this.getCustomBiome();
            String var10 = var2.getCustomBiome();
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
      return var1 instanceof IrisLake;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var7 = var2 * 59 + this.getRarity();
      IrisShapedGeneratorStyle var3 = this.getWidthStyle();
      var7 = var7 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisShapedGeneratorStyle var4 = this.getDepthStyle();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisWorm var5 = this.getWorm();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getCustomBiome();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      int var10000 = this.getRarity();
      return "IrisLake(rarity=" + var10000 + ", widthStyle=" + String.valueOf(this.getWidthStyle()) + ", depthStyle=" + String.valueOf(this.getDepthStyle()) + ", worm=" + String.valueOf(this.getWorm()) + ", customBiome=" + this.getCustomBiome() + ")";
   }
}
