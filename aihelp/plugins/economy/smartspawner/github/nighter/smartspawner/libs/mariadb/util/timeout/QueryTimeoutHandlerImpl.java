package github.nighter.smartspawner.libs.mariadb.util.timeout;

import github.nighter.smartspawner.libs.mariadb.Connection;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.client.util.SchedulerProvider;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueryTimeoutHandlerImpl implements QueryTimeoutHandler {
   private Future<?> timerTaskFuture;
   private ScheduledExecutorService timeoutScheduler;
   private Connection conn;
   private ClosableLock lock;

   public QueryTimeoutHandler create(int queryTimeout) {
      assert this.timerTaskFuture == null;

      if (queryTimeout > 0) {
         if (this.timeoutScheduler == null) {
            this.timeoutScheduler = SchedulerProvider.getTimeoutScheduler(this.lock);
         }

         this.timerTaskFuture = this.timeoutScheduler.schedule(() -> {
            try {
               this.conn.cancelCurrentQuery();
            } catch (Throwable var2) {
            }

         }, (long)queryTimeout, TimeUnit.SECONDS);
      }

      return this;
   }

   public QueryTimeoutHandlerImpl(Connection conn, ClosableLock lock) {
      this.conn = conn;
      this.lock = lock;
   }

   public void close() {
      if (this.timerTaskFuture != null) {
         if (!this.timerTaskFuture.cancel(true)) {
            try {
               this.timerTaskFuture.get();
            } catch (ExecutionException | CancellationException | InterruptedException var2) {
            }
         }

         this.timerTaskFuture = null;
      }

   }
}
