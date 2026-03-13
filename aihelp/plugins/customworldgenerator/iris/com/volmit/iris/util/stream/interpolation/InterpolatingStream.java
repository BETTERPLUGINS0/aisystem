package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class InterpolatingStream<T> extends BasicStream<T> implements Interpolator<T> {
   private final InterpolationMethod type;
   private final NoiseProvider np;
   private final int rx;

   public InterpolatingStream(ProceduralStream<T> stream, int rx, InterpolationMethod type) {
      super(var1);
      this.type = var3;
      this.rx = var2;
      this.np = (var1x, var3x) -> {
         return this.getTypedSource().getDouble(var1x, var3x);
      };
   }

   public T interpolate(double x, double y) {
      return this.fromDouble(IrisInterpolation.getNoise(this.type, (int)var1, (int)var3, (double)this.rx, this.np));
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.interpolate(var1, var3);
   }

   public T get(double x, double y, double z) {
      return this.interpolate(var1, var5);
   }
}
