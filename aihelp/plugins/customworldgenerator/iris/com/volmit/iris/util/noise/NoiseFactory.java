package com.volmit.iris.util.noise;

@FunctionalInterface
public interface NoiseFactory {
   NoiseGenerator create(long seed);
}
