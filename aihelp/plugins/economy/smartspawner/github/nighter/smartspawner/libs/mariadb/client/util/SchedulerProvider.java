package github.nighter.smartspawner.libs.mariadb.client.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class SchedulerProvider {
   private static ScheduledThreadPoolExecutor timeoutScheduler;

   public static ScheduledThreadPoolExecutor getTimeoutScheduler(ClosableLock lock) {
      if (timeoutScheduler == null) {
         ClosableLock ignore = lock.closeableLock();

         try {
            if (timeoutScheduler == null) {
               timeoutScheduler = new ScheduledThreadPoolExecutor(1, (runnable) -> {
                  Thread result = Executors.defaultThreadFactory().newThread(runnable);
                  result.setName("MariaDb-timeout");
                  result.setDaemon(true);
                  return result;
               });
               timeoutScheduler.setRemoveOnCancelPolicy(true);
            }
         } catch (Throwable var5) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignore != null) {
            ignore.close();
         }
      }

      return timeoutScheduler;
   }
}
