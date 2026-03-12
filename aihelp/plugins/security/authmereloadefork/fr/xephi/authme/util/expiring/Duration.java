package fr.xephi.authme.util.expiring;

import java.util.concurrent.TimeUnit;

public class Duration {
   private final long duration;
   private final TimeUnit unit;

   public Duration(long duration, TimeUnit unit) {
      this.duration = duration;
      this.unit = unit;
   }

   public static Duration createWithSuitableUnit(long sourceDuration, TimeUnit sourceUnit) {
      long durationMillis = Math.abs(TimeUnit.MILLISECONDS.convert(sourceDuration, sourceUnit));
      TimeUnit targetUnit;
      if (durationMillis > 86400000L) {
         targetUnit = TimeUnit.DAYS;
      } else if (durationMillis > 3600000L) {
         targetUnit = TimeUnit.HOURS;
      } else if (durationMillis > 60000L) {
         targetUnit = TimeUnit.MINUTES;
      } else {
         targetUnit = TimeUnit.SECONDS;
      }

      long durationInTargetUnit = targetUnit.convert(sourceDuration, sourceUnit);
      return new Duration(durationInTargetUnit, targetUnit);
   }

   public long getDuration() {
      return this.duration;
   }

   public TimeUnit getTimeUnit() {
      return this.unit;
   }
}
