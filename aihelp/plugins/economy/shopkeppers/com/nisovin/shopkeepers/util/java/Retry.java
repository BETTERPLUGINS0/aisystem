package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.concurrent.Callable;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Retry {
   public static <T> T retry(Callable<T> callable, int maxAttempts) throws Exception {
      return retry(callable, maxAttempts, (Retry.Callback)null);
   }

   public static <T> T retry(Callable<T> callable, int maxAttempts, @Nullable Retry.Callback retryCallback) throws Exception {
      Validate.isTrue(maxAttempts > 0, "maxAttempts has to be positive");
      int currentAttempt = 0;
      Exception lastException = null;

      while(true) {
         ++currentAttempt;
         if (currentAttempt <= maxAttempts) {
            try {
               return callable.call();
            } catch (Exception var8) {
               lastException = var8;
               if (retryCallback == null) {
                  continue;
               }

               try {
                  retryCallback.onFailure(currentAttempt, lastException, currentAttempt < maxAttempts);
                  continue;
               } catch (Exception var7) {
                  lastException = var7;
               }
            }
         }

         throw (Exception)Unsafe.assertNonNull(lastException);
      }
   }

   private Retry() {
   }

   public interface Callback {
      void onFailure(int var1, Exception var2, boolean var3) throws Exception;
   }
}
