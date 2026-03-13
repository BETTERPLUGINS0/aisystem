package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Snippet;
import java.awt.Color;
import lombok.Generated;

@Snippet("color")
@Desc("Represents a color")
public class IrisColor {
   private final transient AtomicCache<Color> color = new AtomicCache();
   @MaxNumber(7.0D)
   @MinNumber(6.0D)
   @Desc("Pass in a 6 digit hexadecimal color to fill R G and B values. You can also include the # symbol, but it's not required.")
   private String hex = null;
   @MaxNumber(255.0D)
   @MinNumber(0.0D)
   @Desc("Represents the red channel. Only define this if you are not defining the hex value.")
   private int red = 0;
   @MaxNumber(255.0D)
   @MinNumber(0.0D)
   @Desc("Represents the green channel. Only define this if you are not defining the hex value.")
   private int green = 0;
   @MaxNumber(255.0D)
   @MinNumber(0.0D)
   @Desc("Represents the blue channel. Only define this if you are not defining the hex value.")
   private int blue = 0;

   public static Color blend(Color... c) {
      if (var0 != null && var0.length > 0) {
         float var1 = 1.0F / (float)var0.length;
         int var2 = 0;
         int var3 = 0;
         int var4 = 0;
         int var5 = 0;
         Color[] var6 = var0;
         int var7 = var0.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Color var9 = var6[var8];
            int var10 = var9.getRGB();
            int var11 = var10 >> 24 & 255;
            int var12 = (var10 & 16711680) >> 16;
            int var13 = (var10 & '\uff00') >> 8;
            int var14 = var10 & 255;
            var2 = (int)((float)var2 + (float)var11 * var1);
            var3 = (int)((float)var3 + (float)var12 * var1);
            var4 = (int)((float)var4 + (float)var13 * var1);
            var5 = (int)((float)var5 + (float)var14 * var1);
         }

         return new Color(var2 << 24 | var3 << 16 | var4 << 8 | var5);
      } else {
         return null;
      }
   }

   public Color getColor() {
      return (Color)this.color.aquire(() -> {
         if (this.hex != null) {
            String var1 = (this.hex.startsWith("#") ? this.hex : "#" + this.hex).trim();

            try {
               return Color.decode(var1);
            } catch (Throwable var3) {
               Iris.reportError(var3);
            }
         }

         return new Color(this.red, this.green, this.blue);
      });
   }

   public org.bukkit.Color getBukkitColor() {
      return org.bukkit.Color.fromRGB(this.getColor().getRGB());
   }

   public int getAsRGB() {
      if (this.hex != null) {
         try {
            if (this.hex.startsWith("#")) {
               this.hex = this.hex.substring(1);
            }

            return Integer.parseInt(this.hex, 16);
         } catch (NumberFormatException var2) {
            return 0;
         }
      } else {
         return this.red << 16 | this.green << 8 | this.blue;
      }
   }

   @Generated
   public String getHex() {
      return this.hex;
   }

   @Generated
   public int getRed() {
      return this.red;
   }

   @Generated
   public int getGreen() {
      return this.green;
   }

   @Generated
   public int getBlue() {
      return this.blue;
   }

   @Generated
   public IrisColor setHex(final String hex) {
      this.hex = var1;
      return this;
   }

   @Generated
   public IrisColor setRed(final int red) {
      this.red = var1;
      return this;
   }

   @Generated
   public IrisColor setGreen(final int green) {
      this.green = var1;
      return this;
   }

   @Generated
   public IrisColor setBlue(final int blue) {
      this.blue = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisColor)) {
         return false;
      } else {
         IrisColor var2 = (IrisColor)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRed() != var2.getRed()) {
            return false;
         } else if (this.getGreen() != var2.getGreen()) {
            return false;
         } else if (this.getBlue() != var2.getBlue()) {
            return false;
         } else {
            String var3 = this.getHex();
            String var4 = var2.getHex();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisColor;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getRed();
      var4 = var4 * 59 + this.getGreen();
      var4 = var4 * 59 + this.getBlue();
      String var3 = this.getHex();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getColor());
      return "IrisColor(color=" + var10000 + ", hex=" + this.getHex() + ", red=" + this.getRed() + ", green=" + this.getGreen() + ", blue=" + this.getBlue() + ")";
   }
}
