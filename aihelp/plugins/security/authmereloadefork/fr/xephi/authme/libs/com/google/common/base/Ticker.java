package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class Ticker {
   private static final Ticker SYSTEM_TICKER = new Ticker() {
      public long read() {
         return Platform.systemNanoTime();
      }
   };

   protected Ticker() {
   }

   public abstract long read();

   public static Ticker systemTicker() {
      return SYSTEM_TICKER;
   }
}
