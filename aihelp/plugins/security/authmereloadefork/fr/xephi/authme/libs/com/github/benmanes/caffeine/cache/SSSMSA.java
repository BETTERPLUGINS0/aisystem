package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

class SSSMSA<K, V> extends SSSMS<K, V> {
   final Ticker ticker;
   final Expiry<K, V> expiry;
   final TimerWheel<K, V> timerWheel;
   volatile long expiresAfterAccessNanos;
   final Pacer pacer;

   SSSMSA(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      this.ticker = builder.getTicker();
      this.expiry = builder.getExpiry(this.isAsync);
      this.timerWheel = builder.expiresVariable() ? new TimerWheel(this) : null;
      this.expiresAfterAccessNanos = builder.getExpiresAfterAccessNanos();
      this.pacer = builder.getScheduler() == Scheduler.disabledScheduler() ? null : new Pacer(builder.getScheduler());
   }

   public final Ticker expirationTicker() {
      return this.ticker;
   }

   protected boolean fastpath() {
      return false;
   }

   protected final boolean expiresVariable() {
      return this.timerWheel != null;
   }

   protected final Expiry<K, V> expiry() {
      return this.expiry;
   }

   protected final TimerWheel<K, V> timerWheel() {
      return this.timerWheel;
   }

   protected final boolean expiresAfterAccess() {
      return this.timerWheel == null;
   }

   protected final long expiresAfterAccessNanos() {
      return this.expiresAfterAccessNanos;
   }

   protected final void setExpiresAfterAccessNanos(long expiresAfterAccessNanos) {
      this.expiresAfterAccessNanos = expiresAfterAccessNanos;
   }

   public final Pacer pacer() {
      return this.pacer;
   }
}
