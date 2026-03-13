package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;

public class SynchronizedHunkView<T> implements Hunk<T> {
   private final Hunk<T> src;

   public SynchronizedHunkView(Hunk<T> src) {
      this.src = var1;
   }

   public void setRaw(int x, int y, int z, T t) {
      synchronized(this.src) {
         this.src.setRaw(var1, var2, var3, var4);
      }
   }

   public T getRaw(int x, int y, int z) {
      return this.src.getRaw(var1, var2, var3);
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
