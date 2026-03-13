package com.volmit.iris.util.scheduling;

import com.volmit.iris.util.plugin.CancellableTask;

public abstract class AR implements Runnable, CancellableTask {
   private int id;

   public AR() {
      this(0);
   }

   public AR(int interval) {
      this.id = 0;
      this.id = J.ar(this, var1);
   }

   public void cancel() {
      J.car(this.id);
   }

   public int getId() {
      return this.id;
   }
}
