package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.stats;

enum DisabledStatsCounter implements StatsCounter {
   INSTANCE;

   public void recordHits(int count) {
   }

   public void recordMisses(int count) {
   }

   public void recordLoadSuccess(long loadTime) {
   }

   public void recordLoadFailure(long loadTime) {
   }

   public void recordEviction() {
   }

   public CacheStats snapshot() {
      return CacheStats.empty();
   }

   public String toString() {
      return this.snapshot().toString();
   }
}
