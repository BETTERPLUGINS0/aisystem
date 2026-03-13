package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class ForceDoubleStream extends BasicStream<Double> {
   private final ProceduralStream<?> stream;

   public ForceDoubleStream(ProceduralStream<?> stream) {
      super((ProceduralStream)null);
      this.stream = var1;
   }

   public double toDouble(Double t) {
      return var1;
   }

   public Double fromDouble(double d) {
      return var1;
   }

   public Double get(double x, double z) {
      return this.stream.getDouble(var1, var3);
   }

   public Double get(double x, double y, double z) {
      return this.stream.getDouble(var1, var3, var5);
   }
}
