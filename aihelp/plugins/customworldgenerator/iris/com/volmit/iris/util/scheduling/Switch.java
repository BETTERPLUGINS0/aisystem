package com.volmit.iris.util.scheduling;

public class Switch {
   private volatile boolean b = false;

   public void flip() {
      this.b = true;
   }

   public boolean isFlipped() {
      return this.b;
   }

   public void reset() {
      this.b = false;
   }
}
