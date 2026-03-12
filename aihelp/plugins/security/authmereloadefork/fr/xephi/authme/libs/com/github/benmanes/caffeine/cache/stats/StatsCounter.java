package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.stats;

import fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.RemovalCause;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface StatsCounter {
   void recordHits(@NonNegative int var1);

   void recordMisses(@NonNegative int var1);

   void recordLoadSuccess(@NonNegative long var1);

   void recordLoadFailure(@NonNegative long var1);

   /** @deprecated */
   @Deprecated
   void recordEviction();

   /** @deprecated */
   @Deprecated
   default void recordEviction(@NonNegative int weight) {
      this.recordEviction();
   }

   default void recordEviction(@NonNegative int weight, RemovalCause cause) {
      this.recordEviction(weight);
   }

   @NonNull
   CacheStats snapshot();

   @NonNull
   static StatsCounter disabledStatsCounter() {
      return DisabledStatsCounter.INSTANCE;
   }

   @NonNull
   static StatsCounter guardedStatsCounter(@NonNull StatsCounter statsCounter) {
      return (StatsCounter)(statsCounter instanceof GuardedStatsCounter ? statsCounter : new GuardedStatsCounter(statsCounter));
   }
}
