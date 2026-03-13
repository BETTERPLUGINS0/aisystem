package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;

public class RotatedZHunkView<T> implements Hunk<T> {
   private final Hunk<T> src;
   private final double sin;
   private final double cos;

   public RotatedZHunkView(Hunk<T> src, double deg) {
      this.src = var1;
      this.sin = Math.sin(Math.toRadians(var2));
      this.cos = Math.cos(Math.toRadians(var2));
   }

   public void setRaw(int x, int y, int z, T t) {
      int var5 = (int)Math.round(this.cos * (double)((float)this.getWidth() / 2.0F) - this.sin * (double)((float)this.getHeight() / 2.0F));
      int var6 = (int)Math.round(this.sin * (double)((float)this.getWidth() / 2.0F) + this.cos * (double)((float)this.getHeight() / 2.0F));
      this.src.setIfExists((int)Math.round(this.cos * (double)(var1 - var5) - this.sin * (double)(var2 - var6)) - var5, (int)Math.round(this.sin * (double)(var1 - var5) + this.cos * (double)(var2 - var6)) - var6, var3, var4);
   }

   public T getRaw(int x, int y, int z) {
      int var4 = (int)Math.round(this.cos * (double)((float)this.getWidth() / 2.0F) - this.sin * (double)((float)this.getHeight() / 2.0F));
      int var5 = (int)Math.round(this.sin * (double)((float)this.getWidth() / 2.0F) + this.cos * (double)((float)this.getHeight() / 2.0F));
      return this.src.getIfExists((int)Math.round(this.cos * (double)(var1 - var4) - this.sin * (double)(var2 - var5)) - var4, (int)Math.round(this.sin * (double)(var1 - var4) + this.cos * (double)(var2 - var5)) - var5, var3);
   }

   public int getWidth() {
      return this.src.getWidth();
   }

   public int getDepth() {
      return this.src.getDepth();
   }

   public int getHeight() {
      return this.src.getHeight();
   }

   public Hunk<T> getSource() {
      return this.src;
   }
}
