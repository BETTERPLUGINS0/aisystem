package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.function.Function2;
import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class MaxingStream<T> extends BasicStream<T> {
   private final Function3<Double, Double, Double, Double> add;

   public MaxingStream(ProceduralStream<T> stream, Function3<Double, Double, Double, Double> add) {
      super(var1);
      this.add = var2;
   }

   public MaxingStream(ProceduralStream<T> stream, Function2<Double, Double, Double> add) {
      this(var1, (var1x, var2x, var3) -> {
         return (Double)var2.apply(var1x, var3);
      });
   }

   public MaxingStream(ProceduralStream<T> stream, double add) {
      this(var1, (var2x, var3, var4) -> {
         return var2;
      });
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.fromDouble(Math.max((Double)this.add.apply(var1, 0.0D, var3), this.getTypedSource().getDouble(var1, var3)));
   }

   public T get(double x, double y, double z) {
      return this.fromDouble(Math.max((Double)this.add.apply(var1, var3, var5), this.getTypedSource().getDouble(var1, var3, var5)));
   }
}
