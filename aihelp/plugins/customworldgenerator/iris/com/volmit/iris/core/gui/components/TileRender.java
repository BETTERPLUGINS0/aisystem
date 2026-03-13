package com.volmit.iris.core.gui.components;

import java.awt.image.BufferedImage;
import lombok.Generated;

public class TileRender {
   private BufferedImage image;
   private int quality;

   @Generated
   TileRender(final BufferedImage image, final int quality) {
      this.image = var1;
      this.quality = var2;
   }

   @Generated
   public static TileRender.TileRenderBuilder builder() {
      return new TileRender.TileRenderBuilder();
   }

   @Generated
   public BufferedImage getImage() {
      return this.image;
   }

   @Generated
   public int getQuality() {
      return this.quality;
   }

   @Generated
   public void setImage(final BufferedImage image) {
      this.image = var1;
   }

   @Generated
   public void setQuality(final int quality) {
      this.quality = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TileRender)) {
         return false;
      } else {
         TileRender var2 = (TileRender)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getQuality() != var2.getQuality()) {
            return false;
         } else {
            BufferedImage var3 = this.getImage();
            BufferedImage var4 = var2.getImage();
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
      return var1 instanceof TileRender;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getQuality();
      BufferedImage var3 = this.getImage();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getImage());
      return "TileRender(image=" + var10000 + ", quality=" + this.getQuality() + ")";
   }

   @Generated
   public static class TileRenderBuilder {
      @Generated
      private BufferedImage image;
      @Generated
      private int quality;

      @Generated
      TileRenderBuilder() {
      }

      @Generated
      public TileRender.TileRenderBuilder image(final BufferedImage image) {
         this.image = var1;
         return this;
      }

      @Generated
      public TileRender.TileRenderBuilder quality(final int quality) {
         this.quality = var1;
         return this;
      }

      @Generated
      public TileRender build() {
         return new TileRender(this.image, this.quality);
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.image);
         return "TileRender.TileRenderBuilder(image=" + var10000 + ", quality=" + this.quality + ")";
      }
   }
}
