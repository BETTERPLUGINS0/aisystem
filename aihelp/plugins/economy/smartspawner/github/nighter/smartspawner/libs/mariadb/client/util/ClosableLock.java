package github.nighter.smartspawner.libs.mariadb.client.util;

import java.util.concurrent.locks.ReentrantLock;

public final class ClosableLock extends ReentrantLock implements AutoCloseable {
   private static final long serialVersionUID = -8041187539350329669L;

   public ClosableLock closeableLock() {
      this.lock();
      return this;
   }

   public void close() {
      this.unlock();
   }
}
