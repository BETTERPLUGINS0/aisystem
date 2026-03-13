package com.volmit.iris.util.stream.utility;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class NullSafeStream<T> extends BasicStream<T> implements ProceduralStream<T> {
   private final ProceduralStream<T> stream;
   private final T ifNull;

   public NullSafeStream(ProceduralStream<T> stream, T ifNull) {
      this.stream = var1;
      this.ifNull = var2;
   }

   public double toDouble(T t) {
      return this.stream.toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.stream.fromDouble(var1);
   }

   public T get(double x, double z) {
      Object var5 = this.stream.get(var1, var3);
      return var5 == null ? this.ifNull : var5;
   }

   public T get(double x, double y, double z) {
      Object var7 = this.stream.get(var1, var3, var5);
      return var7 == null ? this.ifNull : var7;
   }
}
