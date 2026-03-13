package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class ClampedStream<T> extends BasicStream<T> implements ProceduralStream<T> {
   private final double min;
   private final double max;

   public ClampedStream(ProceduralStream<T> stream, double min, double max) {
      super(var1);
      this.min = var2;
      this.max = var4;
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   private double clamp(double v) {
      return Math.max(Math.min(var1, this.max), this.min);
   }

   public T get(double x, double z) {
      return this.fromDouble(this.clamp(this.getTypedSource().getDouble(var1, var3)));
   }

   public T get(double x, double y, double z) {
      return this.fromDouble(this.clamp(this.getTypedSource().getDouble(var1, var3, var5)));
   }
}
