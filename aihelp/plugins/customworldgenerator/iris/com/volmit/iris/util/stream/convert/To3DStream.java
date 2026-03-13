package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class To3DStream<T> extends BasicStream<T> {
   public To3DStream(ProceduralStream<T> stream) {
      super(var1);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.getTypedSource().get(var1, var3);
   }

   public T get(double x, double y, double z) {
      return this.getTypedSource().fromDouble(this.getTypedSource().getDouble(var1, var5) >= var3 ? 1.0D : 0.0D);
   }
}
