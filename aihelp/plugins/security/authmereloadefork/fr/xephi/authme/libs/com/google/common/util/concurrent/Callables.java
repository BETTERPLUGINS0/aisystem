package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Supplier;
import java.util.concurrent.Callable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public final class Callables {
   private Callables() {
   }

   public static <T> Callable<T> returning(@ParametricNullness T value) {
      return () -> {
         return value;
      };
   }

   @Beta
   @GwtIncompatible
   public static <T> AsyncCallable<T> asAsyncCallable(Callable<T> callable, ListeningExecutorService listeningExecutorService) {
      Preconditions.checkNotNull(callable);
      Preconditions.checkNotNull(listeningExecutorService);
      return () -> {
         return listeningExecutorService.submit(callable);
      };
   }

   @GwtIncompatible
   static <T> Callable<T> threadRenaming(Callable<T> callable, Supplier<String> nameSupplier) {
      Preconditions.checkNotNull(nameSupplier);
      Preconditions.checkNotNull(callable);
      return () -> {
         Thread currentThread = Thread.currentThread();
         String oldName = currentThread.getName();
         boolean restoreName = trySetName((String)nameSupplier.get(), currentThread);
         boolean var10 = false;

         Object var5;
         try {
            var10 = true;
            var5 = callable.call();
            var10 = false;
         } finally {
            if (var10) {
               if (restoreName) {
                  trySetName(oldName, currentThread);
               }

            }
         }

         if (restoreName) {
            trySetName(oldName, currentThread);
         }

         return var5;
      };
   }

   @GwtIncompatible
   static Runnable threadRenaming(Runnable task, Supplier<String> nameSupplier) {
      Preconditions.checkNotNull(nameSupplier);
      Preconditions.checkNotNull(task);
      return () -> {
         Thread currentThread = Thread.currentThread();
         String oldName = currentThread.getName();
         boolean restoreName = trySetName((String)nameSupplier.get(), currentThread);
         boolean var9 = false;

         try {
            var9 = true;
            task.run();
            var9 = false;
         } finally {
            if (var9) {
               if (restoreName) {
                  trySetName(oldName, currentThread);
               }

            }
         }

         if (restoreName) {
            trySetName(oldName, currentThread);
         }

      };
   }

   @GwtIncompatible
   private static boolean trySetName(String threadName, Thread currentThread) {
      try {
         currentThread.setName(threadName);
         return true;
      } catch (SecurityException var3) {
         return false;
      }
   }
}
