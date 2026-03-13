package com.volmit.iris.util.math;

import com.volmit.iris.util.scheduling.Wrapper;

public class FinalInteger extends Wrapper<Integer> {
   public FinalInteger(Integer t) {
      super(var1);
   }

   public void add(int i) {
      this.set((Integer)this.get() + var1);
   }

   public void sub(int i) {
      this.set((Integer)this.get() - var1);
   }
}
