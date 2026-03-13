package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class RoundingDoubleStream extends BasicStream<Double> {
   private final ProceduralStream<?> stream;

   public RoundingDoubleStream(ProceduralStream<?> stream) {
      this.stream = var1;
   }

   public double toDouble(Double t) {
      return var1;
   }

   public Double fromDouble(double d) {
      return (double)Math.round(var1);
   }

   private double round(double v) {
      return (double)Math.round(var1);
   }

   public Double get(double x, double z) {
      return this.round(this.stream.getDouble(var1, var3));
   }

   public Double get(double x, double y, double z) {
      return this.round(this.stream.getDouble(var1, var3, var5));
   }
}
