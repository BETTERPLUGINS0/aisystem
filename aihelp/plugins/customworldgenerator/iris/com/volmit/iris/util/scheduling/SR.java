package com.volmit.iris.util.scheduling;

import com.volmit.iris.util.plugin.CancellableTask;

public abstract class SR implements Runnable, CancellableTask {
   private int id;

   public SR() {
      this(0);
   }

   public SR(int interval) {
      this.id = 0;
      this.id = J.sr(this, var1);
   }

   public void cancel() {
      J.csr(this.id);
   }

   public int getId() {
      return this.id;
   }
}
