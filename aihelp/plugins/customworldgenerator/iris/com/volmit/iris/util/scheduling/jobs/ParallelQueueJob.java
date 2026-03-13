package com.volmit.iris.util.scheduling.jobs;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import java.util.Iterator;

public abstract class ParallelQueueJob<T> extends QueueJob<T> {
   public void execute() {
      while(this.queue.isNotEmpty()) {
         BurstExecutor var1 = MultiBurst.burst.burst(this.queue.size());
         KList var2 = this.queue.copy();
         this.queue.clear();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            var1.queue(() -> {
               this.execute(var4);
               this.completeWork();
            });
         }

         var1.complete();
      }

   }
}
