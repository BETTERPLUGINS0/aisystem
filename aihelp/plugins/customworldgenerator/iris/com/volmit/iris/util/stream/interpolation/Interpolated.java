package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.CaveResult;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.block.data.BlockData;

public interface Interpolated<T> {
   Interpolated<BlockData> BLOCK_DATA = of((t) -> {
      return 0.0D;
   }, (t) -> {
      return null;
   });
   Interpolated<KList<CaveResult>> CAVE_RESULTS = of((t) -> {
      return 0.0D;
   }, (t) -> {
      return null;
   });
   Interpolated<RNG> RNG = of((t) -> {
      return 0.0D;
   }, (t) -> {
      return null;
   });
   Interpolated<Double> DOUBLE = of((t) -> {
      return t;
   }, (t) -> {
      return t;
   });
   Interpolated<Double[]> DOUBLE_ARRAY = of((t) -> {
      return 0.0D;
   }, (t) -> {
      return new Double[2];
   });
   Interpolated<Boolean> BOOLEAN = of((t) -> {
      return 0.0D;
   }, (t) -> {
      return false;
   });
   Interpolated<Integer> INT = of(Double::valueOf, Double::intValue);
   Interpolated<Long> LONG = of(Double::valueOf, Double::longValue);
   Interpolated<UUID> UUID = of((i) -> {
      return Double.longBitsToDouble(i.getMostSignificantBits());
   }, (i) -> {
      return new UUID(Double.doubleToLongBits(i), i.longValue());
   });

   static <T> Interpolated<T> of(Function<T, Double> a, Function<Double, T> b) {
      return new Interpolated<T>() {
         public double toDouble(T t) {
            return (Double)a.apply(var1);
         }

         public T fromDouble(double d) {
            return b.apply(var1);
         }
      };
   }

   double toDouble(T t);

   T fromDouble(double d);

   default InterpolatorFactory<T> interpolate() {
      if (this instanceof ProceduralStream) {
         return new InterpolatorFactory((ProceduralStream)this);
      } else {
         Iris.warn("Cannot interpolate " + this.getClass().getCanonicalName() + "!");
         return null;
      }
   }
}
