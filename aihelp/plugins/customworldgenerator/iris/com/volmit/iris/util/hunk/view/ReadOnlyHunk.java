package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;

public class ReadOnlyHunk<T> implements Hunk<T> {
   private final Hunk<T> src;

   public ReadOnlyHunk(Hunk<T> src) {
      this.src = var1;
   }

   public void setRaw(int x, int y, int z, T t) {
      throw new IllegalStateException("This hunk is read only!");
   }

   public T getRaw(int x, int y, int z) {
      return this.src.getRaw(var1, var2, var3);
   }

   public void set(int x1, int y1, int z1, int x2, int y2, int z2, T t) {
      throw new IllegalStateException("This hunk is read only!");
   }

   public void fill(T t) {
      throw new IllegalStateException("This hunk is read only!");
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
