package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

final class WSLSMSWR<K, V> extends WSLSMSW<K, V> {
   volatile long refreshAfterWriteNanos;

   WSLSMSWR(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
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
