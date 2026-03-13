package com.volmit.iris.util.data;

public class Dimension {
   private final int width;
   private final int height;
   private final int depth;

   public Dimension(int width, int height, int depth) {
      this.width = var1;
      this.height = var2;
      this.depth = var3;
   }

   public Dimension(int width, int height) {
      this.width = var1;
      this.height = var2;
      this.depth = 0;
   }

   public DimensionFace getPane() {
      if (this.width == 1) {
         return DimensionFace.X;
      } else if (this.height == 1) {
         return DimensionFace.Y;
      } else {
         return this.depth == 1 ? DimensionFace.Z : null;
      }
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getDepth() {
      return this.depth;
   }
}
