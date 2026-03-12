package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SharedTimer {
   private static final AtomicInteger timerCount = new AtomicInteger(0);
   private static final Logger LOGGER = Logger.getLogger(SharedTimer.class.getName());
   @Nullable
   private volatile Timer timer;
   private final AtomicInteger refCount = new AtomicInteger(0);
   private final ResourceLock lock = new ResourceLock();
   @Nullable
   private LazyCleaner.Cleanable<RuntimeException> timerCleanup;

   public int getRefCount() {
      return this.refCount.get();
   }

   public Timer getTimer() {
      ResourceLock ignore = this.lock.obtain();

      Timer var12;
      try {
         Timer timer = this.timer;
         if (timer == null) {
            int index = timerCount.incrementAndGet();
            ClassLoader prevContextCL = Thread.currentThread().getContextClassLoader();

            try {
               Thread.currentThread().setContextClassLoader((ClassLoader)null);
               this.timer = timer = new Timer("PostgreSQL-JDBC-SharedTimer-" + index, true);
               this.timerCleanup = LazyCleaner.getInstance().register(this.refCount, new SharedTimer.TimerCleanup(timer));
            } finally {
               Thread.currentThread().setContextClassLoader(prevContextCL);
            }
         }

         this.refCount.incrementAndGet();
         var12 = timer;
      } catch (Throwable var11) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var9) {
               var11.addSuppressed(var9);
            }
         }

         throw var11;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var12;
   }

   public void releaseTimer() {
      ResourceLock ignore = this.lock.obtain();

      try {
         int count = this.refCount.decrementAndGet();
         if (count > 0) {
            LOGGER.log(Level.FINEST, "Outstanding references still exist so not closing shared Timer");
         } else if (count == 0) {
            LOGGER.log(Level.FINEST, "No outstanding references to shared Timer, will cancel and close it");
            if (this.timerCleanup != null) {
               this.timerCleanup.clean();
               this.timer = null;
               this.timerCleanup = null;
            }
         } else {
            LOGGER.log(Level.WARNING, "releaseTimer() called too many times; there is probably a bug in the calling code");
            this.refCount.set(0);
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

   static class TimerCleanup implements LazyCleaner.CleaningAction<RuntimeException> {
      private final Timer timer;

      TimerCleanup(Timer timer) {
         this.timer = timer;
      }

      public void onClean(boolean leak) throws RuntimeException {
         this.timer.cancel();
      }
   }
}
