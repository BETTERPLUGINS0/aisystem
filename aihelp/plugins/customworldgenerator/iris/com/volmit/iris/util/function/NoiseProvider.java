package com.volmit.iris.util.function;

@FunctionalInterface
public interface NoiseProvider {
   double noise(double x, double z);
}
