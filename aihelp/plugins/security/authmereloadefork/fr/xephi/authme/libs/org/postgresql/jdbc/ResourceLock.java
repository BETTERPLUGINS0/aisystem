package fr.xephi.authme.libs.org.postgresql.jdbc;

import java.util.concurrent.locks.ReentrantLock;

public final class ResourceLock extends ReentrantLock implements AutoCloseable {
   public ResourceLock obtain() {
      this.lock();
      return this;
   }

   public void close() {
      this.unlock();
   }
}
