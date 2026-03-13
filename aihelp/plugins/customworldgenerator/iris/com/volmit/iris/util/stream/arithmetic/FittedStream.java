package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class FittedStream<T> extends BasicStream<T> implements ProceduralStream<T> {
   private final double min;
   private final double max;
   private final double inMin;
   private final double inMax;

   public FittedStream(ProceduralStream<T> stream, double inMin, double inMax, double min, double max) {
      super(var1);
      this.inMin = var2;
      this.inMax = var4;
      this.min = var6;
      this.max = var8;
   }

   public FittedStream(ProceduralStream<T> stream, double min, double max) {
      this(var1, 0.0D, 1.0D, var2, var4);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   private double dlerp(double v) {
      return this.min + (this.max - this.min) * ((var1 - this.inMin) / (this.inMax - this.inMin));
   }

   public T get(double x, double z) {
      return this.fromDouble(this.dlerp(this.getTypedSource().getDouble(var1, var3)));
   }

   public T get(double x, double y, double z) {
      return this.fromDouble(this.dlerp(this.getTypedSource().getDouble(var1, var3, var5)));
   }
}
