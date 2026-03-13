package com.volmit.iris.util.function;

@FunctionalInterface
public interface NoiseInjector {
   double[] combine(double src, double value);
}
