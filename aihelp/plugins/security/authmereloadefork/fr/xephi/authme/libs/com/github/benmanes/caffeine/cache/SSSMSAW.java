package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

class SSSMSAW<K, V> extends SSSMSA<K, V> {
   final WriteOrderDeque<Node<K, V>> writeOrderDeque = new WriteOrderDeque();
   volatile long expiresAfterWriteNanos;

   SSSMSAW(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      this.expiresAfterWriteNanos = builder.getExpiresAfterWriteNanos();
   }

   protected final WriteOrderDeque<Node<K, V>> writeOrderDeque() {
      return this.writeOrderDeque;
   }

   protected final boolean expiresAfterWrite() {
      return true;
   }

   protected final long expiresAfterWriteNanos() {
      return this.expiresAfterWriteNanos;
   }

   protected final void setExpiresAfterWriteNanos(long expiresAfterWriteNanos) {
      this.expiresAfterWriteNanos = expiresAfterWriteNanos;
   }
}
