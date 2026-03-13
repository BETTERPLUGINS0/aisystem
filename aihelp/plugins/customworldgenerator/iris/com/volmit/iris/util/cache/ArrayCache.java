package com.volmit.iris.util.cache;

import com.volmit.iris.util.hunk.bits.Writable;
import java.io.DataOutputStream;
import java.io.IOException;

public interface ArrayCache<T> extends Writable<T> {
   static int zigZag(int coord, int size) {
      if (coord < 0) {
         coord = Math.abs(coord);
      }

      return coord % (size * 2) >= size ? size - coord % size - 1 : coord % size;
   }

   T get(int i);

   void set(int i, T t);

   void iset(int i, int v);

   int getWidth();

   int getHeight();

   void writeCache(DataOutputStream dos) throws IOException;

   default void set(int x, int y, T v) {
      this.set(zigZag(y, this.getHeight()) * this.getWidth() + zigZag(x, this.getWidth()), v);
   }

   default T get(int x, int y) {
      try {
         return this.get(zigZag(y, this.getHeight()) * this.getWidth() + zigZag(x, this.getWidth()));
      } catch (Throwable var4) {
         var4.printStackTrace();
         throw var4;
      }
   }

   default void iset(int x, int y, int v) {
      this.iset(zigZag(y, this.getHeight()) * this.getWidth() + zigZag(x, this.getWidth()), v);
   }
}
