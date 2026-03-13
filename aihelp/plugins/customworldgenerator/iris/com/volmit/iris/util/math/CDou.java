package com.volmit.iris.util.math;

public class CDou {
   private final double max;
   private double number = 0.0D;

   public CDou(double max) {
      this.max = var1;
   }

   public CDou set(double n) {
      this.number = var1;
      this.circ();
      return this;
   }

   public CDou add(double a) {
      this.number += var1;
      this.circ();
      return this;
   }

   public CDou sub(double a) {
      this.number -= var1;
      this.circ();
      return this;
   }

   public double get() {
      return this.number;
   }

   public void circ() {
      if (this.number < 0.0D) {
         this.number = this.max - (Math.abs(this.number) > this.max ? this.max : Math.abs(this.number));
      }

      this.number %= this.max;
   }
}
