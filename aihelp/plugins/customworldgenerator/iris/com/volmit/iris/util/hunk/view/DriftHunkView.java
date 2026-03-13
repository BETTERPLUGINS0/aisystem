package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;

public class DriftHunkView<T> implements Hunk<T> {
   private final int ox;
   private final int oy;
   private final int oz;
   private final Hunk<T> src;

   public DriftHunkView(Hunk<T> src, int ox, int oy, int oz) {
      this.src = var1;
      this.ox = var2;
      this.oy = var3;
      this.oz = var4;
   }

   public void setRaw(int x, int y, int z, T t) {
      this.src.setRaw(var1 + this.ox, var2 + this.oy, var3 + this.oz, var4);
   }

   public T getRaw(int x, int y, int z) {
      return this.src.getRaw(var1 + this.ox, var2 + this.oy, var3 + this.oz);
   }

   public int getWidth() {
      return this.src.getWidth();
   }

   public int getHeight() {
      return this.src.getHeight();
   }

   public int getDepth() {
      return this.src.getDepth();
   }

   public Hunk<T> getSource() {
      return this.src;
   }
}
