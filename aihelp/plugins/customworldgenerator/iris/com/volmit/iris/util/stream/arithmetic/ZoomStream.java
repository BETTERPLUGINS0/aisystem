package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class ZoomStream<T> extends BasicStream<T> {
   private final double ox;
   private final double oy;
   private final double oz;

   public ZoomStream(ProceduralStream<T> stream, double x, double y, double z) {
      super(var1);
      this.ox = var2;
      this.oy = var4;
      this.oz = var6;
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.getTypedSource().get(var1 / this.ox, var3 / this.oz);
   }

   public T get(double x, double y, double z) {
      return this.getTypedSource().get(var1 / this.ox, var3 / this.oy, var5 / this.oz);
   }
}
