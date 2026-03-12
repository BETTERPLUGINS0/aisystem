package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

final class SSMWWR<K, V> extends SSMWW<K, V> {
   volatile long refreshAfterWriteNanos;

   SSMWWR(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      this.refreshAfterWriteNanos = builder.getRefreshAfterWriteNanos();
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
