package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.LazyCleaner;
import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

class PgConnectionCleaningAction implements LazyCleaner.CleaningAction<IOException> {
   private static final Logger LOGGER = Logger.getLogger(PgConnection.class.getName());
   private final ResourceLock lock;
   @Nullable
   private Throwable openStackTrace;
   private final Closeable queryExecutorCloseAction;
   @Nullable
   private Timer cancelTimer;

   PgConnectionCleaningAction(ResourceLock lock, @Nullable Throwable openStackTrace, Closeable queryExecutorCloseAction) {
      this.lock = lock;
      this.openStackTrace = openStackTrace;
      this.queryExecutorCloseAction = queryExecutorCloseAction;
   }

   public Timer getTimer() {
      ResourceLock ignore = this.lock.obtain();

      Timer var3;
      try {
         Timer cancelTimer = this.cancelTimer;
         if (cancelTimer == null) {
            cancelTimer = Driver.getSharedTimer().getTimer();
            this.cancelTimer = cancelTimer;
         }

         var3 = cancelTimer;
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

      return var3;
   }

   public void releaseTimer() {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (this.cancelTimer != null) {
            this.cancelTimer = null;
            Driver.getSharedTimer().releaseTimer();
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

   public void purgeTimerTasks() {
      ResourceLock ignore = this.lock.obtain();

      try {
         Timer timer = this.cancelTimer;
         if (timer != null) {
            timer.purge();
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

   public void onClean(boolean leak) throws IOException {
      if (leak && this.openStackTrace != null) {
         LOGGER.log(Level.WARNING, GT.tr("Leak detected: Connection.close() was not called"), this.openStackTrace);
      }

      this.openStackTrace = null;
      this.releaseTimer();
      this.queryExecutorCloseAction.close();
   }
}
