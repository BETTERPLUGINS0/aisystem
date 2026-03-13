package com.volmit.iris.util.uniques;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class UBufferedImage implements UImage {
   private final BufferedImage buf;

   public UBufferedImage(BufferedImage buf) {
      this.buf = var1;
   }

   public int getWidth() {
      return this.buf.getWidth();
   }

   public int getHeight() {
      return this.buf.getHeight();
   }

   public UImage copy() {
      ColorModel var1 = this.buf.getColorModel();
      boolean var2 = var1.isAlphaPremultiplied();
      WritableRaster var3 = this.buf.copyData((WritableRaster)null);
      return new UBufferedImage(new BufferedImage(var1, var3, var2, (Hashtable)null));
   }

   public Color get(int x, int y) {
      return new Color(this.buf.getRGB(var1, var2));
   }

   public void set(int x, int y, Color color) {
      try {
         this.buf.setRGB(var1, var2, var3.getRGB());
      } catch (Throwable var5) {
      }

   }
}
