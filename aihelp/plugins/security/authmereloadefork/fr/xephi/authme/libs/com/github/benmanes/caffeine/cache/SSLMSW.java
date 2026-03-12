package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

class SSLMSW<K, V> extends SSLMS<K, V> {
   final Ticker ticker;
   final WriteOrderDeque<Node<K, V>> writeOrderDeque;
   volatile long expiresAfterWriteNanos;
   final Pacer pacer;

   SSLMSW(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      this.ticker = builder.getTicker();
      this.writeOrderDeque = new WriteOrderDeque();
      this.expiresAfterWriteNanos = builder.getExpiresAfterWriteNanos();
      this.pacer = builder.getScheduler() == Scheduler.disabledScheduler() ? null : new Pacer(builder.getScheduler());
   }

   public final Ticker expirationTicker() {
      return this.ticker;
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

   public final Pacer pacer() {
      return this.pacer;
   }
}
