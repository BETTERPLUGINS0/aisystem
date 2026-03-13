package com.volmit.iris.core.gui.components;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBiomeGeneratorLink;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class IrisRenderer {
   private final Engine renderer;

   public IrisRenderer(Engine renderer) {
      this.renderer = var1;
   }

   public BufferedImage render(double sx, double sz, double size, int resolution, RenderType currentType) {
      BufferedImage var9 = new BufferedImage(var7, var7, 1);
      BiFunction var10 = (var0, var1x) -> {
         return Color.black.getRGB();
      };
      switch(var8) {
      case BIOME:
      case DECORATOR_LOAD:
      case OBJECT_LOAD:
      case LAYER_LOAD:
         var10 = (var2, var3x) -> {
            return ((IrisBiome)this.renderer.getComplex().getTrueBiomeStream().get(var2, var3x)).getColor(this.renderer, var8).getRGB();
         };
         break;
      case BIOME_LAND:
         var10 = (var2, var3x) -> {
            return ((IrisBiome)this.renderer.getComplex().getLandBiomeStream().get(var2, var3x)).getColor(this.renderer, var8).getRGB();
         };
         break;
      case BIOME_SEA:
         var10 = (var2, var3x) -> {
            return ((IrisBiome)this.renderer.getComplex().getSeaBiomeStream().get(var2, var3x)).getColor(this.renderer, var8).getRGB();
         };
         break;
      case REGION:
         var10 = (var2, var3x) -> {
            return ((IrisRegion)this.renderer.getComplex().getRegionStream().get(var2, var3x)).getColor(this.renderer.getComplex(), var8).getRGB();
         };
         break;
      case CAVE_LAND:
         var10 = (var2, var3x) -> {
            return ((IrisBiome)this.renderer.getComplex().getCaveBiomeStream().get(var2, var3x)).getColor(this.renderer, var8).getRGB();
         };
         break;
      case HEIGHT:
         var10 = (var1x, var2) -> {
            return Color.getHSBColor(((Double)this.renderer.getComplex().getHeightStream().get(var1x, var2)).floatValue(), 100.0F, 100.0F).getRGB();
         };
         break;
      case CONTINENT:
         var10 = (var1x, var2) -> {
            IrisBiome var3 = this.renderer.getBiome((int)Math.round(var1x), this.renderer.getMaxHeight() - 1, (int)Math.round(var2));
            IrisBiomeGeneratorLink var4 = (IrisBiomeGeneratorLink)var3.getGenerators().get(0);
            Color var5;
            if (var4.getMax() <= 0) {
               var5 = Color.BLUE;
            } else if (var4.getMin() < 0) {
               var5 = Color.YELLOW;
            } else {
               var5 = Color.GREEN;
            }

            return var5.getRGB();
         };
      }

      for(int var15 = 0; var15 < var7; ++var15) {
         double var11 = IrisInterpolation.lerp(var1, var1 + var5, (double)var15 / (double)var7);

         for(int var16 = 0; var16 < var7; ++var16) {
            double var13 = IrisInterpolation.lerp(var3, var3 + var5, (double)var16 / (double)var7);
            var9.setRGB(var15, var16, (Integer)var10.apply(var11, var13));
         }
      }

      return var9;
   }
}
