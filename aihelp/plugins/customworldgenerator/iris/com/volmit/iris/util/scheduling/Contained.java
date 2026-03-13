package com.volmit.iris.util.scheduling;

import java.util.function.Function;

public class Contained<T> {
   private T t;

   public void mod(Function<T, T> x) {
      this.set(var1.apply(this.t));
   }

   public T get() {
      return this.t;
   }

   public void set(T t) {
      this.t = var1;
   }
}
