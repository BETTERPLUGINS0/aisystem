package com.nisovin.shopkeepers.util.timer;

public interface Timings {
   void reset();

   long getCounter();

   double getAverageTimeMillis();

   double getMaxTimeMillis();
}
