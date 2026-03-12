package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.stats.StatsCounter;

class WSLS<K, V> extends WSL<K, V> {
   final StatsCounter statsCounter;

   WSLS(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      this.statsCounter = (StatsCounter)builder.getStatsCounterSupplier().get();
   }

   public final boolean isRecordingStats() {
      return true;
   }

   public final Ticker statsTicker() {
      return Ticker.systemTicker();
   }

   public final StatsCounter statsCounter() {
      return this.statsCounter;
   }
}
