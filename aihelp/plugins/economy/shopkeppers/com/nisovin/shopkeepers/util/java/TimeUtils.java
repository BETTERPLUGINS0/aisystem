package com.nisovin.shopkeepers.util.java;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {
   public static final long NANOS_PER_SECOND;

   public static double convert(double duration, TimeUnit from, TimeUnit to) {
      if (from == to) {
         return duration;
      } else {
         return from.ordinal() < to.ordinal() ? duration / (double)from.convert(1L, to) : duration * (double)to.convert(1L, from);
      }
   }

   public static String getTimeAgoString(Instant instant) {
      Duration duration = Duration.between(instant, Instant.now());
      boolean negative = duration.isNegative();
      duration = duration.abs();
      long days = duration.toDays();
      int hours = duration.toHoursPart();
      int minutes = duration.toMinutesPart();
      int seconds = duration.toSecondsPart();
      StringBuilder timeAgoString = new StringBuilder();
      if (negative) {
         timeAgoString.append('-');
      }

      if (days > 0L) {
         timeAgoString.append(days).append("d ");
      }

      if (hours > 0) {
         timeAgoString.append(hours).append("h ");
      }

      if (minutes > 0) {
         timeAgoString.append(minutes).append("m ");
      }

      if (days == 0L && hours == 0 && minutes == 0) {
         timeAgoString.append(seconds).append("s ");
      }

      return timeAgoString.substring(0, timeAgoString.length() - 1);
   }

   private TimeUtils() {
   }

   static {
      NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1L);
   }
}
