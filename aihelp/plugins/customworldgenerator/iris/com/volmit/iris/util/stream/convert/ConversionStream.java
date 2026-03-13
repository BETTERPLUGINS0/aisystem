package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.stream.BasicLayer;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.function.Function;

public class ConversionStream<T, V> extends BasicLayer implements ProceduralStream<V> {
   private final ProceduralStream<T> stream;
   private final Function<T, V> converter;

   public ConversionStream(ProceduralStream<T> stream, Function<T, V> converter) {
      this.stream = var1;
      this.converter = var2;
   }

   public double toDouble(V t) {
      return var1 instanceof Double ? (Double)var1 : 0.0D;
   }

   public V fromDouble(double d) {
      return null;
   }

   public ProceduralStream<V> getTypedSource() {
      return null;
   }

   public ProceduralStream<?> getSource() {
      return null;
   }

   public V get(double x, double z) {
      return this.converter.apply(this.stream.get(var1, var3));
   }

   public V get(double x, double y, double z) {
      return this.converter.apply(this.stream.get(var1, var3, var5));
   }
}
