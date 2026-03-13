package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;

public class HunkView<T> implements Hunk<T> {
   private final int ox;
   private final int oy;
   private final int oz;
   private final int w;
   private final int h;
   private final int d;
   private final Hunk<T> src;

   public HunkView(Hunk<T> src) {
      this(var1, var1.getWidth(), var1.getHeight(), var1.getDepth());
   }

   public HunkView(Hunk<T> src, int w, int h, int d) {
      this(var1, var2, var3, var4, 0, 0, 0);
   }

   public HunkView(Hunk<T> src, int w, int h, int d, int ox, int oy, int oz) {
      this.src = var1;
      this.w = var2;
      this.h = var3;
      this.d = var4;
      this.ox = var5;
      this.oy = var6;
      this.oz = var7;
   }

   public void setRaw(int x, int y, int z, T t) {
      this.src.setRaw(var1 + this.ox, var2 + this.oy, var3 + this.oz, var4);
   }

   public T getRaw(int x, int y, int z) {
      return this.src.getRaw(var1 + this.ox, var2 + this.oy, var3 + this.oz);
   }

   public int getWidth() {
      return this.w;
   }

   public int getDepth() {
      return this.d;
   }

   public int getHeight() {
      return this.h;
   }

   public Hunk<T> getSource() {
      return this.src;
   }
}
