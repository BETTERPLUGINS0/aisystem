package com.volmit.iris.util.stream.sources;

import com.volmit.iris.util.function.Function2;
import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;

public class FunctionStream<T> extends BasicStream<T> {
   private final Function2<Double, Double, T> f2;
   private final Function3<Double, Double, Double, T> f3;
   private final Interpolated<T> helper;

   public FunctionStream(Function2<Double, Double, T> f2, Function3<Double, Double, Double, T> f3, Interpolated<T> helper) {
      this.f2 = var1;
      this.f3 = var2;
      this.helper = var3;
   }

   public double toDouble(T t) {
      return this.helper.toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.helper.fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.f2.apply(var1, var3);
   }

   public T get(double x, double y, double z) {
      return this.f3.apply(var1, var3, var5);
   }
}
