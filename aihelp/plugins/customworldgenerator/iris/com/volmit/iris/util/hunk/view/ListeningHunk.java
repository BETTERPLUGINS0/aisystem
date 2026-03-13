package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.hunk.Hunk;

public class ListeningHunk<T> implements Hunk<T> {
   private final Hunk<T> src;
   private final Consumer4<Integer, Integer, Integer, T> listener;

   public ListeningHunk(Hunk<T> src, Consumer4<Integer, Integer, Integer, T> listener) {
      this.src = var1;
      this.listener = var2;
   }

   public void setRaw(int x, int y, int z, T t) {
      this.listener.accept(var1, var2, var3, var4);
      this.src.setRaw(var1, var2, var3, var4);
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
