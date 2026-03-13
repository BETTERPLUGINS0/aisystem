package com.volmit.iris.util.noise;

import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;

public interface NoiseGenerator {
   double noise(double x);

   double noise(double x, double z);

   double noise(double x, double y, double z);

   default boolean isStatic() {
      return false;
   }

   default boolean isNoScale() {
      return false;
   }

   default ProceduralStream<Double> stream() {
      return ProceduralStream.of(this::noise, this::noise, Interpolated.DOUBLE);
   }

   default OffsetNoiseGenerator offset(long seed) {
      return new OffsetNoiseGenerator(this, seed);
   }
}
