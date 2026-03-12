package fr.xephi.authme.libs.org.postgresql.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LazyCleaner {
   private static final Logger LOGGER = Logger.getLogger(LazyCleaner.class.getName());
   private static final LazyCleaner instance = new LazyCleaner(Duration.ofMillis(Long.getLong("pgjdbc.config.cleanup.thread.ttl", 30000L)), "PostgreSQL-JDBC-Cleaner");
   private final ReferenceQueue<Object> queue;
   private final long threadTtl;
   private final ThreadFactory threadFactory;
   private boolean threadRunning;
   private int watchedCount;
   private LazyCleaner.Node<?> first;

   public static LazyCleaner getInstance() {
      return instance;
   }

   public LazyCleaner(Duration threadTtl, String threadName) {
      this(threadTtl, (runnable) -> {
         Thread thread = new Thread(runnable, threadName);
         thread.setDaemon(true);
         return thread;
      });
   }

   private LazyCleaner(Duration threadTtl, ThreadFactory threadFactory) {
      this.queue = new ReferenceQueue();
      this.threadTtl = threadTtl.toMillis();
      this.threadFactory = threadFactory;
   }

   public <T extends Throwable> LazyCleaner.Cleanable<T> register(Object obj, LazyCleaner.CleaningAction<T> action) {
      assert obj != action : "object handle should not be the same as cleaning action, otherwise the object will never become phantom reachable, so the action will never trigger";

      return this.add(new LazyCleaner.Node(obj, action));
   }

   public synchronized int getWatchedCount() {
      return this.watchedCount;
   }

   public synchronized boolean isThreadRunning() {
      return this.threadRunning;
   }

   private synchronized boolean checkEmpty() {
      if (this.first == null) {
         this.threadRunning = false;
         return true;
      } else {
         return false;
      }
   }

   private synchronized <T extends Throwable> LazyCleaner.Node<T> add(LazyCleaner.Node<T> node) {
      if (this.first != null) {
         node.next = this.first;
         this.first.prev = node;
      }

      this.first = node;
      ++this.watchedCount;
      if (!this.threadRunning) {
         this.threadRunning = this.startThread();
      }

      return node;
   }

   private boolean startThread() {
      Thread thread = this.threadFactory.newThread(new Runnable() {
         public void run() {
            while(true) {
               while(true) {
                  while(true) {
                     try {
                        Thread.currentThread().setContextClassLoader((ClassLoader)null);
                        Thread.currentThread().setUncaughtExceptionHandler((UncaughtExceptionHandler)null);
                        LazyCleaner.Node<?> ref = (LazyCleaner.Node)LazyCleaner.this.queue.remove(LazyCleaner.this.threadTtl);
                        if (ref != null) {
                           try {
                              ref.onClean(true);
                           } catch (Throwable var3) {
                              if (var3 instanceof InterruptedException) {
                                 LazyCleaner.LOGGER.log(Level.WARNING, "Unexpected interrupt while executing onClean", var3);
                                 throw var3;
                              }

                              LazyCleaner.LOGGER.log(Level.WARNING, "Unexpected exception while executing onClean", var3);
                           }
                        } else {
                           if (!LazyCleaner.this.checkEmpty()) {
                              continue;
                           }
                           break;
                        }
                     } catch (InterruptedException var4) {
                        if (LazyCleaner.this.checkEmpty()) {
                           LazyCleaner.LOGGER.log(Level.FINE, "Cleanup queue is empty, and got interrupt, will terminate the cleanup thread");
                           break;
                        }

                        LazyCleaner.LOGGER.log(Level.FINE, "Ignoring interrupt since the cleanup queue is non-empty");
                     } catch (Throwable var5) {
                        LazyCleaner.LOGGER.log(Level.WARNING, "Unexpected exception in cleaner thread main loop", var5);
                     }
                  }

                  return;
               }
            }
         }
      });
      if (thread != null) {
         thread.start();
         return true;
      } else {
         LOGGER.log(Level.WARNING, "Unable to create cleanup thread");
         return false;
      }
   }

   private synchronized boolean remove(LazyCleaner.Node<?> node) {
      if (node.next == node) {
         return false;
      } else {
         if (this.first == node) {
            this.first = node.next;
         }

         if (node.next != null) {
            node.next.prev = node.prev;
         }

         if (node.prev != null) {
            node.prev.next = node.next;
         }

         node.next = node;
         node.prev = node;
         --this.watchedCount;
         return true;
      }
   }

   private class Node<T extends Throwable> extends PhantomReference<Object> implements LazyCleaner.Cleanable<T>, LazyCleaner.CleaningAction<T> {
      @Nullable
      private final LazyCleaner.CleaningAction<T> action;
      private LazyCleaner.Node<?> prev;
      private LazyCleaner.Node<?> next;

      Node(Object referent, LazyCleaner.CleaningAction<T> action) {
         super(referent, LazyCleaner.this.queue);
         this.action = action;
      }

      public void clean() throws T {
         this.onClean(false);
      }

      public void onClean(boolean leak) throws T {
         if (LazyCleaner.this.remove(this)) {
            if (this.action != null) {
               this.action.onClean(leak);
            }

         }
      }
   }

   public interface CleaningAction<T extends Throwable> {
      void onClean(boolean var1) throws T;
   }

   public interface Cleanable<T extends Throwable> {
      void clean() throws T;
   }
}
