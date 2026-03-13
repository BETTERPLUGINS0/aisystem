package com.volmit.iris.util.inventorygui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomColor {
   public static int hueOffset = 0;
   private final Random random;
   private final HashMap<String, RandomColor.ColorInfo> colors = new HashMap();

   public RandomColor() {
      this.loadColorBounds();
      this.random = new Random();
   }

   public RandomColor(long seed) {
      this.loadColorBounds();
      this.random = new Random();
      this.random.setSeed(var1);
   }

   public RandomColor(Random random) {
      this.loadColorBounds();
      this.random = var1;
   }

   private int getColor(int hue, int saturation, int brightness) {
      return java.awt.Color.getHSBColor((float)(var1 + hueOffset % 360) / 360.0F, (float)var2 / 100.0F, (float)var3 / 100.0F).getRGB();
   }

   public int randomColor() {
      return this.randomColor(0, (RandomColor.SaturationType)null, (RandomColor.Luminosity)null);
   }

   public int randomColor(int value, RandomColor.SaturationType saturationType, RandomColor.Luminosity luminosity) {
      int var4 = this.pickHue(var1);
      int var5 = this.pickSaturation(var4, var2, var3);
      int var6 = this.pickBrightness(var4, var5, var3);
      return this.getColor(var4, var5, var6);
   }

   public int randomColor(RandomColor.Color color, RandomColor.SaturationType saturationType, RandomColor.Luminosity luminosity) {
      int var4 = this.pickHue(var1.name());
      int var5 = this.pickSaturation(var4, var2, var3);
      int var6 = this.pickBrightness(var4, var5, var3);
      return this.getColor(var4, var5, var6);
   }

   public int[] randomColor(int count) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("count must be greater than 0");
      } else {
         int[] var2 = new int[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = this.randomColor();
         }

         return var2;
      }
   }

   public int randomColor(RandomColor.Color color) {
      int var2 = this.pickHue(var1.name());
      int var3 = this.pickSaturation((RandomColor.Color)var1, (RandomColor.SaturationType)null, (RandomColor.Luminosity)null);
      int var4 = this.pickBrightness((RandomColor.Color)var1, var3, (RandomColor.Luminosity)null);
      int var5 = this.getColor(var2, var3, var4);
      return var5;
   }

   public int[] random(RandomColor.Color color, int count) {
      if (var2 <= 0) {
         throw new IllegalArgumentException("count must be greater than 0");
      } else {
         int[] var3 = new int[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = this.randomColor(var1);
         }

         return var3;
      }
   }

   private int pickHue(int hue) {
      RandomColor.Range var2 = this.getHueRange(var1);
      return this.doPickHue(var2);
   }

   private int doPickHue(RandomColor.Range hueRange) {
      int var2 = this.randomWithin(var1);
      if (var2 < 0) {
         var2 += 360;
      }

      return var2;
   }

   private int pickHue(String name) {
      RandomColor.Range var2 = this.getHueRange(var1);
      return this.doPickHue(var2);
   }

   private RandomColor.Range getHueRange(int number) {
      return var1 < 360 && var1 > 0 ? new RandomColor.Range(var1, var1) : new RandomColor.Range(0, 360);
   }

   private RandomColor.Range getHueRange(String name) {
      return this.colors.containsKey(var1) ? ((RandomColor.ColorInfo)this.colors.get(var1)).getHueRange() : new RandomColor.Range(0, 360);
   }

   private int pickSaturation(int hue, RandomColor.SaturationType saturationType, RandomColor.Luminosity luminosity) {
      return this.pickSaturation(this.getColorInfo(var1), var2, var3);
   }

   private int pickSaturation(RandomColor.Color color, RandomColor.SaturationType saturationType, RandomColor.Luminosity luminosity) {
      RandomColor.ColorInfo var4 = (RandomColor.ColorInfo)this.colors.get(var1.name());
      return this.pickSaturation(var4, var2, var3);
   }

   private int pickSaturation(RandomColor.ColorInfo colorInfo, RandomColor.SaturationType saturationType, RandomColor.Luminosity luminosity) {
      if (var2 != null) {
         switch(var2.ordinal()) {
         case 0:
            return this.randomWithin(new RandomColor.Range(0, 100));
         case 1:
            return 0;
         case 2:
            return this.randomWithin(new RandomColor.Range(75, 100));
         case 3:
            return this.randomWithin(new RandomColor.Range(35, 55));
         case 4:
            return this.randomWithin(new RandomColor.Range(55, 75));
         }
      }

      if (var1 == null) {
         return 0;
      } else {
         RandomColor.Range var4 = var1.getSaturationRange();
         int var5 = var4.start;
         int var6 = var4.end;
         if (var3 != null) {
            switch(var3.ordinal()) {
            case 0:
               var5 = var6 - 10;
               break;
            case 1:
               var5 = 55;
               break;
            case 2:
               var6 = 55;
            }
         }

         return this.randomWithin(new RandomColor.Range(var5, var6));
      }
   }

   private int pickBrightness(int hue, int saturation, RandomColor.Luminosity luminosity) {
      RandomColor.ColorInfo var4 = this.getColorInfo(var1);
      return this.pickBrightness(var4, var2, var3);
   }

   private int pickBrightness(RandomColor.Color color, int saturation, RandomColor.Luminosity luminosity) {
      RandomColor.ColorInfo var4 = (RandomColor.ColorInfo)this.colors.get(var1.name());
      return this.pickBrightness(var4, var2, var3);
   }

   private int pickBrightness(RandomColor.ColorInfo colorInfo, int saturation, RandomColor.Luminosity luminosity) {
      int var4 = this.getMinimumBrightness(var1, var2);
      int var5 = 100;
      if (var3 != null) {
         switch(var3.ordinal()) {
         case 1:
            var4 = (var5 + var4) / 2;
            break;
         case 2:
            var5 = var4 + 20;
            break;
         case 3:
            var4 = 0;
            var5 = 100;
         }
      }

      return this.randomWithin(new RandomColor.Range(var4, var5));
   }

   private int getMinimumBrightness(RandomColor.ColorInfo colorInfo, int saturation) {
      if (var1 == null) {
         return 0;
      } else {
         List var3 = var1.getLowerBounds();

         for(int var4 = 0; var4 < var3.size() - 1; ++var4) {
            int var5 = ((RandomColor.Range)var3.get(var4)).start;
            int var6 = ((RandomColor.Range)var3.get(var4)).end;
            if (var4 == var3.size() - 1) {
               break;
            }

            int var7 = ((RandomColor.Range)var3.get(var4 + 1)).start;
            int var8 = ((RandomColor.Range)var3.get(var4 + 1)).end;
            if (var2 >= var5 && var2 <= var7) {
               float var9 = (float)(var8 - var6) / (float)(var7 - var5);
               float var10 = (float)var6 - var9 * (float)var5;
               return (int)(var9 * (float)var2 + var10);
            }
         }

         return 0;
      }
   }

   private RandomColor.ColorInfo getColorInfo(int hue) {
      if (var1 >= 334 && var1 <= 360) {
         var1 -= 360;
      }

      Iterator var2 = this.colors.keySet().iterator();

      RandomColor.ColorInfo var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         String var3 = (String)var2.next();
         var4 = (RandomColor.ColorInfo)this.colors.get(var3);
      } while(var4.getHueRange() == null || !var4.getHueRange().contain(var1));

      return var4;
   }

   private int randomWithin(RandomColor.Range range) {
      return (int)Math.floor((double)var1.start + this.random.nextDouble() * (double)(var1.end + 1 - var1.start));
   }

   public void defineColor(String name, RandomColor.Range hueRange, List<RandomColor.Range> lowerBounds) {
      int var4 = ((RandomColor.Range)var3.get(0)).start;
      int var5 = ((RandomColor.Range)var3.get(var3.size() - 1)).start;
      int var6 = ((RandomColor.Range)var3.get(var3.size() - 1)).end;
      int var7 = ((RandomColor.Range)var3.get(0)).end;
      this.colors.put(var1, new RandomColor.ColorInfo(var2, new RandomColor.Range(var4, var5), new RandomColor.Range(var6, var7), var3));
   }

   private void loadColorBounds() {
      ArrayList var1 = new ArrayList();
      var1.add(new RandomColor.Range(0, 0));
      var1.add(new RandomColor.Range(100, 0));
      this.defineColor(RandomColor.Color.MONOCHROME.name(), new RandomColor.Range(0, 0), var1);
      ArrayList var2 = new ArrayList();
      var2.add(new RandomColor.Range(20, 100));
      var2.add(new RandomColor.Range(30, 92));
      var2.add(new RandomColor.Range(40, 89));
      var2.add(new RandomColor.Range(50, 85));
      var2.add(new RandomColor.Range(60, 78));
      var2.add(new RandomColor.Range(70, 70));
      var2.add(new RandomColor.Range(80, 60));
      var2.add(new RandomColor.Range(90, 55));
      var2.add(new RandomColor.Range(100, 50));
      this.defineColor(RandomColor.Color.RED.name(), new RandomColor.Range(-26, 18), var2);
      ArrayList var3 = new ArrayList();
      var3.add(new RandomColor.Range(20, 100));
      var3.add(new RandomColor.Range(30, 93));
      var3.add(new RandomColor.Range(40, 88));
      var3.add(new RandomColor.Range(50, 86));
      var3.add(new RandomColor.Range(60, 85));
      var3.add(new RandomColor.Range(70, 70));
      var3.add(new RandomColor.Range(100, 70));
      this.defineColor(RandomColor.Color.ORANGE.name(), new RandomColor.Range(19, 46), var3);
      ArrayList var4 = new ArrayList();
      var4.add(new RandomColor.Range(25, 100));
      var4.add(new RandomColor.Range(40, 94));
      var4.add(new RandomColor.Range(50, 89));
      var4.add(new RandomColor.Range(60, 86));
      var4.add(new RandomColor.Range(70, 84));
      var4.add(new RandomColor.Range(80, 82));
      var4.add(new RandomColor.Range(90, 80));
      var4.add(new RandomColor.Range(100, 75));
      this.defineColor(RandomColor.Color.YELLOW.name(), new RandomColor.Range(47, 62), var4);
      ArrayList var5 = new ArrayList();
      var5.add(new RandomColor.Range(30, 100));
      var5.add(new RandomColor.Range(40, 90));
      var5.add(new RandomColor.Range(50, 85));
      var5.add(new RandomColor.Range(60, 81));
      var5.add(new RandomColor.Range(70, 74));
      var5.add(new RandomColor.Range(80, 64));
      var5.add(new RandomColor.Range(90, 50));
      var5.add(new RandomColor.Range(100, 40));
      this.defineColor(RandomColor.Color.GREEN.name(), new RandomColor.Range(63, 178), var5);
      ArrayList var6 = new ArrayList();
      var6.add(new RandomColor.Range(20, 100));
      var6.add(new RandomColor.Range(30, 86));
      var6.add(new RandomColor.Range(40, 80));
      var6.add(new RandomColor.Range(50, 74));
      var6.add(new RandomColor.Range(60, 60));
      var6.add(new RandomColor.Range(70, 52));
      var6.add(new RandomColor.Range(80, 44));
      var6.add(new RandomColor.Range(90, 39));
      var6.add(new RandomColor.Range(100, 35));
      this.defineColor(RandomColor.Color.BLUE.name(), new RandomColor.Range(179, 257), var6);
      ArrayList var7 = new ArrayList();
      var7.add(new RandomColor.Range(20, 100));
      var7.add(new RandomColor.Range(30, 87));
      var7.add(new RandomColor.Range(40, 79));
      var7.add(new RandomColor.Range(50, 70));
      var7.add(new RandomColor.Range(60, 65));
      var7.add(new RandomColor.Range(70, 59));
      var7.add(new RandomColor.Range(80, 52));
      var7.add(new RandomColor.Range(90, 45));
      var7.add(new RandomColor.Range(100, 42));
      this.defineColor(RandomColor.Color.PURPLE.name(), new RandomColor.Range(258, 282), var7);
      ArrayList var8 = new ArrayList();
      var8.add(new RandomColor.Range(20, 100));
      var8.add(new RandomColor.Range(30, 90));
      var8.add(new RandomColor.Range(40, 86));
      var8.add(new RandomColor.Range(60, 84));
      var8.add(new RandomColor.Range(80, 80));
      var8.add(new RandomColor.Range(90, 75));
      var8.add(new RandomColor.Range(100, 73));
      this.defineColor(RandomColor.Color.PINK.name(), new RandomColor.Range(283, 334), var8);
   }

   public static enum SaturationType {
      RANDOM,
      MONOCHROME,
      HIGH,
      LOW,
      MEDIUM;

      // $FF: synthetic method
      private static RandomColor.SaturationType[] $values() {
         return new RandomColor.SaturationType[]{RANDOM, MONOCHROME, HIGH, LOW, MEDIUM};
      }
   }

   public static enum Luminosity {
      BRIGHT,
      LIGHT,
      DARK,
      RANDOM;

      // $FF: synthetic method
      private static RandomColor.Luminosity[] $values() {
         return new RandomColor.Luminosity[]{BRIGHT, LIGHT, DARK, RANDOM};
      }
   }

   public static enum Color {
      MONOCHROME,
      RED,
      ORANGE,
      YELLOW,
      GREEN,
      BLUE,
      PURPLE,
      PINK;

      // $FF: synthetic method
      private static RandomColor.Color[] $values() {
         return new RandomColor.Color[]{MONOCHROME, RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, PINK};
      }
   }

   public static class Range {
      int start;
      int end;

      public Range(int start, int end) {
         this.start = var1;
         this.end = var2;
      }

      public boolean contain(int value) {
         return var1 >= this.start && var1 <= this.end;
      }

      public String toString() {
         return "start: " + this.start + " end: " + this.end;
      }
   }

   public static class ColorInfo {
      RandomColor.Range hueRange;
      RandomColor.Range saturationRange;
      RandomColor.Range brightnessRange;
      List<RandomColor.Range> lowerBounds;

      public ColorInfo(RandomColor.Range hueRange, RandomColor.Range saturationRange, RandomColor.Range brightnessRange, List<RandomColor.Range> lowerBounds) {
         this.hueRange = var1;
         this.saturationRange = var2;
         this.brightnessRange = var3;
         this.lowerBounds = var4;
      }

      public RandomColor.Range getHueRange() {
         return this.hueRange;
      }

      public void setHueRange(RandomColor.Range hueRange) {
         this.hueRange = var1;
      }

      public RandomColor.Range getSaturationRange() {
         return this.saturationRange;
      }

      public void setSaturationRange(RandomColor.Range saturationRange) {
         this.saturationRange = var1;
      }

      public RandomColor.Range getBrightnessRange() {
         return this.brightnessRange;
      }

      public void setBrightnessRange(RandomColor.Range brightnessRange) {
         this.brightnessRange = var1;
      }

      public List<RandomColor.Range> getLowerBounds() {
         return this.lowerBounds;
      }

      public void setLowerBounds(List<RandomColor.Range> lowerBounds) {
         this.lowerBounds = var1;
      }
   }

   public static class Options {
      int hue;
      RandomColor.SaturationType saturationType;
      RandomColor.Luminosity luminosity;

      public int getHue() {
         return this.hue;
      }

      public void setHue(int hue) {
         this.hue = var1;
      }

      public RandomColor.SaturationType getSaturationType() {
         return this.saturationType;
      }

      public void setSaturationType(RandomColor.SaturationType saturationType) {
         this.saturationType = var1;
      }

      public RandomColor.Luminosity getLuminosity() {
         return this.luminosity;
      }

      public void setLuminosity(RandomColor.Luminosity luminosity) {
         this.luminosity = var1;
      }
   }
}
