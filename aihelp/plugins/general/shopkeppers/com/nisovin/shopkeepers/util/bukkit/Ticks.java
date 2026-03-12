package com.nisovin.shopkeepers.util.bukkit;

import java.util.concurrent.TimeUnit;
import org.bukkit.util.NumberConversions;

public final class Ticks {
   public static final int PER_SECOND = 20;
   public static final double DURATION_SECONDS = 0.05D;
   public static final long DURATION_MILLIS;
   public static final long DURATION_NANOS;

   public static double toSeconds(long ticks) {
      return (double)ticks * 0.05D;
   }

   public static long fromSeconds(double seconds) {
      return (long)NumberConversions.round(seconds / 0.05D);
   }

   public static long toMillis(long ticks) {
      return ticks * DURATION_MILLIS;
   }

   public static long fromMillis(long millis) {
      return (long)NumberConversions.round((double)millis / (double)DURATION_MILLIS);
   }

   public static long toNanos(long ticks) {
      return ticks * DURATION_NANOS;
   }

   public static long fromNanos(long nanos) {
      return (long)NumberConversions.round((double)nanos / (double)DURATION_NANOS);
   }

   private Ticks() {
   }

   static {
      DURATION_MILLIS = TimeUnit.SECONDS.toMillis(1L) / 20L;
      DURATION_NANOS = TimeUnit.SECONDS.toNanos(1L) / 20L;
   }
}
