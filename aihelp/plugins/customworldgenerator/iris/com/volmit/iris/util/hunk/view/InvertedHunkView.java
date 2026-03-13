package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;

public class InvertedHunkView<T> implements Hunk<T> {
   private final Hunk<T> src;

   public InvertedHunkView(Hunk<T> src) {
      this.src = var1;
   }

   public void setRaw(int x, int y, int z, T t) {
      this.src.setRaw(var1, this.getHeight() - 1 - var2, var3, var4);
   }

   public T getRaw(int x, int y, int z) {
      return this.src.getRaw(var1, var2, var3);
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
