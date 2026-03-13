package com.volmit.iris.util.stream.arithmetic;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class CoordinateBitShiftLeftStream<T> extends BasicStream<T> implements ProceduralStream<T> {
   private final int amount;

   public CoordinateBitShiftLeftStream(ProceduralStream<T> stream, int amount) {
      super(var1);
      this.amount = var2;
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.getTypedSource().get((double)((int)var1 << this.amount), (double)((int)var3 << this.amount));
   }

   public T get(double x, double y, double z) {
      return this.getTypedSource().get((double)((int)var1 << this.amount), (double)((int)var3 << this.amount), (double)((int)var5 << this.amount));
   }
}
