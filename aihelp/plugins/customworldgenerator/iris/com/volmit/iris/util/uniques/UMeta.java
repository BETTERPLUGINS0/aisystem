package com.volmit.iris.util.uniques;

import com.google.gson.GsonBuilder;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.io.IO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import lombok.Generated;

public class UMeta {
   private transient BufferedImage image;
   private KMap<String, UFeatureMeta> features;
   private long id;
   private double time;
   private int width;
   private int height;

   public void registerFeature(String key, UFeatureMeta feature) {
      if (this.features == null) {
         this.features = new KMap();
      }

      this.features.put(var1, var2);
   }

   public void export(File destination) {
      Iterator var2 = this.features.k().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (((UFeatureMeta)this.features.get(var3)).isEmpty()) {
            this.features.remove(var3);
         }
      }

      this.width = this.image.getWidth();
      this.height = this.image.getHeight();
      ImageIO.write(this.image, "PNG", var1);
      IO.writeAll(new File(var1.getParentFile(), var1.getName() + ".json"), (Object)(new GsonBuilder()).setPrettyPrinting().create().toJson(this));
   }

   @Generated
   public BufferedImage getImage() {
      return this.image;
   }

   @Generated
   public KMap<String, UFeatureMeta> getFeatures() {
      return this.features;
   }

   @Generated
   public long getId() {
      return this.id;
   }

   @Generated
   public double getTime() {
      return this.time;
   }

   @Generated
   public int getWidth() {
      return this.width;
   }

   @Generated
   public int getHeight() {
      return this.height;
   }

   @Generated
   public void setImage(final BufferedImage image) {
      this.image = var1;
   }

   @Generated
   public void setFeatures(final KMap<String, UFeatureMeta> features) {
      this.features = var1;
   }

   @Generated
   public void setId(final long id) {
      this.id = var1;
   }

   @Generated
   public void setTime(final double time) {
      this.time = var1;
   }

   @Generated
   public void setWidth(final int width) {
      this.width = var1;
   }

   @Generated
   public void setHeight(final int height) {
      this.height = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof UMeta)) {
         return false;
      } else {
         UMeta var2 = (UMeta)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getId() != var2.getId()) {
            return false;
         } else if (Double.compare(this.getTime(), var2.getTime()) != 0) {
            return false;
         } else if (this.getWidth() != var2.getWidth()) {
            return false;
         } else if (this.getHeight() != var2.getHeight()) {
            return false;
         } else {
            KMap var3 = this.getFeatures();
            KMap var4 = var2.getFeatures();
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
      return var1 instanceof UMeta;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getId();
      int var8 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getTime());
      var8 = var8 * 59 + (int)(var5 >>> 32 ^ var5);
      var8 = var8 * 59 + this.getWidth();
      var8 = var8 * 59 + this.getHeight();
      KMap var7 = this.getFeatures();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getImage());
      return "UMeta(image=" + var10000 + ", features=" + String.valueOf(this.getFeatures()) + ", id=" + this.getId() + ", time=" + this.getTime() + ", width=" + this.getWidth() + ", height=" + this.getHeight() + ")";
   }
}
