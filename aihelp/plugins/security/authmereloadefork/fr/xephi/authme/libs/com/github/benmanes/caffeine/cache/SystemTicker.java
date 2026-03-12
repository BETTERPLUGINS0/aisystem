package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

enum SystemTicker implements Ticker {
   INSTANCE;

   public long read() {
      return System.nanoTime();
   }
}
