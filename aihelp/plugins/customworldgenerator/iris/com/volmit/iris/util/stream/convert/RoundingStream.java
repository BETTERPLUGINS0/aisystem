package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class RoundingStream extends BasicStream<Integer> {
   private final ProceduralStream<?> stream;

   public RoundingStream(ProceduralStream<?> stream) {
      this.stream = var1;
   }

   public double toDouble(Integer t) {
      return var1.doubleValue();
   }

   public Integer fromDouble(double d) {
      return (int)Math.round(var1);
   }

   private int round(double v) {
      return (int)Math.round(var1);
   }

   public Integer get(double x, double z) {
      return this.round(this.stream.getDouble(var1, var3));
   }

   public Integer get(double x, double y, double z) {
      return this.round(this.stream.getDouble(var1, var3, var5));
   }
}
