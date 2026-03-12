package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

final class SISMWR<K, V> extends SISMW<K, V> {
   final Ticker ticker;
   volatile long refreshAfterWriteNanos;

   SISMWR(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      this.ticker = builder.getTicker();
      this.refreshAfterWriteNanos = builder.getRefreshAfterWriteNanos();
   }

   public Ticker expirationTicker() {
      return this.ticker;
   }

   protected boolean refreshAfterWrite() {
      return true;
   }

   protected long refreshAfterWriteNanos() {
      return this.refreshAfterWriteNanos;
   }

   protected void setRefreshAfterWriteNanos(long refreshAfterWriteNanos) {
      this.refreshAfterWriteNanos = refreshAfterWriteNanos;
   }
}
