package com.volmit.iris.util.stream.utility;

import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.concurrent.Semaphore;

public class SemaphoreStream<T> extends BasicStream<T> {
   private final Semaphore semaphore;

   public SemaphoreStream(ProceduralStream<T> stream, int permits) {
      super(var1);
      this.semaphore = new Semaphore(var2);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      try {
         this.semaphore.acquire();
         Object var5 = this.getTypedSource().get(var1, var3);
         this.semaphore.release();
         return var5;
      } catch (InterruptedException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public T get(double x, double y, double z) {
      try {
         this.semaphore.acquire();
         Object var7 = this.getTypedSource().get(var1, var3, var5);
         this.semaphore.release();
         return var7;
      } catch (InterruptedException var8) {
         var8.printStackTrace();
         return null;
      }
   }
}
