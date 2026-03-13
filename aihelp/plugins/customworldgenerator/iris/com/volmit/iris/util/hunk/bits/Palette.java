package com.volmit.iris.util.hunk.bits;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.function.Consumer2;
import com.volmit.iris.util.function.Consumer2IO;
import java.io.DataInputStream;
import java.io.IOException;

public interface Palette<T> {
   T get(int id);

   int add(T t);

   int id(T t);

   int size();

   default int bits() {
      return DataContainer.bits(this.size() + 1);
   }

   void iterate(Consumer2<T, Integer> c);

   default void iterateIO(Consumer2IO<T, Integer> c) {
      this.iterate((a, b) -> {
         try {
            c.accept(a, b);
         } catch (IOException var4) {
            var4.printStackTrace();
         }

      });
   }

   default Palette<T> from(int size, Writable<T> writable, DataInputStream in) throws IOException {
      for(int i = 0; i < size; ++i) {
         this.add(writable.readNodeData(in));
      }

      return this;
   }

   default Palette<T> from(Palette<T> oldPalette) {
      oldPalette.iterate((k, v) -> {
         this.add(k);
      });
      return this;
   }

   default KList<T> list() {
      KList<T> t = new KList();
      this.iterate((tx, __) -> {
         t.add((Object)tx);
      });
      return t;
   }
}
