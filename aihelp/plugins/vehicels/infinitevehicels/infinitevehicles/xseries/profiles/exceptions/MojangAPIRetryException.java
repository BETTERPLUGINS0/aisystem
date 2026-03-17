package me.PM2.infinitevehicles.xseries.profiles.exceptions;

public final class MojangAPIRetryException extends MojangAPIException {
   private final MojangAPIRetryException.Reason reason;

   public MojangAPIRetryException(MojangAPIRetryException.Reason var1, String var2) {
      super(var2);
      this.reason = var1;
   }

   public MojangAPIRetryException(MojangAPIRetryException.Reason var1, String var2, Throwable var3) {
      super(var2, var3);
      this.reason = var1;
   }

   public MojangAPIRetryException.Reason getReason() {
      return this.reason;
   }

   public static enum Reason {
      CONNECTION_RESET,
      CONNECTION_TIMEOUT,
      RATELIMITED;

      // $FF: synthetic method
      private static MojangAPIRetryException.Reason[] $values() {
         return new MojangAPIRetryException.Reason[]{CONNECTION_RESET, CONNECTION_TIMEOUT, RATELIMITED};
      }
   }
}
