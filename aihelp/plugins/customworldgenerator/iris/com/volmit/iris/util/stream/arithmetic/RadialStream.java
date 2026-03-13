package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class RadialStream<T> extends BasicStream<T> implements ProceduralStream<T> {
   private final double scale;

   public RadialStream(ProceduralStream<T> stream) {
      this(var1, 1.0D);
   }

   public RadialStream(ProceduralStream<T> stream, double scale) {
      super(var1);
      this.scale = var2;
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   private double radii(double v) {
      return var1 / (360.0D * this.scale) % 360.0D;
   }

   public T get(double x, double z) {
      return this.fromDouble(this.radii(this.getTypedSource().getDouble(var1, var3)));
   }

   public T get(double x, double y, double z) {
      return this.fromDouble(this.radii(this.getTypedSource().getDouble(var1, var3, var5)));
   }
}
