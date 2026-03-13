package com.volmit.iris.util.stream;

import lombok.Generated;

public class BasicLayer implements ProceduralLayer {
   private final long seed;
   private final double zoom;
   private final double offsetX;
   private final double offsetY;
   private final double offsetZ;

   public BasicLayer(long seed, double zoom) {
      this(var1, var3, 0.0D, 0.0D, 0.0D);
   }

   public BasicLayer(long seed) {
      this(var1, 1.0D);
   }

   public BasicLayer() {
      this(1337L);
   }

   @Generated
   public long getSeed() {
      return this.seed;
   }

   @Generated
   public double getZoom() {
      return this.zoom;
   }

   @Generated
   public double getOffsetX() {
      return this.offsetX;
   }

   @Generated
   public double getOffsetY() {
      return this.offsetY;
   }

   @Generated
   public double getOffsetZ() {
      return this.offsetZ;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof BasicLayer)) {
         return false;
      } else {
         BasicLayer var2 = (BasicLayer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getSeed() != var2.getSeed()) {
            return false;
         } else if (Double.compare(this.getZoom(), var2.getZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetX(), var2.getOffsetX()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetY(), var2.getOffsetY()) != 0) {
            return false;
         } else {
            return Double.compare(this.getOffsetZ(), var2.getOffsetZ()) == 0;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof BasicLayer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getSeed();
      int var13 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getZoom());
      var13 = var13 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getOffsetX());
      var13 = var13 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getOffsetY());
      var13 = var13 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getOffsetZ());
      var13 = var13 * 59 + (int)(var11 >>> 32 ^ var11);
      return var13;
   }

   @Generated
   public String toString() {
      long var10000 = this.getSeed();
      return "BasicLayer(seed=" + var10000 + ", zoom=" + this.getZoom() + ", offsetX=" + this.getOffsetX() + ", offsetY=" + this.getOffsetY() + ", offsetZ=" + this.getOffsetZ() + ")";
   }

   @Generated
   public BasicLayer(final long seed, final double zoom, final double offsetX, final double offsetY, final double offsetZ) {
      this.seed = var1;
      this.zoom = var3;
      this.offsetX = var5;
      this.offsetY = var7;
      this.offsetZ = var9;
   }
}
