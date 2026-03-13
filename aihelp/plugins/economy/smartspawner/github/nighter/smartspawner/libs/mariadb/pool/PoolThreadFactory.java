package github.nighter.smartspawner.libs.mariadb.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolThreadFactory implements ThreadFactory {
   private final ThreadFactory parentFactory = Executors.defaultThreadFactory();
   private final AtomicInteger threadId = new AtomicInteger();
   private final String threadName;

   public PoolThreadFactory(String threadName) {
      this.threadName = threadName;
   }

   public Thread newThread(Runnable runnable) {
      Thread result = this.parentFactory.newThread(runnable);
      result.setName(this.threadName + "-" + this.threadId.incrementAndGet());
      result.setDaemon(true);
      return result;
   }
}
