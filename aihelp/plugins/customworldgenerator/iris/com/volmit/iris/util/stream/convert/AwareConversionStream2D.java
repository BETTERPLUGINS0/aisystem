package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class AwareConversionStream2D<T, V> extends BasicStream<V> {
   private final ProceduralStream<T> stream;
   private final Function3<T, Double, Double, V> converter;

   public AwareConversionStream2D(ProceduralStream<T> stream, Function3<T, Double, Double, V> converter) {
      super((ProceduralStream)null);
      this.stream = var1;
      this.converter = var2;
   }

   public double toDouble(V t) {
      return var1 instanceof Double ? (Double)var1 : 0.0D;
   }

   public V fromDouble(double d) {
      return null;
   }

   public ProceduralStream<?> getSource() {
      return this.stream;
   }

   public V get(double x, double z) {
      return this.converter.apply(this.stream.get(var1, var3), var1, var3);
   }

   public V get(double x, double y, double z) {
      return this.converter.apply(this.stream.get(var1, var3, var5), var1, var5);
   }
}
