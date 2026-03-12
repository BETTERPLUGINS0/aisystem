package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

final class SILSMWAR<K, V> extends SILSMWA<K, V> {
   volatile long refreshAfterWriteNanos;

   SILSMWAR(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
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
