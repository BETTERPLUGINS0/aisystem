package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

enum DisabledTicker implements Ticker {
   INSTANCE;

   public long read() {
      return 0L;
   }
}
