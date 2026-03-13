package org.terraform.utils;

import java.util.concurrent.ConcurrentHashMap;

public class TickTimer {
   public static final ConcurrentHashMap<String, Long> TIMINGS = new ConcurrentHashMap();
   private final String key;
   private final long start = System.currentTimeMillis();
   private long duration = -1L;

   public TickTimer(String key) {
      this.key = key;
   }

   public void finish() {
      this.duration = System.currentTimeMillis() - this.start;
      TIMINGS.compute(this.key, (k, v) -> {
         return v == null ? this.duration : this.duration + v;
      });
   }
}
