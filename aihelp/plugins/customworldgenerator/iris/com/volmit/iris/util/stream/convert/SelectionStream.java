package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.List;

public class SelectionStream<T> extends BasicStream<T> {
   private final ProceduralStream<Integer> stream;
   private final T[] options;

   public SelectionStream(ProceduralStream<?> stream, T[] options) {
      this.stream = var1.fit(0.0D, (double)(var2.length - 1)).round();
      this.options = var2;
   }

   public SelectionStream(ProceduralStream<?> stream, List<T> options) {
      this(var1, var2.toArray());
   }

   public double toDouble(T t) {
      throw new UnsupportedOperationException();
   }

   public T fromDouble(double d) {
      throw new UnsupportedOperationException();
   }

   public T get(double x, double z) {
      return this.options.length == 0 ? null : this.options[(Integer)this.stream.get(var1, var3)];
   }

   public T get(double x, double y, double z) {
      return this.options.length == 0 ? null : this.options[(Integer)this.stream.get(var1, var3, var5)];
   }
}
