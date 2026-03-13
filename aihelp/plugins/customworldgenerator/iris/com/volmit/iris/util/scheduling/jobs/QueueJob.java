package com.volmit.iris.util.scheduling.jobs;

import com.volmit.iris.util.collection.KList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class QueueJob<T> implements Job {
   final KList<T> queue = new KList();
   private final AtomicInteger completed = new AtomicInteger(0);
   protected int totalWork = 0;

   public QueueJob queue(T t) {
      this.queue.add((Object)var1);
      ++this.totalWork;
      return this;
   }

   public QueueJob queue(KList<T> f) {
      this.queue.addAll(var1);
      this.totalWork += var1.size();
      return this;
   }

   public abstract void execute(T t);

   public void execute() {
      this.totalWork = this.queue.size();

      while(this.queue.isNotEmpty()) {
         this.execute(this.queue.pop());
         this.completeWork();
      }

   }

   public void completeWork() {
      this.completed.incrementAndGet();
   }

   public int getTotalWork() {
      return this.totalWork;
   }

   public int getWorkCompleted() {
      return this.completed.get();
   }
}
