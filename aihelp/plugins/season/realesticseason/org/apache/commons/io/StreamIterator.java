package org.apache.commons.io;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

class StreamIterator<E> implements Iterator<E>, Closeable {
   private final Iterator<E> iterator;
   private final Stream<E> stream;

   public static <T> Iterator<T> iterator(Stream<T> var0) {
      return (new StreamIterator(var0)).iterator;
   }

   private StreamIterator(Stream<E> var1) {
      this.stream = (Stream)Objects.requireNonNull(var1, "stream");
      this.iterator = var1.iterator();
   }

   public boolean hasNext() {
      boolean var1 = this.iterator.hasNext();
      if (!var1) {
         this.close();
      }

      return var1;
   }

   public E next() {
      Object var1 = this.iterator.next();
      if (var1 == null) {
         this.close();
      }

      return var1;
   }

   public void close() {
      this.stream.close();
   }
}
