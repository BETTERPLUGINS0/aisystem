package com.volmit.iris.util.scheduling.jobs;

public class SingleJob implements Job {
   private final String name;
   private final Runnable runnable;
   private boolean done;

   public SingleJob(String name, Runnable runnable) {
      this.name = var1;
      this.done = false;
      this.runnable = var2;
   }

   public String getName() {
      return this.name;
   }

   public void execute() {
      this.runnable.run();
      this.completeWork();
   }

   public void completeWork() {
      this.done = true;
   }

   public int getTotalWork() {
      return 1;
   }

   public int getWorkCompleted() {
      return this.done ? 1 : 0;
   }
}
