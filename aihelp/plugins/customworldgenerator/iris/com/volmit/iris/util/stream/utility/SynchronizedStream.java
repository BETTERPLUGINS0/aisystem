package com.volmit.iris.util.stream.utility;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class SynchronizedStream<T> extends BasicStream<T> {
   public SynchronizedStream(ProceduralStream<T> stream) {
      super(var1);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      synchronized(this.getTypedSource()) {
         return this.getTypedSource().get(var1, var3);
      }
   }

   public T get(double x, double y, double z) {
      synchronized(this.getTypedSource()) {
         return this.getTypedSource().get(var1, var3, var5);
      }
   }
}
