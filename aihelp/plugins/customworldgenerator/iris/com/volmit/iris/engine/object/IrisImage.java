package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IrisImage extends IrisRegistrant {
   private final BufferedImage image;

   public IrisImage() {
      this(new BufferedImage(4, 4, 1));
   }

   public IrisImage(BufferedImage image) {
      this.image = var1;
   }

   public int getWidth() {
      return this.image.getWidth();
   }

   public int getHeight() {
      return this.image.getHeight();
   }

   public int getRawValue(int x, int z) {
      return var1 < this.getWidth() && var2 < this.getHeight() && var1 >= 0 && var2 >= 0 ? this.image.getRGB(var1, var2) : 0;
   }

   public double getValue(IrisImageChannel channel, int x, int z) {
      int var4 = this.getRawValue(var2, var3);
      float[] var5;
      switch(var1) {
      case RED:
         return (double)(var4 >> 16 & 255) / 255.0D;
      case GREEN:
         return (double)(var4 >> 8 & 255) / 255.0D;
      case BLUE:
         return (double)(var4 & 255) / 255.0D;
      case SATURATION:
         return (double)Color.RGBtoHSB(var4 >> 16 & 255, var4 >> 8 & 255, var4 & 255, (float[])null)[1];
      case HUE:
         return (double)Color.RGBtoHSB(var4 >> 16 & 255, var4 >> 8 & 255, var4 & 255, (float[])null)[0];
      case BRIGHTNESS:
         return (double)Color.RGBtoHSB(var4 >> 16 & 255, var4 >> 8 & 255, var4 & 255, (float[])null)[2];
      case COMPOSITE_ADD_RGB:
         return ((double)(var4 >> 16 & 255) / 255.0D + (double)(var4 >> 8 & 255) / 255.0D + (double)(var4 & 255) / 255.0D) / 3.0D;
      case COMPOSITE_MUL_RGB:
         return (double)(var4 >> 16 & 255) / 255.0D * ((double)(var4 >> 8 & 255) / 255.0D) * ((double)(var4 & 255) / 255.0D);
      case COMPOSITE_MAX_RGB:
         return Math.max(Math.max((double)(var4 >> 16 & 255) / 255.0D, (double)(var4 >> 8 & 255) / 255.0D), (double)(var4 & 255) / 255.0D);
      case COMPOSITE_ADD_HSB:
         var5 = Color.RGBtoHSB(var4 >> 16 & 255, var4 >> 8 & 255, var4 & 255, (float[])null);
         return (double)(var5[0] + var5[1] + var5[2]) / 3.0D;
      case COMPOSITE_MUL_HSB:
         var5 = Color.RGBtoHSB(var4 >> 16 & 255, var4 >> 8 & 255, var4 & 255, (float[])null);
         return (double)(var5[0] * var5[1] * var5[2]);
      case COMPOSITE_MAX_HSB:
         var5 = Color.RGBtoHSB(var4 >> 16 & 255, var4 >> 8 & 255, var4 & 255, (float[])null);
         return (double)Math.max(var5[0], Math.max(var5[1], var5[2]));
      case RAW:
         return (double)var4;
      default:
         return (double)var4;
      }
   }

   public String getFolderName() {
      return "images";
   }

   public String getTypeName() {
      return "Image";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   public void writeDebug(IrisImageChannel channel) {
      try {
         File var2 = new File(this.getLoadFile().getParentFile(), "debug-see-" + this.getLoadFile().getName());
         BufferedImage var3 = new BufferedImage(this.getWidth(), this.getHeight(), 1);

         for(int var4 = 0; var4 < this.getWidth(); ++var4) {
            for(int var5 = 0; var5 < this.getHeight(); ++var5) {
               var3.setRGB(var4, var5, Color.getHSBColor(0.0F, 0.0F, (float)this.getValue(var1, var4, var5)).getRGB());
            }
         }

         ImageIO.write(var3, "png", var2);
         Iris.warn("Debug image written to " + var2.getPath() + " for channel " + var1.name());
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }
}
