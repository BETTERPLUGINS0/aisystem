package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

@FunctionalInterface
public interface CNGFactory {
   CNG create(RNG seed);
}
