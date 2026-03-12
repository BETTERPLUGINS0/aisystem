package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.Queues;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class ListenerCallQueue<L> {
   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
   private final List<ListenerCallQueue.PerListenerQueue<L>> listeners = Collections.synchronizedList(new ArrayList());

   public void addListener(L listener, Executor executor) {
      Preconditions.checkNotNull(listener, "listener");
      Preconditions.checkNotNull(executor, "executor");
      this.listeners.add(new ListenerCallQueue.PerListenerQueue(listener, executor));
   }

   public void enqueue(ListenerCallQueue.Event<L> event) {
      this.enqueueHelper(event, event);
   }

   public void enqueue(ListenerCallQueue.Event<L> event, String label) {
      this.enqueueHelper(event, label);
   }

   private void enqueueHelper(ListenerCallQueue.Event<L> event, Object label) {
      Preconditions.checkNotNull(event, "event");
      Preconditions.checkNotNull(label, "label");
      synchronized(this.listeners) {
         Iterator var4 = this.listeners.iterator();

         while(var4.hasNext()) {
            ListenerCallQueue.PerListenerQueue<L> queue = (ListenerCallQueue.PerListenerQueue)var4.next();
            queue.add(event, label);
         }

      }
   }

   public void dispatch() {
      for(int i = 0; i < this.listeners.size(); ++i) {
         ((ListenerCallQueue.PerListenerQueue)this.listeners.get(i)).dispatch();
      }

   }

   private static final class PerListenerQueue<L> implements Runnable {
      final L listener;
      final Executor executor;
      @GuardedBy("this")
      final Queue<ListenerCallQueue.Event<L>> waitQueue = Queues.newArrayDeque();
      @GuardedBy("this")
      final Queue<Object> labelQueue = Queues.newArrayDeque();
      @GuardedBy("this")
      boolean isThreadScheduled;

      PerListenerQueue(L listener, Executor executor) {
         this.listener = Preconditions.checkNotNull(listener);
         this.executor = (Executor)Preconditions.checkNotNull(executor);
      }

      synchronized void add(ListenerCallQueue.Event<L> event, Object label) {
         this.waitQueue.add(event);
         this.labelQueue.add(label);
      }

      void dispatch() {
         boolean scheduleEventRunner = false;
         synchronized(this) {
            if (!this.isThreadScheduled) {
               this.isThreadScheduled = true;
               scheduleEventRunner = true;
            }
         }

         if (scheduleEventRunner) {
            try {
               this.executor.execute(this);
            } catch (RuntimeException var6) {
               synchronized(this) {
                  this.isThreadScheduled = false;
               }

               Logger var10000 = ListenerCallQueue.logger;
               Level var10001 = Level.SEVERE;
               String var3 = String.valueOf(this.listener);
               String var4 = String.valueOf(this.executor);
               var10000.log(var10001, (new StringBuilder(42 + String.valueOf(var3).length() + String.valueOf(var4).length())).append("Exception while running callbacks for ").append(var3).append(" on ").append(var4).toString(), var6);
               throw var6;
            }
         }

      }

      public void run() {
         boolean stillRunning = true;

         while(true) {
            boolean var16 = false;

            try {
               var16 = true;
               ListenerCallQueue.Event nextToRun;
               Object nextLabel;
               synchronized(this) {
                  Preconditions.checkState(this.isThreadScheduled);
                  nextToRun = (ListenerCallQueue.Event)this.waitQueue.poll();
                  nextLabel = this.labelQueue.poll();
                  if (nextToRun == null) {
                     this.isThreadScheduled = false;
                     stillRunning = false;
                     var16 = false;
                     break;
                  }
               }

               try {
                  nextToRun.call(this.listener);
               } catch (RuntimeException var19) {
                  Logger var10000 = ListenerCallQueue.logger;
                  Level var10001 = Level.SEVERE;
                  String var5 = String.valueOf(this.listener);
                  String var6 = String.valueOf(nextLabel);
                  var10000.log(var10001, (new StringBuilder(37 + String.valueOf(var5).length() + String.valueOf(var6).length())).append("Exception while executing callback: ").append(var5).append(" ").append(var6).toString(), var19);
               }
            } finally {
               if (var16) {
                  if (stillRunning) {
                     synchronized(this) {
                        this.isThreadScheduled = false;
                     }
                  }

               }
            }
         }

         if (stillRunning) {
            synchronized(this) {
               this.isThreadScheduled = false;
            }
         }

      }
   }

   interface Event<L> {
      void call(L var1);
   }
}
