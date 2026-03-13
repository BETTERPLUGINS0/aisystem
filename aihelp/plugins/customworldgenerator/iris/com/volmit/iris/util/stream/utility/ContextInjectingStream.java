package com.volmit.iris.util.stream.utility;

import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class ContextInjectingStream<T> extends BasicStream<T> {
   private final Function3<ChunkContext, Integer, Integer, T> contextAccessor;

   public ContextInjectingStream(ProceduralStream<T> stream, Function3<ChunkContext, Integer, Integer, T> contextAccessor) {
      super(var1);
      this.contextAccessor = var2;
   }

   public T get(double x, double z) {
      IrisContext var5 = IrisContext.get();
      if (var5 != null) {
         ChunkContext var6 = var5.getChunkContext();
         if (var6 != null && (int)var1 >> 4 == var6.getX() >> 4 && (int)var3 >> 4 == var6.getZ() >> 4) {
            Object var7 = this.contextAccessor.apply(var6, (int)var1 & 15, (int)var3 & 15);
            if (var7 != null) {
               return var7;
            }
         }
      }

      return this.getTypedSource().get(var1, var3);
   }

   public T get(double x, double y, double z) {
      return this.getTypedSource().get(var1, var3, var5);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }
}
