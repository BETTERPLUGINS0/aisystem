package com.volmit.iris.util.scheduling.jobs;

import com.volmit.iris.util.collection.KList;
import java.util.Iterator;

public class JobCollection implements Job {
   private final String name;
   private final KList<Job> jobs;
   private String status;

   public JobCollection(String name, Job... jobs) {
      this(var1, new KList(var2));
   }

   public JobCollection(String name, KList<Job> jobs) {
      this.name = var1;
      this.status = null;
      this.jobs = new KList(var2);
   }

   public String getName() {
      return this.status == null ? this.name : this.name + " 》" + this.status;
   }

   public void execute() {
      Iterator var1 = this.jobs.iterator();

      while(var1.hasNext()) {
         Job var2 = (Job)var1.next();
         this.status = var2.getName();
         var2.execute();
      }

      this.status = null;
   }

   public void completeWork() {
   }

   public int getTotalWork() {
      return this.jobs.stream().mapToInt(Job::getTotalWork).sum();
   }

   public int getWorkCompleted() {
      return this.jobs.stream().mapToInt(Job::getWorkCompleted).sum();
   }
}
